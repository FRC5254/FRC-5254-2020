/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.RobotMap;

public class Climber extends SubsystemBase {

  private final CANSparkMax telescope;
  private final CANSparkMax winch;

  public Climber() {

    telescope = new CANSparkMax(RobotMap.kTelescope, MotorType.kBrushed);
    winch = new CANSparkMax(RobotMap.kWinch, MotorType.kBrushed);

    telescope.restoreFactoryDefaults();
    winch.restoreFactoryDefaults();

    telescope.setIdleMode(IdleMode.kBrake);
    winch.setIdleMode(IdleMode.kBrake);

    telescope.setSmartCurrentLimit(ClimberConstants.kTelescopeCurrentLimit);
    winch.setSmartCurrentLimit(ClimberConstants.kWinchCurrentLimit);
  }

  public void setTelescopeSpeed(double speed) {
    telescope.set(speed);
  }

  public void setWinchSpeed(double speed) {
    winch.set(speed);
  }
}
