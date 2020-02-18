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
  private final DoubleSolenoid intakeDoubleSolenoidLeft;
  private final DoubleSolenoid intakeDoubleSolenoidRight;

  public Intake() {

    rollers = new CANSparkMax(RobotMap.kIntakeMotor, MotorType.kBrushed);
    intakeDoubleSolenoidLeft = new DoubleSolenoid(RobotMap.kIntakeDoubleSolenoidBack, RobotMap.kIntakeDoubleSolenoidFront);
    intakeDoubleSolenoidRight = new DoubleSolenoid(RobotMap.kIntakeDoubleSolenoidBack, RobotMap.kIntakeDoubleSolenoidFront);

    rollers.restoreFactoryDefaults();

    rollers.setIdleMode(IdleMode.kBrake);
  }

  public void setIntakeMotor(double speed) {
    rollers.set(speed);
  }

  public void extend() {
    intakeDoubleSolenoidLeft.set(Value.kForward);
    intakeDoubleSolenoidRight.set(Value.kForward);
  }

  public void retract() {
    intakeDoubleSolenoidLeft.set(Value.kReverse);
    intakeDoubleSolenoidRight.set(Value.kReverse);
  }
}
