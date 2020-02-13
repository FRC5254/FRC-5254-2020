/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DrivetrainAlignToGoal;
import frc.robot.commands.IntakeSetExtended;
import frc.robot.commands.ShooterSetHoodState;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;
import java.util.Map;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class FullShotSequence extends SequentialCommandGroup {
  /** Creates a new FullShotSequence. */
  public FullShotSequence(
      Drivetrain drivetrain,
      Hopper hopper,
      Intake intake,
      Shooter shooter,
      double shooterRPM,
      HoodState hoodState) {
    addCommands(
        new ShooterSetHoodState(shooter, hoodState),
        new ParallelCommandGroup(
            new SelectCommand(
                Map.ofEntries(
                    Map.entry(false, new IntakeSetExtended(intake)),
                    Map.entry(true, new InstantCommand())),
                () -> intake == null),
            new DrivetrainAlignToGoal(drivetrain),
            new ShooterSetSpeed(shooter, shooterRPM, true)));
  }

  public FullShotSequence(
      Drivetrain drivetrain,
      Hopper hopper,
      Shooter shooter,
      double shooterRPM,
      HoodState hoodState) {
    this(drivetrain, hopper, null, shooter, shooterRPM, hoodState);
  }
}
