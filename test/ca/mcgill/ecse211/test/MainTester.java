package ca.mcgill.ecse211.test;

import static ca.mcgill.ecse211.test.TestResources.*;

import ca.mcgill.ecse211.project.Display;
import ca.mcgill.ecse211.project.Localizer;
import ca.mcgill.ecse211.project.Navigation;
import ca.mcgill.ecse211.project.menu.MainMenu;
import ca.mcgill.ecse211.tools.SubMenu;
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
public class MainTester {
  // Every user made thread should define its own sleep interval
  private static final long T_INTERVAL = 10;
  public static double WHEEL_RADIUS = 4.4;

  public static void main(String[] args) {
    // Initializing code goes here.
    initRobot(WHEEL_RADIUS,BASE_WIDTH,Motor.A,Motor.D);
    
    SubMenu testsMenu = TestsMenu.createTests();
    while (!END_PROGRAM) {
      END_PROGRAM = testsMenu.select();

      try {

        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

  }

  public static void initRobot(double wheelRadius, double baseWidth, NXTRegulatedMotor motorLeft,
      NXTRegulatedMotor motorRight) {
    // The motors as "Wheels" with radii and position
    Wheel wheelLeft = WheeledChassis.modelWheel(motorLeft, wheelRadius).offset(baseWidth / 2);
    Wheel wheelRight = WheeledChassis.modelWheel(motorRight, wheelRadius).offset(-baseWidth / 2);

    // Chassis combines those wheels together to make a vehicle
    Chassis chassis = new WheeledChassis(new Wheel[] { wheelLeft, wheelRight },
        WheeledChassis.TYPE_DIFFERENTIAL);

    // Navigation combines odometry and pilot to move the robot
    navigation = new Navigation(chassis);

    // pilot directs the robot's movements and speed, controlling both motors
    pilot = navigation.getPilot();
    
    // odometry tracks the movements performed by the pilot
    odometry = navigation.getOdometry();
  }
}
