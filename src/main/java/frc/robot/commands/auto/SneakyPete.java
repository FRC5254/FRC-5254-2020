/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.commands.IntakeSetState;
import frc.robot.commands.groups.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class SneakyPete extends SequentialCommandGroup {
  /** Creates a new SneakyPete. */
  public SneakyPete(
      Drivetrain drivetrain, Intake intake, Shooter shooter, Hopper hopper, Limelight limelight) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
        new ParallelCommandGroup(
            new IntakeSetState(intake, IntakeState.EXTENDED),
            new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
            AutoHelper.createStandardPath(
                drivetrain,
                false,
                Units.feetToMeters(2.5),
                new Pose2d(0, 0, new Rotation2d(0)),
                new Pose2d(Units.inchesToMeters(125 - 12 - 12 - 4 - 1), 0, new Rotation2d(0)),
                new Translation2d(Units.inchesToMeters(60), 0))),
        new WaitCommand(0.5),
        AutoHelper.createStandardPath(
            drivetrain,
            true,
            Units.feetToMeters(9.5),
            new Pose2d(Units.inchesToMeters(125), 0, new Rotation2d(0)),
            new Pose2d(
                Units.inchesToMeters(24),
                Units.inchesToMeters(100),
                Rotation2d.fromDegrees(-180 - 45)),
            new Translation2d(-Units.inchesToMeters(0), Units.inchesToMeters(70))),
        new PrepRobotForFeed(
            drivetrain,
            intake,
            shooter,
            ShooterConstants.kAcceleratorRPMAutoLine,
            limelight,
            ShooterConstants.kAutoLineRPM,
            HoodState.AUTOLINE_SHOT),
        new FeedSpunUpShooter(
            hopper,
            () ->
                shooter.getShotsFired()
                    > 5, // This may be interferring with shooting --> would maybe be easier without
                         // FeedSpunUpShooter?
            5));
  }
}
