package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.util.Units;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import java.util.List;

public class Path {
  private Pose2d start, end;
  private List<Translation2d> middle;
  private boolean reversed;
  private double maxSpeed, maxAcceleration;

  public Path(
      Pose2d start,
      List<Translation2d> middle,
      Pose2d end,
      boolean reversed,
      double maxSpeed,
      double maxAcceleration) {
    this.start = start;
    this.end = end;
    this.middle = middle;
    this.reversed = reversed;
    this.maxSpeed = maxSpeed;
    this.maxAcceleration = maxAcceleration;
  }

  public Path(Pose2d start, List<Translation2d> middle, Pose2d end) {
    this(
        start,
        middle,
        end,
        false,
        AutoConstants.kMaxSpeedMetersPerSecond,
        AutoConstants.kMaxAccelerationMetersPerSecondSquared);
  }

  public Path setMaxSpeedFPS(double maxSpeed) {
    this.maxSpeed = Units.feetToMeters(maxSpeed);
    return this;
  }

  public Path setMaxAccelFPS(double maxAcceleration) {
    this.maxAcceleration = Units.feetToMeters(maxAcceleration);
    return this;
  }

  public Path setReversed(boolean reversed) {
    this.reversed = reversed;
    return this;
  }

  public Trajectory toTrajectory() {
    return TrajectoryGenerator.generateTrajectory(
        start,
        middle,
        end,
        new TrajectoryConfig(maxSpeed, maxAcceleration)
            .setKinematics(DriveConstants.kDriveKinematics)
            .addConstraint(
                new DifferentialDriveVoltageConstraint(
                    new SimpleMotorFeedforward(
                        DriveConstants.ksVolts,
                        DriveConstants.kvVoltSecondsPerMeter,
                        DriveConstants.kaVoltSecondsSquaredPerMeter),
                    DriveConstants.kDriveKinematics,
                    AutoConstants.kAutoMaxDriveVoltage))
            .setReversed(reversed));
  }
}
