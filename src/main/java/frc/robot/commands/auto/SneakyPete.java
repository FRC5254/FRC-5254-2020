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
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.IntakeSetState;
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

  static Move slowingPoint = new Move().forward(80).markAsReference();
  static Trajectory initialDriveForwards =
      new Path(
              AutoHelper.makePose(0, 0, 0),
              List.of(new Move().forward(35).get()),
              slowingPoint.get(0))
          .setMaxSpeedFPS(8)
          .toTrajectory();

  static Move ballPoint = slowingPoint.copy().forward(10).markAsReference();
  static Trajectory slowDriveForwards =
      new Path(slowingPoint.get(0), List.of(slowingPoint.copy().forward(5).get()), ballPoint.get(0))
          .setMaxSpeedFPS(3)
          .toTrajectory();

  static Move shotPoint = ballPoint.copy().left(100).backward(100).markAsReference();
  static Trajectory goToShootPosition =
      new Path(
              ballPoint.get(0),
              List.of(ballPoint.copy().left(35).backward(60).get()),
              shotPoint.get(-180 - 45))
          .setMaxSpeedFPS(9)
          .setMaxAccelFPS(2)
          .toTrajectory();

  public SneakyPete(
      Drivetrain drivetrain, Intake intake, Shooter shooter, Hopper hopper, Limelight limelight) {
    super(
        new ParallelCommandGroup(
            new IntakeSetState(intake, IntakeState.EXTENDED),
            new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
            new SequentialCommandGroup(
                AutoHelper.driveTrajectory(initialDriveForwards, drivetrain),
                AutoHelper.driveTrajectoryAndStop(slowDriveForwards, drivetrain))),
        new WaitCommand(0.25),
        AutoHelper.driveTrajectoryAndStop(goToShootPosition, drivetrain),
        new PrepRobotForFeed(
            drivetrain,
            intake,
            shooter,
            ShooterConstants.kAcceleratorRPMAutoLine,
            limelight,
            ShooterConstants.kAutoLineRPM,
            HoodState.AUTOLINE_SHOT),
        new FeedSpunUpShooter(hopper, intake, () -> false, 5));
  }
}
