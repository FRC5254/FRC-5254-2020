/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;

public class ControllerVibrate extends WaitCommand {
  public enum ControllerChoice {
    DRIVER,
    OPERATOR,
    BOTH;
  };

  private ControllerChoice choice;
  private double intensity;

  /** Creates a new ControllerVibrate. */
  public ControllerVibrate(double seconds, double intensity, ControllerChoice choice) {
    // Use addRequirements() here to declare subsystem dependencies.
    super(seconds);
    this.choice = choice;
    this.intensity = intensity;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (choice == ControllerChoice.DRIVER || choice == ControllerChoice.BOTH) {
      Robot.m_robotContainer.driverController.setRumble(RumbleType.kLeftRumble, intensity);
      Robot.m_robotContainer.driverController.setRumble(RumbleType.kRightRumble, intensity);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.m_robotContainer.driverController.setRumble(RumbleType.kLeftRumble, 0);
    Robot.m_robotContainer.driverController.setRumble(RumbleType.kRightRumble, 0);
  }
}
