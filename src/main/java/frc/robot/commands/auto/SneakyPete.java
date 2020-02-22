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
import frc.robot.Constants.IntakeConstants;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.IntakeSetState;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class SneakyPete extends SequentialCommandGroup {
  /** Creates a new SneakyPete. */
  public SneakyPete(Drivetrain drivetrain, Intake intake) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
        new ParallelCommandGroup(
            new IntakeSetState(intake, IntakeState.EXTENDED),
            new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
            AutoHelper.createStandardPath(
                drivetrain,
                false,
                Units.feetToMeters(3),
                new Pose2d(0, 0, new Rotation2d(0)),
                new Pose2d(Units.inchesToMeters(125 - 12), 0, new Rotation2d(0)),
                new Translation2d(Units.inchesToMeters(100), 0))),
        AutoHelper.createStandardPath(
            drivetrain,
            true,
            Units.feetToMeters(9),
            new Pose2d(Units.inchesToMeters(125), 0, new Rotation2d(0)),
            new Pose2d(
                -Units.inchesToMeters(135 - 125), Units.inchesToMeters(200), new Rotation2d(-135)),
            new Translation2d(-Units.inchesToMeters(150 - 125), Units.inchesToMeters(100))));
    // new ParallelCommandGroup(
    //     new IntakeSetState(intake, IntakeState.EXTENDED),
    //     new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
    //     AutoHelper.createStandardPath(
    //         drivetrain,
    //         false,
    //         Units.feetToMeters(3),
    //         new Pose2d(
    //             AutoHelper.getStartingPoint(
    //                 StartingLocations.kSneakyPete,
    //                 OffsetConfig.OFFSET_AWAY_FROM_GOAL,
    //                 LocationConstants.kStandardOffsetDistance),
    //             Rotation2d.fromDegrees(0)),
    //         new Pose2d(
    //             AutoHelper.getStartingPoint(
    //                 StartingLocations.kYoinkOpponentTrench,
    //                 OffsetConfig.OFFSET_AWAY_FROM_GOAL,
    //                 LocationConstants.kStandardOffsetDistance),
    //             Rotation2d.fromDegrees(0)),
    //         new Translation2d(122, StartingLocations.kYoinkOpponentTrench.getY()))),
    // AutoHelper.createStandardPath(
    //     drivetrain,
    //     true,
    //     Units.feetToMeters(9),
    //     new Pose2d(
    //         AutoHelper.getStartingPoint(
    //             StartingLocations.kYoinkOpponentTrench,
    //             OffsetConfig.OFFSET_AWAY_FROM_GOAL,
    //             LocationConstants.kStandardOffsetDistance),
    //         Rotation2d.fromDegrees(0)),
    //     new Pose2d(
    //         AutoHelper.getStartingPoint(
    //             StartingLocations.kInFrontOfGoal,
    //             OffsetConfig.OFFSET_TOWARDS_GOAL,
    //             LocationConstants.kStandardOffsetDistance),
    //         Rotation2d.fromDegrees(180)),
    //     StartingLocations.kYoinkOpponentTrench));
  }
}
