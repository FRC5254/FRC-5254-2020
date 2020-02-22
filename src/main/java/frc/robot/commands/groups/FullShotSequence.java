/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;
import java.util.function.BooleanSupplier;

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
      Limelight limelight,
      double shooterRPM,
      HoodState hoodState,
      BooleanSupplier endCondition,
      double timeout) {
    addCommands(
        new PrepRobotForFeed(drivetrain, intake, shooter, limelight, shooterRPM, hoodState),
        new FeedSpunUpShooter(hopper, intake, shooter, endCondition, timeout));
  }
}
