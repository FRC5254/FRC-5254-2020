package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants.HopperConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.commands.HopperSetSpeed;
import frc.robot.commands.IntakeSetRollers;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

import java.util.function.BooleanSupplier;

public class FeedSpunUpShooter extends ParallelCommandGroup {
  public FeedSpunUpShooter(
      Hopper hopper, Intake intake, Shooter shooter, BooleanSupplier endCondition, double timeout) {
    addCommands(
        // Spin up hopper
        new HopperSetSpeed(
            hopper, shooter, HopperConstants.kLeftNormalFeedSpeed, HopperConstants.kRightNormalFeedSpeed),
        // Intaking to make sure powercells don't get stuck in intake
        new IntakeSetRollers(intake, IntakeConstants.kIntakeSpeed),

        // Wait until the end condition is satisfied,
        // or until time elapses
        new WaitUntilCommand(endCondition).withTimeout(timeout));
  }
}
