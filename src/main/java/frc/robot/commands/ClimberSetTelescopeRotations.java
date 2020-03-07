/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ClimberConstants;
import frc.robot.subsystems.Climber;

public class ClimberSetTelescopeRotations extends CommandBase {
  
  public final Climber m_climber;
  public double rotations;

  public ClimberSetTelescopeRotations(Climber climber, double rotations) {
    m_climber = climber;
    this.rotations = rotations;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climber.setTelescopeRotations(rotations);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.setTelescopeSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return ((ClimberConstants.kMaxHeightRotations - 15) < m_climber.getPosition()) && ((m_climber.getPosition() < ClimberConstants.kMaxHeightRotations + 15));
  }
}
