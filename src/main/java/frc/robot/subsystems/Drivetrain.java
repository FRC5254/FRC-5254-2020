package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.RobotMap;

public class Drivetrain extends SubsystemBase {

  private final CANSparkMax left1, left2, right1, right2;
  private final ADXRS450_Gyro gyro;
  public final CANEncoder leftEncoder, rightEncoder;
  public final DifferentialDriveOdometry m_odometry;

  public Drivetrain() {
    left1 = new CANSparkMax(RobotMap.kDriveMotorLeft1, MotorType.kBrushless);
    left2 = new CANSparkMax(RobotMap.kDriveMotorLeft2, MotorType.kBrushless);
    right1 = new CANSparkMax(RobotMap.kDriveMotorRight1, MotorType.kBrushless);
    right2 = new CANSparkMax(RobotMap.kDriveMotorRight2, MotorType.kBrushless);

    left1.restoreFactoryDefaults();
    left2.restoreFactoryDefaults();
    right1.restoreFactoryDefaults();
    right2.restoreFactoryDefaults();

    left2.follow(left1);
    right2.follow(right1);

    right1.setInverted(true);
    right2.setInverted(true);

    left1.setSmartCurrentLimit(DriveConstants.kCurrentLimit);
    right1.setSmartCurrentLimit(DriveConstants.kCurrentLimit);

    left1.setOpenLoopRampRate(DriveConstants.kRampRate);
    right1.setOpenLoopRampRate(DriveConstants.kRampRate);

    left1.setIdleMode(IdleMode.kBrake);
    left2.setIdleMode(IdleMode.kCoast);
    right1.setIdleMode(IdleMode.kBrake);
    right2.setIdleMode(IdleMode.kCoast);

    // Set encoders to return distance in terms of meters
    left1.getEncoder().setPositionConversionFactor(DriveConstants.kDistancePerPulse);
    right1.getEncoder().setPositionConversionFactor(DriveConstants.kDistancePerPulse);

    // Set encoders to return velocity in terms of meters per second
    left1.getEncoder().setVelocityConversionFactor(DriveConstants.kDistancePerPulse / 60.0);
    right1.getEncoder().setVelocityConversionFactor(DriveConstants.kDistancePerPulse / 60.0);

    leftEncoder = left1.getEncoder();
    rightEncoder = right1.getEncoder();

    gyro = new ADXRS450_Gyro();
    resetEncoders();
    zeroHeading();
    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(
        Rotation2d.fromDegrees(getHeading()),
        leftEncoder.getPosition(),
        rightEncoder.getPosition());
  }

  public void GTADrive(double leftTrigger, double rightTrigger, double turn) {
    if (-DriveConstants.kJoystickTurnDeadzone <= turn
        && turn <= DriveConstants.kJoystickTurnDeadzone) {
      turn = 0.0;
    }

    turn = turn * turn * Math.signum(turn);

    double left = rightTrigger - leftTrigger + turn;
    double right = rightTrigger - leftTrigger - turn;
    left = Math.min(1.0, Math.max(-1.0, left));
    right = Math.max(-1.0, Math.min(1.0, right));

    right1.set(right);
    left1.set(left);
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Returns the current wheel speeds of the robot.
   *
   * @return The current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity(), rightEncoder.getVelocity());
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
  }

  /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    left1.setVoltage(leftVolts);
    right1.setVoltage(rightVolts);
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   *
   * @return the left drive encoder
   */
  public CANEncoder getLeftEncoder() {
    return leftEncoder;
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public CANEncoder getRightEncoder() {
    return rightEncoder;
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    gyro.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from 180 to 180
   */
  public double getHeading() {
    // return 0;
    return Math.IEEEremainder(gyro.getAngle(), 360);
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    // return 0;
    return gyro.getRate();
  }
}
