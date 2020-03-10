/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Shooter;

public class HopperAndAcceleratorSetSpeeds extends CommandBase {
  
  private final Hopper m_hopper;
  private final Shooter m_shooter;

  private double leftSpeed, rightSpeed, acceleratorRPM;
  private boolean didTrigger;

  public HopperAndAcceleratorSetSpeeds(Hopper hopper, Shooter shooter, double leftSpeed, double rightSpeed, double acceleratorRPM) {
    m_hopper = hopper;
    m_shooter = shooter;

    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;
    this.acceleratorRPM = acceleratorRPM;
    
    didTrigger = false;

    addRequirements(m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    didTrigger = false;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (didTrigger || m_shooter.getBallSensor()) {
      didTrigger = true;
      m_hopper.setHopper(0, 0);
      m_shooter.setAcceleratorToRPM(0);
    }
    else {
      m_hopper.setHopper(leftSpeed, rightSpeed);
      m_shooter.setAcceleratorToRPM(acceleratorRPM);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
