package frc.robot;

import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.util.Units;
import frc.robot.Limelight.Pipeline;

public final class Constants {
  public static final class RobotMap {
    // Drivetrain
    public static int kDriveMotorLeft1 = 1;
    public static int kDriveMotorLeft2 = 2;
    public static int kDriveMotorRight1 = 3;
    public static int kDriveMotorRight2 = 4;

    // Shooter
    public static int kFlywheelMotorLeft = 8;
    public static int kFlywheelMotorRight = 9;
    public static int kAcceleratorMotor = 10;
    public static int kHoodDoubleSolenoidFront = 2; // Towards piston head
    public static int kHoodDoubleSolenoidBack = 3;

    // Intake
    public static int kIntakeMotor = 5;
    public static int kIntakeDoubleSolenoidFront = 0; // Towards piston head
    public static int kIntakeDoubleSolenoidBack = 1;

    // Hopper
    public static int kLeftBelt = 6;
    public static int kRightBelt = 7;
  }

  public static final class DriveConstants {
    // Physical details
    public static final double kTrackwidthMeters = Units.inchesToMeters(24);
    public static final DifferentialDriveKinematics kDriveKinematics =
        new DifferentialDriveKinematics(kTrackwidthMeters);
    public static final double kWheelDiameter = 6.0;
    public static final double kEncoderPulsePerRev = 42.0;
    public static final double kGearRatio = (50.0 / 11.0) * (50.0 / 24.0);
    public static final double kDistancePerPulse =
        (1.0 / kGearRatio) * Units.inchesToMeters(6.0) * Math.PI;

    // These are example values only - DO NOT USE THESE FOR YOUR OWN ROBOT!
    // These characterization values MUST be determined either experimentally or theoretically
    // for *your* robot's drive.
    // The Robot Characterization Toolsuite provides a convenient tool for obtaining these
    // values for your robot.
    public static final double ksVolts = 1.25;
    public static final double kvVoltSecondsPerMeter = 2.52;
    public static final double kaVoltSecondsSquaredPerMeter = 0.322;

    // Example value only - as above, this must be tuned for your drive!
    public static final double kPDriveVel = 1.6;

    // Motor config
    public static final int kCurrentLimit = 60;
    public static final double kRampRate = 0.0;
    public static final double kJoystickTurnDeadzone = 0.15;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 2;
    public static final double kMaxAccelerationMetersPerSecondSquared = .5;
    public static final double kAutoMaxDriveVoltage = 10;

    // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
  }

  public static final class ShooterConstants {
    // Physical details
    public static final double kFlywheelGearRatio = 1.0;
    public static final double kFlywheelMaxRPM = 5_880 / kFlywheelGearRatio;
    public static final double kAcceleratorGearRatio = 6.0;
    public static final double kAcceleratorMaxRPM = 11_000 / kAcceleratorGearRatio;

    // Shot details
    public static final double kWallShotRPM = 3_800;
    public static final double kTrenchShotRPM = 5_400;
    public static final double kAutoLineRPM = 5_000;
    public static final double kAcceptableRPMRange = 50;
    public static final double kAcceleratorRPM = 6_000;
    public static final double kCurrentDrawnToDetectCompletedShot = 15;
    public static final double kCurrentDrawnTimeWindow = 0.1;
    public static final double kLowRPMThreshold = 1_000;
    public static final double kLowRPMRampRate = 0.05;

    // Motor config
    public static final int kFlywheelCurrentLimit = 20;
    public static final int kAcceleratorCurrentLimit = 30;
    public static final double kFlywheelkP = 0.000050;
    public static final double kFlywheelkF = 0.000165;
  }

  // Okay, this is a little gross
  // Coordinate (0, 0) is outside of the field!! Because of the curved driver stations
  // The point of view is from behind the driver station facing the field on the close side of your
  // robot
  // Which means its as if you were looking at the robot from the other alliance's POV
  // So with that: Y moves you left/right along the intiation line
  // and X moves you towards the driver station wall (theoretically, X = 0 is against the flat part
  // of the wall)
  public static final class LocationConstants {
    public static final class StartingLocations {
      public static final Translation2d kInFrontOfGoal =
          new Translation2d(Units.inchesToMeters(121), Units.inchesToMeters(94.655));
      public static final Translation2d kSneakyPete =
          new Translation2d(Units.inchesToMeters(121), Units.inchesToMeters(286.311));
    }

    public static final double kRobotLengthWithBumpersMeters = Units.inchesToMeters(38);
    public static final double kStandardOffsetDistance = kRobotLengthWithBumpersMeters / 2;
  }

  public static final class IntakeConstants {
    public static final double kIntakeSpeed = 0.5; // Suggested, not final
  }

  public static final class HopperConstants {
    public static final double kLeftNormalFeedSpeed = 0.65;
    public static final double kRightNormalFeedSpeed = 0.65;
    public static final double kLeftUnjamFeedSpeed = -0.3;
    public static final double kRightUnjamFeedSpeed = -0.3;
  }

  public static final class LimelightConstants {
    // Alignment constants
    public static final double kAlignmentkP = 0.0;
    public static final double kAlignmentkI = 0.0;
    public static final double kAlignmentkD = 0.0;
    public static final double kAlignmentAcceptableError = 0.0;
    public static final double kTargetLimelightOffset = 1.0;

    public static final Pipeline kShotPipeline = Pipeline.PIPELINE0;
  }
}
