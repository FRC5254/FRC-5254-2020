package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  private CANSparkMax flywheel1, flywheel2, accelerator;
  private CANEncoder encoder;

  public Shooter() {
    flywheel1 = new CANSparkMax(4, MotorType.kBrushless);
    flywheel2 = new CANSparkMax(5, MotorType.kBrushless);
    accelerator = new CANSparkMax(6, MotorType.kBrushless);
    encoder = flywheel1.getEncoder();

    flywheel2.follow(flywheel1);
  }

  public void setFlywheelToRPM(double rpm) {
    flywheel1.getPIDController().setReference(rpm, ControlType.kVelocity);
  }

  public void setAccelerator(boolean on) {
    accelerator.set(on ? 1.0 : 0.0);
  }

  public CANEncoder getFlywheelEncoder() {
    return encoder;
  }

  @Override
  public void periodic() {}
}
