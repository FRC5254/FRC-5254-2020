/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpiutil.net.PortForwarder;

import java.util.Map;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  public static RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    // Set up port forwarding so we can access the limelight over USB :)
    // visit '172.22.11.2:5800' in your browser (ie, Chrome) on the laptop
    // to see the limelight
    // ... maybe
    // Not sure why 5805 is required. Someone recommended it
    // Probably internal limelight networking stuff
    PortForwarder.add(5800, "limelight.local", 5800);
    PortForwarder.add(5801, "limelight.local", 5801);
    PortForwarder.add(5805, "limelight.local", 5805);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    m_robotContainer.m_shooter.resetShotsFired();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    SmartDashboard.putBoolean("Auto command is finished", m_autonomousCommand.isFinished());

    // Run HelixLogger
    // HelixLogger.getInstance().saveLogs();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    m_robotContainer.m_climber.resetTelescopeEncoder();
    m_robotContainer.m_climber.setTelescopeSpeed(0);

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Run HelixLogger
    // HelixLogger.getInstance().saveLogs();

    // Set this to true when we want to test color wheel stuff
    final boolean checkForGameData = false;
    if (checkForGameData) {
      String gameData = DriverStation.getInstance().getGameSpecificMessage().toUpperCase();
      String dashboardString = "";

      if (gameData.length() > 0) {
        Map<String, String> dataToFullColorName =
            Map.ofEntries(
                Map.entry("R", "Red"),
                Map.entry("G", "Green"),
                Map.entry("B", "Blue"),
                Map.entry("Y", "Yellow"));
        dashboardString =
            dataToFullColorName.getOrDefault(gameData, "CORRUPT DATA -- CALL AN FTA / CSA");
      }

      SmartDashboard.putString("Wheel of Misfortune color", dashboardString);
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
