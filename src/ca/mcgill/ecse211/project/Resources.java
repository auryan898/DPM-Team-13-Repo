package ca.mcgill.ecse211.project;

import com.auryan898.dpm.lejoscomm.BasicComm;
import com.auryan898.dpm.lejoscomm.StringData;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
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
public class Resources {
  
  /**
   * The wrapper class for the text display, much easier to use.
   */
  public static Display display = null;
  
  /**
   * The text display.
   */
  public static TextLCD lcd = null;
  
  /**
   * The ev3 instance.
   */
  public static EV3 ev3 = null;
  
  
  /**
   * The width of the robot's base, the distance between the wheels, that touches the ground.
   */
  public static float BASE_WIDTH = 11.25f;
  
  /**
   * Tile width.
   */
  public static float TILE_WIDTH = 31.25f;
  
  
  /**
   * The navigator that will direct the robot to certain waypoints.
   */
  public static Navigation navigation = null;
  
  /**
   * The localizing class, simplified and optimized, derived from the last lab.
   */
  public static Localizer localizer = null;
  
  /**
   * The ultrasonic sensor device.
   */
  public static EV3UltrasonicSensor ultrasonicSensorDevice;
  
  /**
   * The color sensor device.
   */
  public static EV3ColorSensor colorSensorDevice;
  
  /**
   * The pilot that drives the robot.
   */
  public static MovePilot pilot;
  
  /**
   * The odometry tracker.
   */
  public static OdometryPoseProvider odometry;
  
  /**
   * Communications object to send data to computer for debugging.
   */
  public static BasicComm comm = null;
  
  /**
   * Ensures the heading is [0,360) degrees instead of [-180,180].
   * It will work even if input is 0-360.
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
   * @return the position as a Pose object
   */
  public static Pose getOdometryPos() {
    return odometry.getPose();
  }
  
  /**
   * Debugging statement, that sends data to the pc.
   */
  public static void debug(String line) {
    if (comm != null && comm.isConnected()) {
      comm.send("Print", new StringData(line));
    }
  }
}
