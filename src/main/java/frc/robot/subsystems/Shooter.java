package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RobotMap;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  private CANSparkMax flywheel1, flywheel2, accelerator;
  private CANEncoder encoder;

  public Shooter() {
    flywheel1 = new CANSparkMax(RobotMap.kFlywheelMotor1, MotorType.kBrushless);
    flywheel2 = new CANSparkMax(RobotMap.kFlywheelMotor2, MotorType.kBrushless);
    accelerator = new CANSparkMax(RobotMap.kAcceleratorMotor, MotorType.kBrushed);
    encoder = flywheel1.getEncoder();

    flywheel2.follow(flywheel1);

    flywheel1.getEncoder().setPositionConversionFactor(1.0 / ShooterConstants.kFlywheelGearRatio);
    flywheel1
        .getEncoder()
        .setVelocityConversionFactor(1.0 / (60 * ShooterConstants.kFlywheelGearRatio));
    // accelerator
    //     .getEncoder()
    //     .setPositionConversionFactor(1.0 / ShooterConstants.kAcceleratorGearRatio);
    // accelerator
    //     .getEncoder()
    //     .setVelocityConversionFactor(1.0 / (60 * ShooterConstants.kAcceleratorGearRatio));

    flywheel1.setIdleMode(IdleMode.kCoast);
    flywheel2.setIdleMode(IdleMode.kCoast);
    accelerator.setIdleMode(IdleMode.kBrake);

    flywheel1.setSmartCurrentLimit(ShooterConstants.kFlywheelCurrentLimit);
    accelerator.setSmartCurrentLimit(ShooterConstants.kAcceleratorCurrentLimit);
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
    double kp = SmartDashboard.getNumber("Shooter kP", 0);
    double kf = SmartDashboard.getNumber("Shooter kF", 0);
    double maxOutput = SmartDashboard.getNumber("Shooter max output", 0);
    double setpointRPM = SmartDashboard.getNumber("Shooter setpoint (RPM)", 0);

    CANPIDController pidController = flywheel1.getPIDController();
    pidController.setP(kp);
    pidController.setFF(kf);
    pidController.setOutputRange(-maxOutput, maxOutput);
    pidController.setReference(setpointRPM, ControlType.kVelocity);

    SmartDashboard.putNumber("Shooter velocity", encoder.getVelocity());
    SmartDashboard.putNumber("Shooter error", setpointRPM - encoder.getVelocity());
  }
}
