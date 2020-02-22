/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.LocationConstants;
import frc.robot.Constants.LocationConstants.StartingLocations;
import frc.robot.commands.auto.AutoHelper.OffsetConfig;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class WallShotAuto extends SequentialCommandGroup {
  /** Creates a new WallShotAuto. */
  public WallShotAuto(Drivetrain drivetrain, Intake intake) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
        new ParallelCommandGroup(
            // new IntakeSetState(intake, IntakeState.EXTENDED),
            AutoHelper.createStandardPath(
                drivetrain,
                false,
                Units.feetToMeters(4),
                new Pose2d(
                    AutoHelper.getStartingPoint(
                        StartingLocations.kInFrontOfGoal,
                        OffsetConfig.OFFSET_TOWARDS_GOAL,
                        LocationConstants.kStandardOffsetDistance),
                    Rotation2d.fromDegrees(180)),
                new Pose2d(
                    AutoHelper.getStartingPoint(
                        StartingLocations.kWallShot,
                        OffsetConfig.OFFSET_AWAY_FROM_GOAL,
                        LocationConstants.kStandardOffsetDistance),
                    Rotation2d.fromDegrees(180)),
                new Translation2d(60, 94.655))));
  }
}
