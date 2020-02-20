/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.LimelightConstants;
import frc.robot.Limelight;
import frc.robot.Limelight.CamMode;
import frc.robot.subsystems.Drivetrain;

public class DrivetrainAlignToGoal extends CommandBase {
  /** Creates a new DrivetrainAlignToGoal. */
  Drivetrain m_drivetrain;

  PIDController pidController;

  public DrivetrainAlignToGoal(Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    m_drivetrain = drivetrain;
    pidController =
        new PIDController(
            LimelightConstants.kAlignmentkP,
            LimelightConstants.kAlignmentkI,
            LimelightConstants.kAlignmentkD);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // We want the limelight horizontal offset to be a specific value (probably 1.0 for centered)
    pidController.setSetpoint(LimelightConstants.kTargetLimelightOffset);
    pidController.setTolerance(LimelightConstants.kAlignmentAcceptableError);
    Limelight.setCamMode(CamMode.VISION_CAM);
    Limelight.setPipeline(LimelightConstants.kShotPipeline);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double pidOutput = pidController.calculate(Limelight.getHorizontalOffset());
    m_drivetrain.tankDriveVolts(-pidOutput, pidOutput);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.tankDriveVolts(0, 0);
    Limelight.setCamMode(CamMode.DRIVER_CAM);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return pidController.atSetpoint();
  }
}
