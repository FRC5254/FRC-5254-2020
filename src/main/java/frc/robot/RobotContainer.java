/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.RamseteGenerator;
import frc.robot.commands.ShooterShootNScore;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain m_robotDrive = new Drivetrain();
  private final Shooter m_shooter = new Shooter();

  // Controllers
  public final XboxController driverController = new XboxController(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Set default commands for each subsystem
    m_robotDrive.setDefaultCommand(
        new RunCommand(
            () -> {
              m_robotDrive.GTADrive(
                  driverController.getTriggerAxis(GenericHID.Hand.kLeft),
                  driverController.getTriggerAxis(GenericHID.Hand.kRight),
                  driverController.getX(GenericHID.Hand.kLeft));
            }, m_robotDrive));

    new JoystickButton(driverController, Button.kA.value)
        .whileActiveOnce(new ShooterShootNScore(m_shooter));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return RamseteGenerator.createStandardPath(
            m_robotDrive,
            new Pose2d(0, 0, new Rotation2d(0)), // start
            new Pose2d(3, 0, new Rotation2d(0)), // end
            // all middle points:
            new Translation2d(1, 1),
            new Translation2d(2, -1))
        .andThen(() -> m_robotDrive.tankDriveVolts(0, 0), m_robotDrive);
  }
}
