/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Shooter;

public class ShooterSetSpeed extends CommandBase {
  /** Creates a new ShooterSetSpeed. */
  private final Shooter m_shooter;

  private double rpm;
  private boolean waitUntilAtSpeed;

  public ShooterSetSpeed(Shooter shooter, double rpm) {
    // Use addRequirements() here to declare subsystem dependencies.
    this(shooter, rpm, false);
  }

  public ShooterSetSpeed(Shooter shooter, double rpm, boolean waitUntilSpunUp) {
    m_shooter = shooter;
    this.rpm = rpm;
    waitUntilAtSpeed = waitUntilSpunUp;
    addRequirements(m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooter.setFlywheelToRPM(rpm);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (waitUntilAtSpeed) {
      return m_shooter.isVelocityWithinTargetRange(rpm, ShooterConstants.kAcceptableRPMRange);
    } else {
      return true;
    }
  }
}
