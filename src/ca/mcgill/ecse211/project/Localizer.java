package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;

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
 * <p>
 * It is best to use the odometry, pilot, and navigation static
 * instances to control/manage the robot's movement.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Localizer implements Runnable, MoveListener {
  private static final long T_INTERVAL = 10;

  private float[] lightLeftSample = new float[3];
  private float[] lightRightSample = new float[3];
  private float[] distSample = new float[1];
  private long distReadInterval = 20;

  /**
   * The implemented run method for usage in a Thread.
   */
  public void run() {
    while (!END_PROGRAM) {
      // Place localizer thread code here:

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
    float range = distSample[0];
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
        Thread.sleep(distReadInterval);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }
    pilot.setAngularSpeed(oldSpeed);

    return readings;
  }

  @Override
  public void moveStarted(Move event, MoveProvider mp) {
    // TODO: Fill this provided moveListener method to localize and alter the
    // robot's position with each movement

  }

  @Override
  public void moveStopped(Move event, MoveProvider mp) {
    // TODO: Fill this provided moveListener method to localize and alter the
    // robot's position with each movement

  }
}
