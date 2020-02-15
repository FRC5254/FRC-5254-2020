package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RobotMap;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  public enum HoodState {
    WALL_SHOT(true),
    TRENCH_SHOT(false);

    private boolean value;

    private HoodState(boolean value) {
      this.value = value;
    }
  }

  private CANSparkMax flywheel1, flywheel2, accelerator;
  private CANEncoder encoder;
  private int shotsFired;
  private Timer currentMonitorTimer;
  private Solenoid hoodPiston;
  public HoodState hoodState;

  public Shooter() {
    shotsFired = 0;
    currentMonitorTimer = new Timer();

    flywheel1 = new CANSparkMax(RobotMap.kFlywheelMotorLeft, MotorType.kBrushless);
    flywheel2 = new CANSparkMax(RobotMap.kFlywheelMotorRight, MotorType.kBrushless);
    accelerator = new CANSparkMax(RobotMap.kAcceleratorMotor, MotorType.kBrushless);

    hoodPiston = new Solenoid(RobotMap.kHoodSolenoid);
    hoodState = null;

    flywheel1.restoreFactoryDefaults();
    flywheel2.restoreFactoryDefaults();
    accelerator.restoreFactoryDefaults();

    encoder = flywheel1.getEncoder();

    flywheel1.setInverted(true);
    flywheel2.follow(flywheel1, true);

    flywheel1.getEncoder().setVelocityConversionFactor(1.0);
    flywheel1.getEncoder().setPositionConversionFactor(1.0 / ShooterConstants.kFlywheelGearRatio);
    // flywheel1
    //     .getEncoder()
    //     .setVelocityConversionFactor(1.0 / (60 * ShooterConstants.kFlywheelGearRatio));
    // accelerator
    //     .getEncoder()
    //     .setPositionConversionFactor(1.0 / ShooterConstants.kAcceleratorGearRatio);
    // accelerator
    //     .getEncoder()
    //     .setVelocityConversionFactor(1.0 / (60 * ShooterConstants.kAcceleratorGearRatio));

    flywheel1.setIdleMode(IdleMode.kCoast);
    flywheel2.setIdleMode(IdleMode.kCoast);
    accelerator.setIdleMode(IdleMode.kCoast);

    flywheel1.setSmartCurrentLimit(ShooterConstants.kFlywheelCurrentLimit);
    accelerator.setSmartCurrentLimit(ShooterConstants.kAcceleratorCurrentLimit);

    SmartDashboard.putNumber("Shooter kP", 0.000050);
    SmartDashboard.putNumber("Shooter kF", 0.000165);
    SmartDashboard.putNumber("Shooter max output", 0);
    SmartDashboard.putNumber("Shooter setpoint (RPM)", 0);
    SmartDashboard.putNumber("Shooter accelerator RPM", 0);
  }

  public void setFlywheelToRPM(double rpm) {
    flywheel1.getPIDController().setReference(rpm, ControlType.kVelocity);
  }

  public void setAcceleratorToRPM(double rpm) {
    accelerator.set(rpm / ShooterConstants.kAcceleratorMaxRPM);
  }

  public CANEncoder getFlywheelEncoder() {
    return encoder;
  }

  @Override
  public void periodic() {
    if (hoodState == null) {
      setHoodState(HoodState.TRENCH_SHOT);
    }

    // Shot detection
    // If our current draw is high
    if (flywheel1.getOutputCurrent() >= ShooterConstants.kCurrentDrawnToDetectCompletedShot) {
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
    }

    // Live PID tuning
    double kp = SmartDashboard.getNumber("Shooter kP", 0);
    double kf = SmartDashboard.getNumber("Shooter kF", 0);
    double maxOutput = SmartDashboard.getNumber("Shooter max output", 0);
    double setpointRPM = SmartDashboard.getNumber("Shooter setpoint (RPM)", 0);
    double acceleratorRPM = SmartDashboard.getNumber("Shooter accelerator RPM", 0);

    CANPIDController pidController = flywheel1.getPIDController();
    pidController.setP(kp);
    pidController.setFF(kf);
    pidController.setOutputRange(-maxOutput, maxOutput);
    pidController.setReference(setpointRPM, ControlType.kVelocity);

    SmartDashboard.putNumber("Shooter velocity", encoder.getVelocity());
    SmartDashboard.putNumber("Shooter error", encoder.getVelocity() - setpointRPM);
    setAcceleratorToRPM(acceleratorRPM);
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
    hoodPiston.set(newState.value);
    hoodState = newState;
  }

  public HoodState getHoodState() {
    return hoodState;
  }
}
