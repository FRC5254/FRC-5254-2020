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
import frc.robot.commands.*;
import frc.robot.commands.auto.AutoLineAuto;
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
    private final Limelight m_limelight = new Limelight();

    // Controllers
    public final XboxController driverController = new XboxController(0);
    public final XboxController operatorController = new XboxController(1);

    // Generate this in robot container constructor so that the command is created on robot init
    // not on auto start (it can take up to 0.5s to generate the paths)
    public final Command autoCommand;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the button bindings
        configureSingleControllerButtonBindings();
//    configureDualControllerButtonBindings();

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

        // Generate paths at robot init
        autoCommand =
                new SequentialCommandGroup(
                        new InstantCommand(
                                () -> {
                                    m_robotDrive.zeroHeading();
                                    m_robotDrive.resetEncoders();
                                    m_robotDrive.resetOdometry(new Pose2d(0, 0, new Rotation2d(0)));
                                }),
                        // new SneakyPete(m_robotDrive, m_intake, m_shooter, m_hopper, m_limelight)
                        // new WallShotAuto(m_robotDrive, m_intake, m_shooter, m_hopper, m_limelight, 0)
                        new AutoLineAuto(m_robotDrive, m_intake, m_shooter, ShooterConstants.kAcceleratorRPMAutoLine, m_hopper, m_limelight, 0)
                );
    }

    /**
     * Configures the common button bindings which are always the responsibility of the driver.
     */
    private void configureCommonDriverControllerButtons() {
        // HOPPER ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Hopper in (forward)
        new JoystickButton(driverController, XboxController.Button.kA.value)
                .whenPressed(
                        new HopperSetSpeed(
                                m_hopper, m_shooter,
                                HopperConstants.kLeftNormalFeedSpeed,
                                HopperConstants.kRightNormalFeedSpeed))
                .whenReleased(new HopperSetSpeed(m_hopper, m_shooter, 0.0, 0.0));

        // Hopper unjam (backward)
        new JoystickButton(driverController, XboxController.Button.kStickRight.value)
                .whenPressed(
                        new HopperSetSpeed(
                                m_hopper, m_shooter,
                                HopperConstants.kLeftUnjamFeedSpeed,
                                HopperConstants.kRightUnjamFeedSpeed))
                .whenReleased(new HopperSetSpeed(m_hopper, m_shooter, 0.0, 0.0));

        // SHOOTING ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Limelight lineup
        new JoystickButton(driverController, XboxController.Button.kY.value)
                .whileActiveOnce(new DrivetrainAlignToGoal(m_robotDrive, m_limelight));

        // Wall shot (shooter, accelerator, & hood)
        new JoystickButton(driverController, XboxController.Button.kBumperRight.value)
                .whenPressed(new ShooterSetSpeed(m_shooter, ShooterConstants.kWallShotRPM))
                .whenPressed(
                        new ShooterSetAcceleratorSpeed(m_shooter, ShooterConstants.kAcceleratorRPMWall))
                .whenPressed(new ShooterSetHoodState(m_shooter, HoodState.WALL_SHOT));

        // Auto line shot (shooter, accelerator, & hood)
        new JoystickButton(driverController, XboxController.Button.kBumperLeft.value)
                .whenPressed(new ShooterSetSpeed(m_shooter, ShooterConstants.kAutoLineRPM))
                .whenPressed(
                        new ShooterSetAcceleratorSpeed(m_shooter, ShooterConstants.kAcceleratorRPMAutoLine))
                .whenPressed(new ShooterSetHoodState(m_shooter, HoodState.AUTOLINE_SHOT));

        // Turn shooter + accelerator off
        new JoystickButton(driverController, XboxController.Button.kB.value)
                .whenPressed(new ShooterSetSpeed(m_shooter, 0.0))
                .whenPressed(new ShooterSetAcceleratorSpeed(m_shooter, 0.0));

    }

    /**
     * Configure the button bindings for all being on the drivers controller.
     */
    private void configureSingleControllerButtonBindings() {
        configureCommonDriverControllerButtons();

        // "OPERATOR" CONTROLS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Extend intake (down)
        new Trigger(() -> driverController.getPOV() == 180) // down
                .whenActive(new IntakeSetState(m_intake, IntakeState.EXTENDED));

        // Retract intake (up)
        new Trigger(() -> driverController.getPOV() == 0) // up
                .whenActive(new IntakeSetState(m_intake, IntakeState.RETRACTED));

        // Intake in
        new JoystickButton(driverController, XboxController.Button.kX.value)
                .whenPressed(new IntakeToggleRollers(m_intake, IntakeConstants.kIntakeSpeed));

        // Intake out
        new JoystickButton(driverController, XboxController.Button.kBack.value)
                .whenPressed(new IntakeToggleRollers(m_intake, -IntakeConstants.kIntakeSpeed));
    }

    /**
     * Configure the button bindings for some being on the operator controller.
     */
    private void configureDualControllerButtonBindings() {
        configureCommonDriverControllerButtons();

        // OPERATOR CONTROLS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Extend intake (down)
        new Trigger(() -> operatorController.getPOV() == 180) // down
                .whenActive(new IntakeSetState(m_intake, IntakeState.EXTENDED));

        // Retract intake (up)
        new Trigger(() -> operatorController.getPOV() == 0) // up
                .whenActive(new IntakeSetState(m_intake, IntakeState.RETRACTED));

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
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoCommand;
    }
}
