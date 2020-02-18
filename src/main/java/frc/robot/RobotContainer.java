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
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.HopperSetSpeed;
import frc.robot.commands.IntakeSetExtended;
import frc.robot.commands.IntakeSetRetracted;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.ShooterSetAcceleratorSpeed;
import frc.robot.commands.ShooterSetHoodState;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.commands.auto.AutoHelper;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
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
//   private final Shooter m_shooter = new Shooter();
  private final Hopper m_hopper = new Hopper();
  private final Intake m_intake = new Intake();

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
              if(driverController.getBButton()) {
              m_robotDrive.GTADrive(
                  driverController.getTriggerAxis(GenericHID.Hand.kLeft) / 2,
                  driverController.getTriggerAxis(GenericHID.Hand.kRight) / 2,
                  -driverController.getX(GenericHID.Hand.kLeft) / 2);
              }
              else {
                m_robotDrive.GTADrive(
                  driverController.getTriggerAxis(GenericHID.Hand.kLeft),
                  driverController.getTriggerAxis(GenericHID.Hand.kRight),
                  -driverController.getX(GenericHID.Hand.kLeft));
              }
            },
            m_robotDrive));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // DRIVER CONTROLS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Don't know actual direction for hopper motors yet
    // Hopper intake (forward)
    new JoystickButton(driverController, XboxController.Button.kBumperRight.value)
        .whenHeld(new HopperSetSpeed(m_hopper, 1.0, -1.0));

    // Don't know actual direction for hopper motors yet
    // Hopper unjam (backward)
    new JoystickButton(driverController, XboxController.Button.kBumperLeft.value)
        .whenHeld(new HopperSetSpeed(m_hopper, -0.5, 0.5));
    

    // OPERATOR CONTROLS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Extend intake (down)
    new JoystickButton(operatorController, XboxController.Button.kX.value)
        .whenPressed(new IntakeSetExtended(m_intake));

    // Retract intake (up)
    new JoystickButton(operatorController, XboxController.Button.kA.value)
        .whenPressed(new IntakeSetRetracted(m_intake));

    // Rollers intake
    new Trigger(
            () -> {
              return operatorController.getTriggerAxis(GenericHID.Hand.kRight) > .1;
            })
        .whenActive(new IntakeSetRollers(m_intake, 1.0));

    // Rollers outtake
    new JoystickButton(operatorController, XboxController.Button.kBack.value)
        .whenHeld(new IntakeSetRollers(m_intake, -1.0));

    // Wall shot
    // new JoystickButton(operatorController, XboxController.Button.kBumperRight.value)
    //     .whenPressed(new ShooterSetSpeed(m_shooter, ShooterConstants.kWallShotRPM))
    //     .whenPressed(new ShooterSetAcceleratorSpeed(m_shooter, ShooterConstants.kAcceleratorRPM));

    // Trench shot
    // new JoystickButton(operatorController, XboxController.Button.kBumperLeft.value)
    //     .whenPressed(new ShooterSetSpeed(m_shooter, ShooterConstants.kTrenchShotRPM))
    //     .whenPressed(new ShooterSetAcceleratorSpeed(m_shooter, ShooterConstants.kAcceleratorRPM));

    // Turn shooter off
    // new JoystickButton(operatorController, XboxController.Button.kStart.value)
    //     .whenPressed(new ShooterSetSpeed(m_shooter, 0.0));

    // Hood state TRENCH
    // new JoystickButton(operatorController, XboxController.Button.kY.value)
    //     .whenPressed(new ShooterSetHoodState(m_shooter, HoodState.TRENCH_SHOT));
    
    // // Hood state WALL
    // new JoystickButton(operatorController, XboxController.Button.kB.value)
    //     .whenPressed(new ShooterSetHoodState(m_shooter, HoodState.WALL_SHOT));
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
        AutoHelper.createStandardPath(
            m_robotDrive,
            new Pose2d(0, 0, new Rotation2d(0)),
            new Pose2d(2, 0, new Rotation2d(0)),
            new Translation2d(0.25, 0.25),
            new Translation2d(0.5, 0.5),
            new Translation2d(0.75, 0.5),
            new Translation2d(1.0, 0.25),
            new Translation2d(1.25, 0.0),
            new Translation2d(1.5, 0.0)),
        new InstantCommand(
            () -> {
              m_robotDrive.tankDriveVolts(0, 0);
            },
            m_robotDrive));
  }
}
