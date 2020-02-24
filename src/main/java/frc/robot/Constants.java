package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.util.Units;
import frc.robot.subsystems.Limelight.Pipeline;

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
    public static final double kTrackwidthMeters = 0.6295313467128202;
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
    public static final double ksVolts = 0.18;
    public static final double kvVoltSecondsPerMeter = 2.45;
    public static final double kaVoltSecondsSquaredPerMeter = 0.495;

    // Example value only - as above, this must be tuned for your drive!
    public static final double kPDriveVel = 1.91;

    // Motor config
    public static final int kCurrentLimit = 60;
    public static final double kRampRate = 0.0;
    public static final double kJoystickTurnDeadzone = 0.15;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = Units.feetToMeters(5);
    public static final double kMaxAccelerationMetersPerSecondSquared = Units.feetToMeters(5);
    public static final double kAutoMaxDriveVoltage = 10;

    // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
    public static final double kRamseteB = 2.0;
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
    public static final double kTrenchShotRPM = 5_600;
    public static final double kAutoLineRPM = 5_000;
    public static final double kAcceptableRPMRange = 50;
    public static final double kAcceleratorRPM = 6_000;
    public static final double kCurrentDrawnToDetectCompletedShot = 10;
    public static final double kCurrentDrawnTimeWindow = 0.075;
    public static final double kLowRPMThreshold = 1_500;
    public static final double kLowRPMRampRate = 0.25;

    // Motor config
    public static final int kFlywheelCurrentLimit = 30;
    public static final int kAcceleratorCurrentLimit = 25;
    public static final double kFlywheelkP = 0.000050;
    public static final double kFlywheelkF = 0.000165;
  }

  public static final class IntakeConstants {
    public static final double kIntakeSpeed = 0.85; // Suggested, not final
  }

  public static final class HopperConstants {
    public static final double kLeftNormalFeedSpeed = 0.8;
    public static final double kRightNormalFeedSpeed = 0.8;
    public static final double kLeftUnjamFeedSpeed = -0.3;
    public static final double kRightUnjamFeedSpeed = -0.3;
  }

  public static final class LimelightConstants {
    // Alignment constants
    public static final double kAlignmentkP = 0.35;
    public static final double kAlignmentkI = 0.0;
    public static final double kAlignmentkD = 0.075;
    public static final double kAlignmentAcceptableError = 0.3;
    public static final double kTargetLimelightOffset = 1.0;

    public static final Pipeline kShotPipeline = Pipeline.PIPELINE0;
  }
}
