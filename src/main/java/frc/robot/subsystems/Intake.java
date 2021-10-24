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
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.RobotMap;

public class Intake extends SubsystemBase {
  public enum IntakeState {
    EXTENDED(Value.kReverse),
    RETRACTED(Value.kForward);

    private Value state;

    private IntakeState(Value state) {
      this.state = state;
    }
  }

  private CANSparkMax rollers;
  private DoubleSolenoid intakeDoubleSolenoid;
  private IntakeState intakeState;
  private double intakeSpeed;

  public Intake() {
    intakeState = null;
    intakeSpeed = 0.0;
    rollers = new CANSparkMax(RobotMap.kIntakeMotor, MotorType.kBrushed);
    intakeDoubleSolenoid =
        new DoubleSolenoid(RobotMap.kIntakeDoubleSolenoidFront, RobotMap.kIntakeDoubleSolenoidBack);

    rollers.restoreFactoryDefaults();

    rollers.setIdleMode(IdleMode.kBrake);
    rollers.setSmartCurrentLimit(IntakeConstants.kIntakeCurrentLimit);
  }

  public void setIntakeMotor(double speed) {
    this.intakeSpeed = speed;
    rollers.set(this.intakeSpeed);
  }

  public void toggleIntakeSpeed(double speed) {
    if(speed == this.intakeSpeed) {
      // We are toggling with the current value, that means we want to turn the intake off
      this.setIntakeMotor(0.0);
    } else {
      // The value is something else currently so we want to set it to the speed
      this.setIntakeMotor(speed);
    }
  }

  public void setIntakeState(IntakeState state) {
    intakeState = state;
    intakeDoubleSolenoid.set(state.state);
  }

  @Override
  public void periodic() {
    if (intakeState == null) {
      setIntakeState(IntakeState.RETRACTED);
    }

    // Current peak number is a guess right now
    // if (rollers.getOutputCurrent() >= 15) {
    //   CommandScheduler.getInstance().schedule(new ControllerVibrate(0.5, 1.0,
    // ControllerChoice.BOTH));
    // }
  }
}
