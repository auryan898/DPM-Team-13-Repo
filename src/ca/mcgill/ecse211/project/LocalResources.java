package ca.mcgill.ecse211.project;

import ca.mcgill.ecse211.playingfield.Point;
import ca.mcgill.ecse211.playingfield.Region;
import ca.mcgill.ecse211.wificlient.WifiConnection;
import java.math.BigDecimal;
import java.util.Map;
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
 * <p>
 * This class stores information for the robot (eg, BASE_WIDTH) and contains the
 * object
 * instances of various tools, such as a Search object, Attachment, etc. This
 * class also contains
 * the methods and information necessary to update its game parameters via wifi.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class LocalResources {

  // --------------------Adjustable Wifi Settings--------------------------
  /**
   * The default server IP used by the profs and TA's.
   */
  public static final String DEFAULT_SERVER_IP = "192.168.2.3";

  /**
   * The IP address of the server that transmits data to the robot. For the beta
   * demo and
   * competition, replace this line with
   * 
   * <p>
   * {@code public static final String SERVER_IP = DEFAULT_SERVER_IP;}
   */
  public static final String SERVER_IP = "192.168.2.3"; // = DEFAULT_SERVER_IP;

  /**
   * Your team number.
   */
  public static final int TEAM_NUMBER = 13;

  /**
   * Enables printing of debug info from the WiFi class.
   */
  public static final boolean ENABLE_DEBUG_WIFI_PRINT = false;

  /**
   * Enable this to attempt to receive Wi-Fi parameters at the start of the
   * program.
   */
  public static final boolean RECEIVE_WIFI_PARAMS = true;

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
   * The searching class.
   */
  public static Search search = new Search();

  /**
   * The attachment class.
   */
  public static Attachment lift = new Attachment();

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

  /** The color sensor device. */
  public static SampleProvider colorSensor = (new EV3ColorSensor(SensorPort.S2)).getRGBMode();

  /** The left line detector. */
  public static SampleProvider lineDetectLeft = (new EV3ColorSensor(SensorPort.S3)).getRedMode();

  /** The right line detector. */
  public static SampleProvider lineDetectRight = (new EV3ColorSensor(SensorPort.S4)).getRedMode();

  /** the left motor. */
  public static RegulatedMotor motorLeft = Motor.A;

  /** the left motor. */
  public static RegulatedMotor motorRight = Motor.D;

  /** a boolean to decide when to end the whole program. */
  public static boolean END_PROGRAM = false;

  // -------------Static Methods That Can Be Used Anywhere-----------------

  /**
   * Container for the Wi-Fi parameters.
   */
  public static Map<String, Object> wifiParameters;

  static {
    navigation = initNavigation(WHEEL_RADIUS, BASE_WIDTH, motorLeft, motorRight);
    pilot = (MovePilot) navigation;
    odometry = navigation.getOdometry();
    navigation.addMoveListener(localizer);

    // This static initializer MUST be declared before any Wi-Fi parameters.
    /**
     * Use FieldLayouts to load offline versions of the wifiParameters, instead of
     * the usual call to receiveWifiParameters(). This is useful if the user cannot
     * run the laptop/pc server for some reason, but still wishes to test features
     * that rely on playing field parameters in the code.
     * 
     * Example:
     * { @code FieldLayouts.getPlayingFieldLayout(resource_path)}
     * 
     */
    // wifiParameters =
    // FieldLayouts.getLayout("ca.mcgill.ecse211.project.fieldlayout1");
    receiveWifiParameters();
  }

  /** Red team number. */
  public static int redTeam = getWP("RedTeam");

  /** Red team's starting corner. */
  public static int redCorner = getWP("RedCorner");

  /** Green team number. */
  public static int greenTeam = getWP("GreenTeam");

  /** Green team's starting corner. */
  public static int greenCorner = getWP("GreenCorner");

  /** The Red Zone. */
  public static Region red = makeRegion("Red");

  /** The Green Zone. */
  public static Region green = makeRegion("Green");

  /** The Island. */
  public static Region island = makeRegion("Island");

  /** The red tunnel footprint. */
  public static Region tnr = makeRegion("TNR");

  /** The green tunnel footprint. */
  public static Region tng = makeRegion("TNG");

  /** The red search zone. */
  public static Region szr = makeRegion("SZR");

  /** The green search zone. */
  public static Region szg = makeRegion("SZG");

  /**
   * Receives Wi-Fi parameters from the server program.
   */
  public static void receiveWifiParameters() {
    // Only initialize the parameters if needed
    if (!RECEIVE_WIFI_PARAMS || wifiParameters != null) {
      return;
    }
    System.out.println("Waiting to receive Wi-Fi parameters.");

    // Connect to server and get the data, catching any errors that might occur
    try (
        WifiConnection conn = new WifiConnection(SERVER_IP, TEAM_NUMBER, ENABLE_DEBUG_WIFI_PRINT)) {
      /*
       * getData() will connect to the server and wait until the user/TA presses the
       * "Start" button in the GUI on their laptop with the data filled in. Once it's
       * waiting, you can kill it by pressing the back/escape button on the EV3.
       * getData() will throw exceptions if something goes wrong.
       * 
       */

      wifiParameters = conn.getData();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  /**
   * Returns the Wi-Fi parameter int value associated with the given key.
   * 
   * @param  key the Wi-Fi parameter key
   * @return     the Wi-Fi parameter int value associated with the given key
   */
  public static int getWP(String key) {
    if (wifiParameters != null) {
      return ((BigDecimal) wifiParameters.get(key)).intValue();
    } else {
      return 0;
    }
  }

  /**
   * Makes a point given a Wi-Fi parameter prefix.
   */
  public static Point makePoint(String paramPrefix) {
    return new Point(getWP(paramPrefix + "_x"), getWP(paramPrefix + "_y"));
  }

  /**
   * Makes a region given a Wi-Fi parameter prefix.
   */
  public static Region makeRegion(String paramPrefix) {
    return new Region(makePoint(paramPrefix + "_LL"), makePoint(paramPrefix + "_UR"));
  }

  /**
   * Creates a Navigation object given a wheel radius, base width, and two motors.
   * 
   * @param  wheelRadius wheel radius in cm
   * @param  baseWidth   base width in cm
   * @param  motorLeft   leJOS motor
   * @param  motorRight  leJOS motor
   * @return             new Navigation instance
   */
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
