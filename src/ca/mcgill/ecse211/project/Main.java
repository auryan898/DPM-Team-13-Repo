package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import ca.mcgill.ecse211.project.tools.MenuDisplay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
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
  private static final long T_INTERVAL = 10;
  public static double WHEEL_RADIUS = MovePilot.WHEEL_SIZE_EV3;

  public static void main(String[] args) {
    initRobot();
    initNavigation();
    
    MenuDisplay menu = new MenuDisplay();
    Thread display = new Thread(menu);
    // Thread display = new Thread(display);
    display.start(); // start this thread or the display thread, pick one.

    // Initializing code goes here.
    while (!END_PROGRAM) {
      // Looped code goes here.
      
      try {
        // Every user made thread should define its own sleep interval
        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

  }

  public static void initRobot() {
    ev3 = LocalEV3.get();
    lcd = ev3.getTextLCD();
    display = new Display();
  }

  public static void initNavigation() {
    // Drive control
    Wheel wheelLeft = WheeledChassis.modelWheel(motorLeft, WHEEL_RADIUS).offset(-BASE_WIDTH / 2);
    Wheel wheelRight = WheeledChassis.modelWheel(motorRight, WHEEL_RADIUS).offset(BASE_WIDTH / 2);
    Chassis chassis = new WheeledChassis(new Wheel[] { wheelLeft, wheelRight }, WheeledChassis.TYPE_DIFFERENTIAL);

    pilot = new MovePilot(chassis); // global resource
    odometry = new OdometryPoseProvider(pilot); // global resource

    navigation = new Navigation(); // global resource, by group 23
  }

  public static void initSensor() {
    // Sensor Declaration
    ultrasonicSensorDevice = new EV3UltrasonicSensor(SensorPort.S1); // global resource
    colorSensorDevice = new EV3ColorSensor(SensorPort.S2); // global resource
  }
}
