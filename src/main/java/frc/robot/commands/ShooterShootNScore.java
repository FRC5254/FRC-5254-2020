/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ShooterShootNScore extends SequentialCommandGroup {
  private final Shooter shooter;

  /** Creates a new ShooterShootNScore. */
  public ShooterShootNScore(Shooter shooter) {
    super(
        new ShooterSetSpeed(shooter, ShooterConstants.kWallShotRPM),
        new InstantCommand(
            () -> shooter.setAcceleratorToRPM(ShooterConstants.kAcceleratorRPM), shooter));

    this.shooter = shooter;
  }

  @Override
  public void end(boolean interrupted) {
    shooter.setFlywheelToRPM(0);
    shooter.setAcceleratorToRPM(0);
  }
}
