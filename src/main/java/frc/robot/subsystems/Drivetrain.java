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
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

  private final CANSparkMax left1, left2, right1, right2;
  private final ADXRS450_Gyro gyro;

  private final CANEncoder leftEncoder, rightEncoder;
  private final DifferentialDriveOdometry m_odometry;

  public Drivetrain() {
    left1 = new CANSparkMax(0, MotorType.kBrushless);
    left2 = new CANSparkMax(1, MotorType.kBrushless);
    right1 = new CANSparkMax(2, MotorType.kBrushless);
    right2 = new CANSparkMax(3, MotorType.kBrushless);

    left2.follow(left1);
    right2.follow(right1);

    left1.setInverted(true);
    left2.setInverted(true);

    left1.setSmartCurrentLimit(60);
    right1.setSmartCurrentLimit(60);

    left1.setOpenLoopRampRate(0.0);
    right1.setOpenLoopRampRate(0.0);

    left1.setIdleMode(IdleMode.kBrake);
    left2.setIdleMode(IdleMode.kCoast);
    right1.setIdleMode(IdleMode.kBrake);
    right2.setIdleMode(IdleMode.kCoast);

    final double wheelDiameter = 4.0;
    final double pulsePerRev = 42.0;
    final double gearRatio = 6.67;
    final double distancePerPulse =
        Math.PI * Units.inchesToMeters(wheelDiameter) / pulsePerRev / gearRatio;

    // Set encoders to return distance in terms of meters
    left1.getEncoder().setPositionConversionFactor(1.0 / distancePerPulse);
    right1.getEncoder().setPositionConversionFactor(1.0 / distancePerPulse);

    // Set encoders to return velocity in terms of meters per second
    left1.getEncoder().setVelocityConversionFactor((1.0 / distancePerPulse) * (1.0 / 60.0));
    right1.getEncoder().setVelocityConversionFactor((1.0 / distancePerPulse) * (1.0 / 60.0));

    leftEncoder = left1.getEncoder();
    rightEncoder = right1.getEncoder();

    gyro = new ADXRS450_Gyro();
    resetEncoders();
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

  public void GTADrive(double leftTrigger, double rightTrigger, double turnValue) {
    // Wren: implement this
    // This is a custom control scheme that we used last year, and 2791 has used since 2014
    // Left trigger is reverse, right trigger is forward, turn value is how fast the drivetrain
    // should be rotating
    // You can see an implementation of it here:
    // https://github.com/FRC5254/FRC-5254---2019/blob/master/src/main/java/frc/robot/subsystems/Drivetrain.java#L110
    // Try to understand how it works. For example, there is a function we use called `Math.signum`.
    // You can see some docs on the `Math` package here:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html
    // (Math is a library built into Java by default)
    // I will ask you to explain how the drive code works on Saturday

    // code here:

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
    left1.set(leftVolts / 12.0);
    right1.set(rightVolts / 12.0);
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
    return Math.IEEEremainder(gyro.getAngle(), 360);
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return gyro.getRate();
  }
}
