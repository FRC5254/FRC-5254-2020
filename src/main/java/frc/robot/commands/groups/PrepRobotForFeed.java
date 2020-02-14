package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import frc.robot.commands.DrivetrainAlignToGoal;
import frc.robot.commands.IntakeSetExtended;
import frc.robot.commands.ShooterSetHoodState;
import frc.robot.commands.ShooterSetSpeed;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.HoodState;
import java.util.Map;

class PrepRobotForFeed extends ParallelCommandGroup {
  public PrepRobotForFeed(
      Drivetrain drivetrain,
      Intake intake,
      Shooter shooter,
      double shooterRPM,
      HoodState hoodState) {
    addCommands(
        new ShooterSetHoodState(shooter, hoodState),
        new SelectCommand(
            Map.ofEntries(
                Map.entry(false, new IntakeSetExtended(intake)),
                Map.entry(true, new InstantCommand())),
            () -> intake == null),
        // If the drivetrain was provided, align to the goal
        new SelectCommand(
            Map.ofEntries(
                Map.entry(false, new DrivetrainAlignToGoal(drivetrain)),
                Map.entry(true, new InstantCommand())),
            () -> drivetrain == null),
        // Spin the shooter up and wait for it to reach full speed
        new ShooterSetSpeed(shooter, shooterRPM, true));
  }
}
