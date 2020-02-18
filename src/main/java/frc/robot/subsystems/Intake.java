/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RobotMap;

public class Intake extends SubsystemBase {

  private final CANSparkMax rollers;
  private final DoubleSolenoid intakeDoubleSolenoid;

  public Intake() {

    rollers = new CANSparkMax(RobotMap.kIntakeMotor, MotorType.kBrushed);
    intakeDoubleSolenoid = new DoubleSolenoid(RobotMap.kIntakeDoubleSolenoidFront, RobotMap.kIntakeDoubleSolenoidBack);

    rollers.restoreFactoryDefaults();

    rollers.setIdleMode(IdleMode.kBrake);
  }

  public void setIntakeMotor(double speed) {
    rollers.set(speed);
  }

  public void extend() {
    intakeDoubleSolenoid.set(Value.kReverse);
  }
    
  public void retract() {
    intakeDoubleSolenoid.set(Value.kForward);
  }
}
