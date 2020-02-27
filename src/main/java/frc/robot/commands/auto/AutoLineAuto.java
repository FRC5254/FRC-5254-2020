/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
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
import frc.robot.Constants.HopperConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.HopperSetSpeed;
import frc.robot.commands.ShooterSetAcceleratorSpeed;
import frc.robot.commands.ShooterSetHoodState;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;

/** Add your docs here. */
public class AutoLineAuto extends SequentialCommandGroup {

  public AutoLineAuto(
      Drivetrain drivetrain,
      Intake intake,
      Shooter shooter,
      double acceleratorRPM,
      Hopper hopper,
      Limelight limelight,
      double offsetTime1,
      double offsetTime2) {

    super(
        new ParallelCommandGroup(
            new ShooterSetSpeed(shooter, ShooterConstants.kAutoLineRPM),
            new ShooterSetHoodState(shooter, HoodState.AUTOLINE_SHOT),
            new ShooterSetAcceleratorSpeed(shooter, ShooterConstants.kAcceleratorRPMAutoLine)),
        new WaitCommand(offsetTime1),
        new HopperSetSpeed(
            hopper, HopperConstants.kLeftNormalFeedSpeed, HopperConstants.kRightNormalFeedSpeed),
        new WaitCommand(offsetTime2),
        new ParallelCommandGroup(
            new ShooterSetSpeed(shooter, 0),
            new ShooterSetAcceleratorSpeed(shooter, 0),
            new HopperSetSpeed(hopper, 0, 0),
            AutoHelper.createStandardPath(
                drivetrain,
                false,
                Units.feetToMeters(7),
                new Pose2d(0, 0, new Rotation2d(0)),
                new Pose2d(-Units.inchesToMeters(5), 0, new Rotation2d(0)),
                new Translation2d(0, 0) // Wren: not sure what Translation2d does
                ),
            new HopperSetSpeed(hopper, 0, 0),
            new ShooterSetSpeed(shooter, 0),
            new ShooterSetAcceleratorSpeed(shooter, 0)));
  }
}