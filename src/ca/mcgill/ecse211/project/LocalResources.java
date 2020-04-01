package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;

/**
 * Static resources available to all classes via the import line:
 * import static ca.mcgill.ecse211.project.Resources.*;
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class LocalResources {

  // --------------------Changeable Robot Parameters-----------------------

  /**
   * Radius of each wheel.
   */
  public static float WHEEL_RADIUS = 4.4f;

  /**
   * The width of the robot's base, the distance between the point where they
   * touch the ground.
   */
  public static float BASE_WIDTH = 9.88f;

  /**
   * Tile width.
   */
  public static float TILE_WIDTH = 31.25f;

  /**
   * Ultrasonic sensor offset.
   */
  public static float US_OFFSET = (float) (0.0665); // in meters

  // --------------------Useful Robot Control Objects----------------------
  /**
   * The wrapper class for the text display, much easier to use.
   */
  public static Display display = new Display();

  /**
   * The localizing class, simplified and optimized, derived from the last lab.
   */
  public static Localizer localizer = new Localizer();

  /**
   * The navigator that will direct the robot to certain waypoints.
   */
  public static Navigation navigation;

  /**
   * The pilot that drives the robot.
   */
  public static MovePilot pilot;

  /**
   * The odometry tracker.
   */
  public static OdometryPoseProvider odometry;

  // --------------------Lower-Level Control Objects-----------------------
  /**
   * The text display.
   */
  public static TextLCD lcd = LocalEV3.get().getTextLCD();

  /**
   * The ultrasonic sensor device.
   */
  public static SampleProvider ultrasonicSensor = (new EV3UltrasonicSensor(SensorPort.S1))
      .getDistanceMode();

  /**
   * The color sensor device.
   */
  public static SampleProvider colorSensor = (new EV3ColorSensor(SensorPort.S2)).getRGBMode();
  public static SampleProvider lineDetectLeft = (new EV3ColorSensor(SensorPort.S3)).getRedMode();
  public static SampleProvider lineDetectRight = (new EV3ColorSensor(SensorPort.S4)).getRedMode();

  /**
   * The Left and Right motors.
   */
  public static RegulatedMotor motorLeft = Motor.A;
  public static RegulatedMotor motorRight = Motor.D;

  /**
   * a boolean to decide when to end the whole program.
   */
  public static boolean END_PROGRAM = false;

  // -------------Static Methods That Can Be Used Anywhere-----------------
  static {
    navigation = initNavigation(WHEEL_RADIUS, BASE_WIDTH, motorLeft, motorRight);
    pilot = navigation.getPilot();
    odometry = navigation.getOdometry();
    navigation.addMoveListener(localizer);
  }

  public static Navigation initNavigation(float wheelRadius, float baseWidth,
      RegulatedMotor motorLeft,
      RegulatedMotor motorRight) {

    // The motors as "Wheels" with radii and position
    Wheel wheelLeft = WheeledChassis.modelWheel(motorLeft, wheelRadius).offset(baseWidth / 2);
    Wheel wheelRight = WheeledChassis.modelWheel(motorRight, wheelRadius).offset(-baseWidth / 2);

    // Chassis combines those wheels together to make a vehicle
    Chassis chassis = new WheeledChassis(new Wheel[] { wheelLeft, wheelRight },
        WheeledChassis.TYPE_DIFFERENTIAL);

    // Navigation combines odometry and pilot to move the robot
    Navigation nav = new Navigation(chassis);
    return nav;
  }

  /**
   * Ensures the heading is [0,360) degrees instead of [-180,180].
   * It will work even if input is 0-360.
   * 
   * @return
   */
  public static float absoluteHeading(float heading) {
    return (heading + 360) % 360; // [0-360)
  }

  /**
   * Ensures the heading is between (-180,180], instead of [0,360).
   */
  public static float negativeHeading(float heading) {
    float theta = absoluteHeading(heading) - 180;
    return theta == -180 ? 180 : theta;
  }

  /**
   * Resets the heading of the odometer to an angle.
   * 
   * @param angle the new heading
   */
  public static void setOdometryAngle(float angle) {
    Pose pose = odometry.getPose();
    pose.setHeading(angle);
    odometry.setPose(pose);
  }

  /**
   * Resets the heading of the odometer a specified point.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public static void setOdometryPos(float x, float y) {
    Pose pose = odometry.getPose();
    pose.setLocation(x, y);
    odometry.setPose(pose);
  }

  /**
   * Gets the position of the robot.
   * 
   * @return the position as a Pose object
   */
  public static Pose getOdometryPos() {
    return odometry.getPose();
  }
}
