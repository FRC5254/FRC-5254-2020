/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.HopperSetSpeed;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.IntakeSetState;
import frc.robot.commands.ShooterSetAcceleratorSpeed;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.commands.groups.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;
import java.util.List;

public class SneakyPete extends SequentialCommandGroup {

  static Move slowingPoint = new Move().forward(89).markAsReference();
  static Trajectory initialDriveForwards =
      new Path(
              AutoHelper.makePose(0, 0, 0),
              List.of(new Move().forward(35).get()),
              slowingPoint.get(0))
          .setMaxSpeedFPS(14)
          .toTrajectory();

  static Move rightBallPoint = slowingPoint.copy().forward(6).markAsReference();
  static Trajectory slowDriveForwards =
      new Path(
              slowingPoint.get(0),
              List.of(slowingPoint.copy().forward(5).get()),
              rightBallPoint.get(0))
          .setMaxSpeedFPS(8)
          .toTrajectory();

  static Move firstReversalPoint = rightBallPoint.copy().backward(40).markAsReference();
  static Trajectory backup =
      new Path(
              rightBallPoint.get(0),
              List.of(slowingPoint.copy().backward(8).get()),
              firstReversalPoint.get(0))
          .setMaxSpeedFPS(14)
          .setReversed(true)
          .toTrajectory();

  static Move leftBallPoint = rightBallPoint.copy().left(15).forward(3).markAsReference();
  static Trajectory intakeLeftBall =
      new Path(
              firstReversalPoint.get(0),
              List.of(firstReversalPoint.copy().forward(17).left(2).get()),
              leftBallPoint.get(10))
          .setMaxSpeedFPS(8)
          .toTrajectory();

  static Move secondReversalPoint = leftBallPoint.copy().backward(47).markAsReference();
  static Trajectory secondBackup =
      new Path(
              leftBallPoint.get(10),
              List.of(leftBallPoint.copy().backward(25).get()),
              secondReversalPoint.get(10))
          .setMaxSpeedFPS(10)
          .setReversed(true)
          .toTrajectory();

  static Move shotPoint = leftBallPoint.copy().left(150).backward(90).markAsReference();
  static Trajectory goToShootPosition =
      new Path(
              firstReversalPoint.get(0),
              List.of(leftBallPoint.copy().left(75).backward(40).get()),
              shotPoint.get(-180 - 25))
          .setMaxSpeedFPS(14)
          .toTrajectory();

  public SneakyPete(
      Drivetrain drivetrain, Intake intake, Shooter shooter, Hopper hopper, Limelight limelight) {
    super(
        new ParallelCommandGroup(
            new IntakeSetState(intake, IntakeState.EXTENDED),
            new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
            new SequentialCommandGroup(
                AutoHelper.driveTrajectoryAndStop(initialDriveForwards, drivetrain),
                AutoHelper.driveTrajectoryAndStop(slowDriveForwards, drivetrain))),
        new WaitCommand(0.05),
        new IntakeSetRollers(intake, 0),
        AutoHelper.driveTrajectoryAndStop(backup, drivetrain),
        new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
        AutoHelper.driveTrajectoryAndStop(intakeLeftBall, drivetrain),
        new WaitCommand(0.05),
        AutoHelper.driveTrajectoryAndStop(secondBackup, drivetrain),
        new IntakeSetRollers(intake, 0),
        AutoHelper.driveTrajectoryAndStop(goToShootPosition, drivetrain),
        new PrepRobotForFeed(
            drivetrain,
            intake,
            shooter,
            ShooterConstants.kAcceleratorRPMAutoLine,
            limelight,
            ShooterConstants.kAutoLineRPM,
            HoodState.AUTOLINE_SHOT),
        new FeedSpunUpShooter(hopper, intake, () -> false, 5),
        new HopperSetSpeed(hopper, 0, 0),
        new ShooterSetSpeed(shooter, 0),
        new ShooterSetAcceleratorSpeed(shooter, 0),
        new IntakeSetRollers(intake, 0));
  }
}
