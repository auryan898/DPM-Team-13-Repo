package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;
import static ca.mcgill.ecse211.project.Navigation.absoluteHeading;
import static ca.mcgill.ecse211.project.Navigation.negativeHeading;

import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.MoveProvider;

/**
 * Base localizer class that can read distances, color light values,
 * and has a distance scanning method.
 * 
 * <p>See section 3.3 Localization and Navigation and the sequence diagram from section 4.0 Thread
 * Layout, both from the "Software Documentation Final" document, for a description of how the class
 * is used. Note that it is best to use the odometry, pilot, and navigation static instances to
 * control/manage the robot's movement.
 * 
 * <p>See section 5.0 on Recommendations for an overview of Continuous Localization
 * and a way in which it could be implemented here.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Localizer implements Runnable, MoveListener {
  private static final long T_INTERVAL = 10;

  private static final float DEFAULT_DISTANCE_TO_DETECT = 0;

  private float[] lightLeftSample = new float[3];
  private float[] lightRightSample = new float[3];
  private float[] distSample = new float[1];
  private static long distReadInterval = 20;

  /**
   * The implemented run method for usage in a Thread.
   */
  public void run() {
    while (!END_PROGRAM) {
      // Place continuous localizer code here to periodically update useful values:
      getLeftLightValue();
      getRightLightValue();
      getRange(DEFAULT_DISTANCE_TO_DETECT);
      // End localizer code.
      try {
        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }
  }

  /**
   * Fetches the color sensor sample and returns the array.
   * 
   * @return the lightSample array size 3 for the possible different modes
   */
  public float getLeftLightValue() {
    lineDetectLeft.fetchSample(lightLeftSample, 0);
    return lightLeftSample[0];
  }

  /**
   * Fetches the color sensor sample and returns the array.
   * 
   * @return the lightSample array size 3 for the possible different modes
   */
  public float getRightLightValue() {
    lineDetectRight.fetchSample(lightRightSample, 0);
    return lightRightSample[0];
  }

  /**
   * Gets the range detected by the ultrasonic sensor, within a given threshold.
   * 
   * @param  threshold the cm threshold of the range sensor
   * @return
   */
  public float getRange(float threshold) {
    ultrasonicSensor.fetchSample(distSample, 0);
    return getRange(threshold, distSample[0]);
  }

  /**
   * Gets the range detected by the ultrasonic sensor, within a given threshold.
   * FOR TEST PURPOSE ONLY.
   * 
   * @param  threshold the cm threshold of the range sensor
   * @param  range     detected by ultrasonic sensor
   * @return
   */
  public static float getRange(float threshold, float range) {
    if (range > threshold || Float.isInfinite(range) || Float.isNaN(range)) {
      range = -1;
    } // invalid range value
    return range * 100;
  }

  /**
   * Does a scan starting from one heading, stopping at regular intervals and
   * waiting, until it reaches another heading.
   * 
   * @param  startAngle    - starting heading
   * @param  angleAmount   - ending heading
   * @param  intervalAngle - angle to stop at each interval
   * @param  speed         - the angular speed for each interval turn
   * @param  threshold     - the maximum cm distance to read, invalid is value -1
   * @return               the distance detected at each degree interval, starting
   *                       from 0
   */
  public RangeReadings scanDist(float startAngle, float angleAmount, float intervalAngle,
      double speed, float threshold) {

    intervalAngle = absoluteHeading(intervalAngle);
    // turn to first heading
    navigation.turnTo(startAngle);

    // loop for each interval
    float accHeading = 0;
    RangeReadings readings = new RangeReadings(0);
    double oldSpeed = pilot.getAngularSpeed();
    pilot.setAngularSpeed(speed);
    while (accHeading < angleAmount) {
      pilot.rotate(intervalAngle);

      accHeading += intervalAngle;
      float range = getRange(threshold);
      readings.add(new RangeReading(absoluteHeading(accHeading + startAngle), range));

      try {
        Thread.sleep(getDistReadInterval());
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }
    pilot.setAngularSpeed(oldSpeed);

    return readings;
  }

  // TODO: **Include localize in place methods

  @Override
  public void moveStarted(Move event, MoveProvider mp) {
    // TODO: Fill this provided moveListener method to localize and alter the
    // robot's position with each movement
    // Continuous Localization
  }

  @Override
  public void moveStopped(Move event, MoveProvider mp) {
    // TODO: Fill this provided moveListener method to localize and alter the
    // robot's position with each movement
    // Continuous Localization
  }

  /**
   * Returns the currently assigned millisecond delay period for the scanDist
   * method.
   * 
   * @return millisecond delay time
   */
  public static long getDistReadInterval() {
    return distReadInterval;
  }

  /**
   * Sets the millisecond delay period for the scanDist method.
   * 
   * @param distReadInterval millisecond delay time
   */
  public void setDistReadInterval(long distReadInterval) {
    this.distReadInterval = distReadInterval;
  }
}
