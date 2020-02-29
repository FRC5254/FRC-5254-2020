/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.HopperConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.DrivetrainAlignToGoal;
import frc.robot.commands.HopperSetSpeed;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.IntakeSetState;
import frc.robot.commands.ShooterSetAcceleratorSpeed;
import frc.robot.commands.ShooterSetHoodState;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.commands.auto.AutoLineAuto;
import frc.robot.commands.auto.WallShotAuto;
import frc.robot.commands.auto.SneakyPete;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain m_robotDrive = new Drivetrain();
  public final Shooter m_shooter = new Shooter();
  public final Hopper m_hopper = new Hopper();
  private final Intake m_intake = new Intake();
  private final Climber m_climber = new Climber();
  private final Limelight m_limelight = new Limelight();

  // Controllers
  public final XboxController driverController = new XboxController(0);
  public final XboxController operatorController = new XboxController(1);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Set default commands for each subsystem
    m_robotDrive.setDefaultCommand(
        new RunCommand(
            () -> {
              if (driverController.getBButton()) {
                m_robotDrive.GTADrive(
                    driverController.getTriggerAxis(GenericHID.Hand.kLeft) / 2,
                    driverController.getTriggerAxis(GenericHID.Hand.kRight) / 2,
                    driverController.getX(GenericHID.Hand.kLeft) / 2);
              } else {
                m_robotDrive.GTADrive(
                    driverController.getTriggerAxis(GenericHID.Hand.kLeft),
                    driverController.getTriggerAxis(GenericHID.Hand.kRight),
                    driverController.getX(GenericHID.Hand.kLeft));
              }
            },
            m_robotDrive));

    m_climber.setDefaultCommand(
        new RunCommand(
            () -> {
              if (driverController.getBButton()) {
                m_climber.setTelescopeSpeed(0);
              } else {
                m_climber.setTelescopeSpeed(operatorController.getY(GenericHID.Hand.kLeft));
              }
            },
            m_climber));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // DRIVER CONTROLS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Hopper intake (forward)
    new JoystickButton(driverController, XboxController.Button.kBumperRight.value)
        .whenPressed(
            new HopperSetSpeed(
                m_hopper,
                HopperConstants.kLeftNormalFeedSpeed,
                HopperConstants.kRightNormalFeedSpeed))
        .whenReleased(new HopperSetSpeed(m_hopper, 0.0, 0.0));

    // Hopper unjam (backward)
    new JoystickButton(driverController, XboxController.Button.kBumperLeft.value)
        .whenPressed(
            new HopperSetSpeed(
                m_hopper,
                HopperConstants.kLeftUnjamFeedSpeed,
                HopperConstants.kRightUnjamFeedSpeed))
        .whenReleased(new HopperSetSpeed(m_hopper, 0.0, 0.0));

    new JoystickButton(driverController, XboxController.Button.kY.value)
        .whileActiveOnce(new DrivetrainAlignToGoal(m_robotDrive, m_limelight));

    // OPERATOR CONTROLS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Extend intake (down)
    new JoystickButton(operatorController, XboxController.Button.kX.value)
        .whenPressed(new IntakeSetState(m_intake, IntakeState.EXTENDED));

    // Retract intake (up)
    new JoystickButton(operatorController, XboxController.Button.kA.value)
        .whenPressed(new IntakeSetState(m_intake, IntakeState.RETRACTED));

    // Rollers intake
    new Trigger(
            () -> {
              return operatorController.getTriggerAxis(GenericHID.Hand.kRight) > 0.1;
            })
        .whenActive(new IntakeSetRollers(m_intake, IntakeConstants.kIntakeSpeed))
        .whenInactive(new IntakeSetRollers(m_intake, 0.0));

    // Rollers outtake
    new JoystickButton(operatorController, XboxController.Button.kBack.value)
        .whenPressed(new IntakeSetRollers(m_intake, -IntakeConstants.kIntakeSpeed))
        .whenReleased(new IntakeSetRollers(m_intake, 0.0));

    // Wall shot (shooter, accelerator, & hood)
    new JoystickButton(operatorController, XboxController.Button.kBumperRight.value)
        .whenPressed(new ShooterSetSpeed(m_shooter, ShooterConstants.kWallShotRPM))
        .whenPressed(
            new ShooterSetAcceleratorSpeed(m_shooter, ShooterConstants.kAcceleratorRPMWall))
        .whenPressed(new ShooterSetHoodState(m_shooter, HoodState.WALL_SHOT));

    // Auto line shot (shooter, accelerator, & hood)
    new JoystickButton(operatorController, XboxController.Button.kBumperLeft.value)
        .whenPressed(new ShooterSetSpeed(m_shooter, ShooterConstants.kAutoLineRPM))
        .whenPressed(
            new ShooterSetAcceleratorSpeed(m_shooter, ShooterConstants.kAcceleratorRPMAutoLine))
        .whenPressed(new ShooterSetHoodState(m_shooter, HoodState.AUTOLINE_SHOT));

    // Turn shooter + accelerator off
    new JoystickButton(operatorController, XboxController.Button.kY.value)
        .whenPressed(new ShooterSetSpeed(m_shooter, 0.0))
        .whenPressed(new ShooterSetAcceleratorSpeed(m_shooter, 0.0));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
        new InstantCommand(
            () -> {
              m_robotDrive.zeroHeading();
              m_robotDrive.resetEncoders();
              m_robotDrive.resetOdometry(new Pose2d(0, 0, new Rotation2d(0)));
            }),
        // new WallShotAuto(m_robotDrive, m_intake, m_shooter, 0, m_hopper, 5),
        new AutoLineAuto(m_robotDrive, m_intake, m_shooter, 0, m_hopper, m_limelight, 2),
        new InstantCommand(
            () -> {
              m_robotDrive.tankDriveVolts(0, 0);
            },
            m_robotDrive));
  }
}
