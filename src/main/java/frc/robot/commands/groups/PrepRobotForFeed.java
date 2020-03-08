package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.DrivetrainAlignToGoal;
import frc.robot.commands.ShooterSetAcceleratorSpeed;
import frc.robot.commands.ShooterSetHoodState;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;
import java.util.Map;

public class PrepRobotForFeed extends ParallelCommandGroup {
  public PrepRobotForFeed(
      Drivetrain drivetrain,
      Shooter shooter,
      double acceleratorRPM,
      Limelight limelight,
      double shooterRPM,
      HoodState hoodState) {
    addCommands(
        new ShooterSetHoodState(shooter, hoodState),
        // If the drivetrain was provided, align to the goal
        new SelectCommand(
            Map.ofEntries(
                Map.entry(false, new DrivetrainAlignToGoal(drivetrain, limelight)),
                Map.entry(true, new InstantCommand())),
            () -> drivetrain == null),
        // Spin the shooter up and wait for it to reach full speed
        new ShooterSetSpeed(shooter, shooterRPM, true).withTimeout(2.5),
        new ShooterSetAcceleratorSpeed(shooter, acceleratorRPM),
        new WaitCommand(ShooterConstants.kSpinUpSeconds));
  }
}
