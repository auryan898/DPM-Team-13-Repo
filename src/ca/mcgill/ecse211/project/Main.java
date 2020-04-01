package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;
import static ca.mcgill.ecse211.test.TestResources.navigation;
import static ca.mcgill.ecse211.test.TestResources.odometry;
import static ca.mcgill.ecse211.test.TestResources.pilot;

import ca.mcgill.ecse211.project.menu.MainMenu;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;

/**
 * The main class that includes basic initialization methods for initializing
 * navigation, odometry, and the robot pilot, along with sensors
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Main {
  // Every user made thread should define its own sleep interval
  private static final long T_INTERVAL = 10;

  public static void main(String[] args) {
    // Initializing code goes here.
    MainMenu mainMenu = MainMenu.createMainMenu();

    // Start main thread
    while (!END_PROGRAM) {
      END_PROGRAM = mainMenu.select();

      try {
        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

  }
}
