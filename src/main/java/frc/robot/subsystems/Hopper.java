package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RobotMap;

public class Hopper extends SubsystemBase {

  private final CANSparkMax leftBelt;
  private final CANSparkMax rightBelt;

  public Hopper() {

    leftBelt = new CANSparkMax(RobotMap.kLeftBelt, MotorType.kBrushless);
    rightBelt = new CANSparkMax(RobotMap.kRightBelt, MotorType.kBrushless);

    leftBelt.restoreFactoryDefaults();
    rightBelt.restoreFactoryDefaults();

    // These are here in case we need to change them - maybe they aren't necessary?
    leftBelt.setInverted(false);
    rightBelt.setInverted(true);

    leftBelt.setIdleMode(IdleMode.kBrake);
    rightBelt.setIdleMode(IdleMode.kBrake);
  }

  public void setHopper(double leftSpeed, double rightSpeed) {
    leftBelt.set(leftSpeed);
    rightBelt.set(rightSpeed);
  }
}
