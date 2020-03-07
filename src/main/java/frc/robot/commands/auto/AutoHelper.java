package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;

public class AutoHelper {

  public static Command driveTrajectoryAndStop(Trajectory trajectory, Drivetrain dt) {
    return new SequentialCommandGroup(
        driveTrajectory(trajectory, dt),
        new InstantCommand(
            () -> {
              dt.tankDriveVolts(0, 0);
            },
            dt));
  }

  public static Command driveTrajectory(Trajectory trajectory, Drivetrain dt) {
    return new SequentialCommandGroup(
        new InstantCommand(
            () -> {
              dt.resetOdometry(trajectory.getInitialPose());
            }),
        new RamseteCommand(
            trajectory,
            dt::getPose,
            new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
            new SimpleMotorFeedforward(
                DriveConstants.ksVolts,
                DriveConstants.kvVoltSecondsPerMeter,
                DriveConstants.kaVoltSecondsSquaredPerMeter),
            DriveConstants.kDriveKinematics,
            dt::getWheelSpeeds,
            new PIDController(DriveConstants.kPDriveVel, 0, 0),
            new PIDController(DriveConstants.kPDriveVel, 0, 0),
            dt::tankDriveVolts,
            dt));
  }

  public static Pose2d makePose(double x, double y, double degrees) {
    return new Pose2d(x, y, Rotation2d.fromDegrees(degrees));
  }
}
