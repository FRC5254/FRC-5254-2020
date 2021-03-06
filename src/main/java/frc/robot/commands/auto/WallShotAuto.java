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
import frc.robot.commands.groups.FeedSpunUpShooter;
import frc.robot.commands.groups.PrepRobotForFeed;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;
import java.util.List;

public class WallShotAuto extends SequentialCommandGroup {
  public static Trajectory trajectory =
      new Path(
              AutoHelper.makePose(0, 0, 0),
              List.of(new Move().forward(35).get()),
              new Move().forward(75).get(0))
          .setMaxSpeedFPS(8)
          .toTrajectory();

  public WallShotAuto(
      Drivetrain drivetrain,
      Intake intake,
      Shooter shooter,
      Hopper hopper,
      Limelight limelight,
      double offsetTime) {
    super(
        new ParallelCommandGroup(
            new IntakeSetState(intake, IntakeState.EXTENDED),
            new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
            AutoHelper.driveTrajectoryAndStop(trajectory, drivetrain),
            new PrepRobotForFeed(
                null,
                shooter,
                ShooterConstants.kAcceleratorRPMWall,
                limelight,
                ShooterConstants.kWallShotRPM,
                HoodState.WALL_SHOT)),
        new WaitCommand(offsetTime),
        new FeedSpunUpShooter(hopper, intake, shooter, () -> false, 3.0),
        new ParallelCommandGroup(
            new HopperSetSpeed(hopper, shooter, 0, 0),
            new ShooterSetSpeed(shooter, 0),
            new IntakeSetRollers(intake, 0),
            new ShooterSetAcceleratorSpeed(shooter, 0)));
  }
}
