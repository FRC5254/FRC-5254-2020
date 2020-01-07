package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  private CANSparkMax left1, left2, right1, right2;

  public Shooter() {
    left1 = new CANSparkMax(4, MotorType.kBrushless);
    left2 = new CANSparkMax(5, MotorType.kBrushless);
    right1 = new CANSparkMax(6, MotorType.kBrushless);
    right2 = new CANSparkMax(7, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    // Implement drive code here
  }
}
