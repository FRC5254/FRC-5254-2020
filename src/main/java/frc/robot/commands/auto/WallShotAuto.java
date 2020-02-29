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
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.HopperSetSpeed;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.IntakeSetState;
import frc.robot.commands.ShooterSetAcceleratorSpeed;
import frc.robot.commands.ShooterSetHoodState;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.commands.groups.FeedSpunUpShooter;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class WallShotAuto extends SequentialCommandGroup {
  /** Creates a new WallShotAuto. */
  public WallShotAuto(
      Drivetrain drivetrain, Intake intake, Shooter shooter, Hopper hopper, double offsetTime) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    // super(
    // new ParallelCommandGroup(
    //     new IntakeSetState(intake, IntakeState.EXTENDED),
    //     AutoHelper.createStandardPath(
    //         drivetrain,
    //         false,
    //         Units.feetToMeters(4),
    //         new Pose2d(
    //             AutoHelper.getStartingPoint(
    //                 StartingLocations.kInFrontOfGoal,
    //                 OffsetConfig.OFFSET_TOWARDS_GOAL,
    //                 LocationConstants.kStandardOffsetDistance),
    //             Rotation2d.fromDegrees(0)),

    //         new Pose2d(
    //             AutoHelper.getStartingPoint(
    //                 StartingLocations.kWallShot,
    //                 OffsetConfig.OFFSET_AWAY_FROM_GOAL,
    //                 LocationConstants.kStandardOffsetDistance),
    //             Rotation2d.fromDegrees(0)),
    //         new Translation2d(Units.inchesToMeters(80), Units.inchesToMeters(94.65))),
    //         new InstantCommand(() -> {
    //             SmartDashboard.putString("ending spot", new Pose2d(
    //                 AutoHelper.getStartingPoint(
    //                     StartingLocations.kWallShot,
    //                     OffsetConfig.OFFSET_AWAY_FROM_GOAL,
    //                     LocationConstants.kStandardOffsetDistance),
    //                 Rotation2d.fromDegrees(0)).toString());
    //         }),
    //         new InstantCommand(() -> {
    //             SmartDashboard.putString("starting spot", new Pose2d(
    //                 AutoHelper.getStartingPoint(
    //                     StartingLocations.kInFrontOfGoal,
    //                     OffsetConfig.OFFSET_TOWARDS_GOAL,
    //                     LocationConstants.kStandardOffsetDistance),
    //                 Rotation2d.fromDegrees(0)).toString());
    //         })

    //         ));

    super(
        new ParallelCommandGroup(
            new IntakeSetState(intake, IntakeState.EXTENDED),
            new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
            AutoHelper.createStandardPath(
                drivetrain,
                false,
                Units.feetToMeters(7),
                new Pose2d(0, 0, new Rotation2d(0)),
                new Pose2d(Units.inchesToMeters(72.5), 0, new Rotation2d(0)),
                new Translation2d(Units.inchesToMeters(35), 0)),
            new ShooterSetHoodState(shooter, HoodState.WALL_SHOT),
            new ShooterSetSpeed(shooter, ShooterConstants.kWallShotRPM),
            new ShooterSetAcceleratorSpeed(shooter, ShooterConstants.kAcceleratorRPMWall)),
        new WaitCommand(offsetTime),
        new FeedSpunUpShooter(
            hopper,
            () -> shooter.getShotsFired() > 100000,
            3), // Wren: --need to reset shotsFired - set big num for now-- why do we want the
                // intake intaking?
        new HopperSetSpeed(hopper, 0, 0),
        new ShooterSetSpeed(shooter, 0),
        new ShooterSetAcceleratorSpeed(shooter, 0),
        new IntakeSetRollers(intake, 0));
  }
}
