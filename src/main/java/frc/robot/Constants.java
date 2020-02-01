package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import io.github.oblarg.oblog.annotations.Log;

public final class Constants {
  public static final class RobotMap {
    // Drivetrain
    public static int kDriveMotorLeft1 = 1;
    public static int kDriveMotorLeft2 = 2;
    public static int kDriveMotorRight1 = 4;
    public static int kDriveMotorRight2 = 6;

    // Shooter
    public static int kFlywheelMotor1 = 4;
    public static int kFlywheelMotor2 = 5;
    public static int kAcceleratorMotor = 6;
  }

  public static final class DriveConstants {
    // Physical details
    public static final double kTrackwidthMeters = Units.inchesToMeters(24);
    public static final DifferentialDriveKinematics kDriveKinematics =
        new DifferentialDriveKinematics(kTrackwidthMeters);
    public static final double kWheelDiameter = 6.0;
    public static final double kEncoderPulsePerRev = 42.0;
    public static final double kGearRatio = (50.0 / 11.0) * (50.0 / 24.0);
    public static final double kDistancePerPulse = (1.0 / kGearRatio) * Units.inchesToMeters(6.0) * Math.PI;

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
    public static final double kMaxSpeedMetersPerSecond = 15;
    public static final double kMaxAccelerationMetersPerSecondSquared = 15;
    public static final double kAutoMaxDriveVoltage = 10;

    // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
  }

  public static final class ShooterConstants {
    // Physical details
    public static final double kFlywheelGearRatio = 3.0;
    public static final double kFlywheelMaxRPM = 11_000 / kFlywheelGearRatio;
    public static final double kAcceleratorGearRatio = 4.0;
    public static final double kAcceleratorMaxRPM = 11_000 / kAcceleratorGearRatio;

    // Shot details
    public static final double kWallShotRPM = 3_000;
    public static final double kAutoLineRPM = 2_500;
    public static final double kAcceptableRPMRange = 50;
    public static final double kAcceleratorRPM = 2_500;

    // Motor config
    public static final int kFlywheelCurrentLimit = 100;
    public static final int kAcceleratorCurrentLimit = 100;
  }
}
