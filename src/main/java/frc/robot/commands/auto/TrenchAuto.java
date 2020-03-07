/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

/** Add your docs here. */
public class TrenchAuto extends SequentialCommandGroup {

  public TrenchAuto(
      Drivetrain drivetrain,
      Intake intake,
      Shooter shooter,
      double acceleratorRPM,
      Hopper hopper,
      Limelight limelight,
      double offsetTime1) {

    // Need to fill in createStandardPath distances and angles! Also need to see how much time is
    // left after this to figure out what you want to do at the end
    // super(
    //     new PrepRobotForFeed(null, null, shooter, ShooterConstants.kAcceleratorRPMAutoLine,
    // limelight, 5_500, HoodState.AUTOLINE_SHOT),
    //     new WaitCommand(ShooterConstants.kSpinUpSeconds), // Waits for flywheel spinup
    //     new WaitCommand(offsetTime1),
    //     new FeedSpunUpShooter(hopper,intake, () -> shooter.getShotsFired() > 10, 3),
    //     new ParallelCommandGroup(
    //         new HopperSetSpeed(hopper, 0, 0),
    //         new ShooterSetSpeed(shooter, 0),
    //         new ShooterSetAcceleratorSpeed(shooter, 0),
    //         new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),
    //         new IntakeSetState(intake, IntakeState.EXTENDED),
    //         AutoHelper.createStandardPath(
    //             drivetrain,
    //             false,
    //             Units.feetToMeters(7),
    //             new Pose2d(0, 0, new Rotation2d(0)),
    //             new Pose2d(0, 0, new Rotation2d(0)),
    //             new Translation2d(0, 0)),
    //     new IntakeSetRollers(intake, 0))
    //     );
  }
}
