package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import java.util.List;

public class AutoHelper {
  public static Command createStandardPath(
      Drivetrain robotDrive, Pose2d startingPose, Pose2d endingPose, Translation2d... innerPoints) {
    // Create a voltage constraint to ensure we don't accelerate too fast
    var autoVoltageConstraint =
        new DifferentialDriveVoltageConstraint(
            new SimpleMotorFeedforward(
                DriveConstants.ksVolts,
                DriveConstants.kvVoltSecondsPerMeter,
                DriveConstants.kaVoltSecondsSquaredPerMeter),
            DriveConstants.kDriveKinematics,
            AutoConstants.kAutoMaxDriveVoltage);

    // Create config for trajectory
    TrajectoryConfig config =
        new TrajectoryConfig(
                AutoConstants.kMaxSpeedMetersPerSecond,
                AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(DriveConstants.kDriveKinematics);
    // Apply the voltage constraint
    // .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow.  All units in meters.
    Trajectory exampleTrajectory =
        TrajectoryGenerator.generateTrajectory(
            startingPose, List.of(innerPoints), endingPose, config);

    RamseteCommand ramseteCommand =
        new RamseteCommand(
            exampleTrajectory,
            robotDrive::getPose,
            new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
            new SimpleMotorFeedforward(
                DriveConstants.ksVolts,
                DriveConstants.kvVoltSecondsPerMeter,
                DriveConstants.kaVoltSecondsSquaredPerMeter),
            DriveConstants.kDriveKinematics,
            robotDrive::getWheelSpeeds,
            new PIDController(DriveConstants.kPDriveVel, 0, 0),
            new PIDController(DriveConstants.kPDriveVel, 0, 0),
            // RamseteCommand passes volts to the callback
            robotDrive::tankDriveVolts,
            robotDrive);

    return ramseteCommand;
  }

  public enum OffsetConfig {
    OFFSET_TOWARDS_GOAL,
    OFFSET_AWAY_FROM_GOAL
  }

  public static Translation2d getStartingPoint(
      Translation2d startingPoint, OffsetConfig offsetConfig, double offsetDistance) {
    if (offsetConfig == OffsetConfig.OFFSET_TOWARDS_GOAL) {
      return startingPoint.minus(new Translation2d(offsetDistance, 0));
    } else if (offsetConfig == OffsetConfig.OFFSET_AWAY_FROM_GOAL) {
      return startingPoint.plus(new Translation2d(offsetDistance, 0));
    } else {
      SmartDashboard.putString("ERROR", "Incorrect offset configuration given");
      return startingPoint;
    }
  }
}
