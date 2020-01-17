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

  private final Shooter shooter;
  private final double rpm;

  /** Creates a new ShooterSetSpeed. */
  public ShooterSetSpeed(Shooter shooter, double rpm) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.rpm = rpm;

    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setFlywheelToRPM(rpm);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double rpmError = shooter.getFlywheelEncoder().getVelocity() - rpm;
    return -ShooterConstants.kAcceptableRPMRange <= rpmError
        && rpmError <= ShooterConstants.kAcceptableRPMRange;
  }
}
