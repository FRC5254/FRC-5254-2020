package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeToggleRollers extends CommandBase {
    /** Creates a new IntakeSetRollers. */
    private final Intake m_intake;

    private double speed;

    public IntakeToggleRollers(Intake intake, double speed) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_intake = intake;
        this.speed = speed;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_intake.toggleIntakeSpeed(speed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }
}
