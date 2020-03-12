package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RobotMap;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Robot;

public class Shooter extends SubsystemBase {
  public enum HoodState {
    WALL_SHOT(Value.kReverse),
    AUTOLINE_SHOT(Value.kForward);

    private Value state;

    private HoodState(Value state) {
      this.state = state;
    }
  }

  private CANSparkMax flywheelLeft, flywheelRight, accelerator;
  private CANEncoder encoder;
  private int shotsFired;
  private Timer currentMonitorTimer;
  private DoubleSolenoid hoodPiston;
  public HoodState hoodState;
  public DigitalInput ballSensor;

  public Shooter() {
    shotsFired = 0;
    currentMonitorTimer = new Timer();

    flywheelLeft = new CANSparkMax(RobotMap.kFlywheelMotorLeft, MotorType.kBrushless);
    flywheelRight = new CANSparkMax(RobotMap.kFlywheelMotorRight, MotorType.kBrushless);
    accelerator = new CANSparkMax(RobotMap.kAcceleratorMotor, MotorType.kBrushless);

    ballSensor = new DigitalInput(0);

    hoodPiston =
        new DoubleSolenoid(RobotMap.kHoodDoubleSolenoidFront, RobotMap.kHoodDoubleSolenoidBack);
    hoodState = null;

    flywheelLeft.restoreFactoryDefaults();
    flywheelRight.restoreFactoryDefaults();
    accelerator.restoreFactoryDefaults();

    encoder = flywheelLeft.getEncoder();

    flywheelLeft.setInverted(true);
    flywheelRight.follow(flywheelLeft, true);
    accelerator.setInverted(true);

    flywheelLeft
        .getEncoder()
        .setPositionConversionFactor(1.0 / ShooterConstants.kFlywheelGearRatio);
    flywheelLeft
        .getEncoder()
        .setVelocityConversionFactor(1.0 / ShooterConstants.kFlywheelGearRatio);
    accelerator
        .getEncoder()
        .setPositionConversionFactor(1.0 / ShooterConstants.kAcceleratorGearRatio);
    accelerator
        .getEncoder()
        .setVelocityConversionFactor(1.0 / ShooterConstants.kAcceleratorGearRatio);

    flywheelLeft.setIdleMode(IdleMode.kCoast);
    flywheelRight.setIdleMode(IdleMode.kCoast);
    accelerator.setIdleMode(IdleMode.kCoast);

    flywheelLeft.setSmartCurrentLimit(ShooterConstants.kFlywheelCurrentLimit);
    accelerator.setSmartCurrentLimit(ShooterConstants.kAcceleratorCurrentLimit);

    SmartDashboard.putNumber("Shooter setpoint (RPM)", 0);
    SmartDashboard.putNumber("Shooter accelerator RPM", 0);

    flywheelLeft.getPIDController().setP(ShooterConstants.kFlywheelkP);
    flywheelLeft.getPIDController().setFF(ShooterConstants.kFlywheelkF);
    flywheelLeft.getPIDController().setOutputRange(-1, 1);
  }

  public boolean getBallSensor() {
    return !ballSensor.get();
  }

  public void setFlywheelToRPM(double rpm) {
    if (rpm == 0) {
      flywheelLeft.set(0.0);
    } else {
      flywheelLeft.getPIDController().setReference(rpm, ControlType.kVelocity);
    }
  }

  public void setAcceleratorToRPM(double rpm) {
    accelerator.set(rpm / ShooterConstants.kAcceleratorMaxRPM);
  }

  public CANEncoder getFlywheelEncoder() {
    return encoder;
  }

  @Override
  public void periodic() {

    SmartDashboard.putBoolean("BallSensor", getBallSensor());

    if (hoodState == null) {
      setHoodState(HoodState.AUTOLINE_SHOT);
    }

    // Make belts not skip lol
    if (encoder.getVelocity() < ShooterConstants.kLowRPMThreshold) {
      flywheelLeft.setClosedLoopRampRate(ShooterConstants.kLowRPMRampRate);
    } else {
      flywheelLeft.setClosedLoopRampRate(0.0);
    }

    SmartDashboard.putNumber("Shooter current draw", flywheelLeft.getOutputCurrent());

    // Shot detection
    // If our current draw is high
    if (flywheelLeft.getOutputCurrent() >= ShooterConstants.kCurrentDrawnToDetectCompletedShot
        && Robot.m_robotContainer.m_hopper.isFeeding()) {
      // If the timer is not started
      if (currentMonitorTimer.get() == 0) {
        // Start it
        currentMonitorTimer.start();
      }

      // Now that the timer is running
      // If the timer is gone past the required time
      if (currentMonitorTimer.get() >= ShooterConstants.kCurrentDrawnTimeWindow) {
        // Stop and reset the timer, and increment our shot number by 1
        currentMonitorTimer.stop();
        currentMonitorTimer.reset();
        shotsFired++;
      }
    } else { // Our current draw is low, so stop the timer
      currentMonitorTimer.stop();
      currentMonitorTimer.reset();
    }

    SmartDashboard.putNumber("Shots fired", shotsFired);

    // Live PID tuning
    final boolean enableLivePIDTuning = true;
    if (enableLivePIDTuning) {
      double setpointRPM = SmartDashboard.getNumber("Shooter setpoint (RPM)", 0);
      double acceleratorRPM = SmartDashboard.getNumber("Shooter accelerator RPM", 0);

      CANPIDController pidController = flywheelLeft.getPIDController();
      setFlywheelToRPM(setpointRPM);

      SmartDashboard.putNumber("Shooter velocity", encoder.getVelocity());
      SmartDashboard.putNumber("Shooter error", encoder.getVelocity() - setpointRPM);
      setAcceleratorToRPM(acceleratorRPM);
    }
  }

  public boolean isVelocityWithinTargetRange(double setpoint, double targetRange) {
    return setpoint - targetRange <= getFlywheelEncoder().getVelocity()
        && getFlywheelEncoder().getVelocity() <= setpoint + targetRange;
  }

  public int getShotsFired() {
    return shotsFired;
  }

  public void resetShotsFired() {
    shotsFired = 0;
  }

  public void setHoodState(HoodState newState) {
    hoodPiston.set(newState.state);
    hoodState = newState;
  }

  public HoodState getHoodState() {
    return hoodState;
  }

  public boolean isAcceleratorRunning() {
    return accelerator.get() != 0;
  }

  public boolean isFlywheelRunning() {
    return flywheelLeft.get() != 0 && flywheelRight.get() != 0;
  }
}
