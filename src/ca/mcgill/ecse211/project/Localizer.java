package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;
import static ca.mcgill.ecse211.project.Navigation.absoluteHeading;
import java.util.ArrayList;
import java.util.List;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;

/**
 * Base localizer class that can read distances, color light values,
 * and has a distance scanning method.
 * 
 * <p>
 * See section 3.3 Localization and Navigation and the sequence diagram from
 * section 4.0 Thread
 * Layout, both from the "Software Documentation Final" document, for a
 * description of how the class
 * is used. Note that it is best to use the odometry, pilot, and navigation
 * static instances to
 * control/manage the robot's movement.
 * 
 * <p>
 * See section 5.0 on Recommendations for an overview of Continuous Localization
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
   * @param  startAngle    starting heading
   * @param  angleAmount   ending heading
   * @param  intervalAngle angle to stop at each interval
   * @param  speed         the angular speed for each interval turn
   * @param  threshold     the maximum cm distance to read, invalid is value -1
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

  /**
   * Assumes that the robot is positioned in a corner of two walls on the playing
   * field, between the corner and the grid point (1,1).
   * 
   * @return true if it runs successfully, false if there was an unexpected error
   *         that comes up
   */
  public boolean localizeInCorner() {
    // Do a full circle scan

    pilot.setAngularAcceleration(70);
    pilot.setLinearAcceleration(70);
    setOdometryAngle(90);
    RangeReadings readings = scanDist(90, 360, 10, 100, 50);

    // Find a closest point
    float angle = findMinAngle(readings);

    // Turn to the right side wall
    display.writeNext("" + angle);

    if (readings.getRange(absoluteHeading(angle + 90)) > 0
        && readings.getRange(absoluteHeading(angle + 90)) < 1.5 * TILE_WIDTH) {
      navigation.turnTo((double) angle + 90);
    } else if (readings.getRange(absoluteHeading(angle - 90)) > 0
        && readings.getRange(absoluteHeading(angle - 90)) < 1.5 * TILE_WIDTH) {
      // minimum angle is on right wall
      navigation.turnTo((double) angle);
    } else {
      return false;
    }
    display.writeNext("" + odometry.getPose().toString());

    // Reset the angle and scan the closest wall for the closest point
    setOdometryAngle(0);
    readings = scanDist(-30, 60, 5, 15, 50);
    angle = findMinAngle(readings);
    navigation.turnTo(angle);
    setOdometryAngle(0);

    // Moving to point (1,1)
    pilot.travel((double) getRange(100) - TILE_WIDTH);
    pilot.rotate(-90);
    pilot.travel((double) getRange(100) - TILE_WIDTH);
    pilot.rotate(-90);

    // Set position to (1,1) and forward angle to 90 deg
    setOdometryPos(1 * TILE_WIDTH, 1 * TILE_WIDTH);
    setOdometryAngle(90);

    return true;
  }

  /**
   * Command the robot to position itself on top of a grid point of the playing
   * field grid.
   * 
   * <p>
   * The robot first scans the area using just one color sensor, to detect where
   * the lines of the grid are with respect to the robot's heading. Then the robot
   * does a positional correction, moving closer to the grid point. If the
   * movement was deemed accurate, then it will correct the heading as it is now,
   * otherwise it will continue to correct the position until it is accurate
   * enough.
   * 
   */
  public void localizeAtGridLines() {
    Pose oldPose = getOdometryPos();
    final float oldX = oldPose.getX();
    final float oldY = oldPose.getY();
    final double oldAngular = pilot.getAngularSpeed();
    final double oldLinear = pilot.getLinearSpeed();
    final float oldAngle = oldPose.getHeading();

    pilot.setAngularAcceleration(70);
    pilot.setAngularSpeed(70);
    pilot.setLinearSpeed(15);
    float color;
    float angle;
    ArrayList<Float> manyAngles = null;
    int count = 0;
    while (manyAngles == null || manyAngles.size() != 4) {
      // Positional correction (vectors)
      manyAngles = new ArrayList<>();
      if (count % 2 == 1) {
        pilot.rotate(360 * 1, true);
      } else {
        pilot.rotate(-360 * 1);
      }
      while (pilot.isMoving()) { //
        color = getLeftLightValue();
        if (color < .3f) {
          while (color < .3f && pilot.isMoving()) {
            color = getLeftLightValue();
          }
          pilot.stop();
        }
      }
      if (count % 2 == 1) {
        pilot.rotate(370 * 1, true);
      } else {
        pilot.rotate(-370 * 1);
      }
      while (pilot.isMoving()) { // collect samples
        color = getLeftLightValue();
        if (color < .3f) {
          float angle0 = absoluteHeading(getOdometryPos().getHeading());
          while (color < .3f && pilot.isMoving()) {
            color = getLeftLightValue();
          } // save angle once line is no longer dark
          angle = absoluteHeading(getOdometryPos().getHeading());
          angle = (angle0 + angle) / 2;
          manyAngles.add(angle);
        }
      }
      // Compensate with vector displacement from origin point
      float[] vector = vectorize(manyAngles, 5.8f);
      if (Math.abs(vector[0]) > 1 || Math.abs(vector[1]) >= 1) {
        Pose p = getOdometryPos();
        navigation.travelTo(p.getX() - vector[0], p.getY() - vector[1]);
      }
      count++;
    }

    float originalAngle = getOdometryPos().getHeading();
    // Correct angle offset
    // pilot.rotate(10, false);
    try {
      pilot.rotate(360 * 1, true);
      Float[] detectedAngles = new Float[4];
      while (pilot.isMoving()) {
        color = getLeftLightValue();
        if (color < .30f) {
          float angle0 = absoluteHeading(getOdometryPos().getHeading());
          while (color < .3f && pilot.isMoving()) {
            color = getLeftLightValue();
          } // save angle once line is no longer dark
          angle = absoluteHeading(getOdometryPos().getHeading());
          angle = angle0;
          detectedAngles[getLineZone(angle)] = angle;
        }
      }

      // Math to determine angle correction offset
      float offset1 = offsetCalc(detectedAngles[1], detectedAngles[3]) % 90;
      float offset2 = offsetCalc(detectedAngles[0], detectedAngles[2]) % 90;
      offset1 = offset1 >= 45 ? offset1 - 90 : offset1;
      offset2 = offset2 >= 45 ? offset2 - 90 : offset2;

      // Take the average of the offsets
      offset1 = (offset1 + offset2) / 2;

      // Turn to the proper old angle
      navigation.turnTo(originalAngle + offset1 - 1);
      setOdometryAngle(originalAngle);
    } catch (NullPointerException e) {
    }

    setOdometryPos(oldX, oldY);
    navigation.turnTo(oldAngle);

    pilot.setAngularSpeed(oldAngular);
    pilot.setLinearSpeed(oldLinear);
  }

  /**
   * Gets the midpoint angle between two given angles.
   * 
   * @param  angle1 first angle
   * @param  angle2 second angle
   * @return        the midpoint
   */
  public float offsetCalc(float angle1, float angle2) {
    float theta2 = Math.max(angle1, angle2);
    float theta1 = Math.min(angle1, angle2);

    return (theta2 - theta1) / 2 + theta1;
  }

  /**
   * Adds a series of vectors together to form one resultant vector. All of these
   * vectors have the same length "dist", but have different angles "manyAngles".
   * This method is intended for use in the localizeAtGridPoint() method.
   * 
   * @param  manyAngles an array of angles of each of the vectors
   * @param  dist       the length of the vectors, which is the same for all
   * @return            an array of the form [x,y], representing the vector (x,y)
   */
  private float[] vectorize(List<Float> manyAngles, float dist) {
    float[] res = { 0, 0 };
    if (manyAngles.size() >= 5) {
      manyAngles = manyAngles.subList(0, 4);
    }
    for (Float val : manyAngles) {
      res[0] += dist * Math.cos(Math.toRadians(val));
      res[1] += dist * Math.sin(Math.toRadians(val));
    }
    return res;
  }

  /**
   * Gives an integer value, denoting the zone of an angle that is given. These
   * zones are defined as follows:
   * 
   * <pre>
   * Zone 0: 235 deg to  45 deg
   * Zone 1:  45 deg to 135 deg
   * Zone 2: 135 deg to 225 deg
   * Zone 3: 225 deg to 315 deg
   * </pre>
   * 
   * @param  angle the angle for which we will determine the zone
   * @return       the zone integer
   */
  private int getLineZone(float angle) {
    angle = absoluteHeading(angle);
    if (45 <= angle && angle < 135) {
      return 1;
    } else if (135 <= angle && angle < 225) {
      return 2;
    } else if (225 <= angle && angle < 315) {
      return 3;
    } else {
      return 0;
    }
  }

  /**
   * Find the minimum angle in a set of RangeReadings.
   * 
   * @param  readings the angles and ranges around the robot
   * @return          the minimum angle
   */
  public float findMinAngle(RangeReadings readings) {
    // find minimum angle
    int min = 0;
    for (int i = 1; i < readings.size(); i++) {
      if (readings.getRange(min) < 0
          || (readings.getRange(min) > readings.getRange(i) && readings.getRange(i) >= 0)) {
        min = i;
      }
    } // Found one of the minimum distances
    return readings.getAngle(min);
  }

  /**
   * This method is called whenever the robot begins a movement. These movements
   * include rotations, and forward or backwards movements.
   * 
   * <p>
   * This method is intended to be used for Continuous Localization.
   */
  public void moveStarted(Move event, MoveProvider mp) {
    // TODO: Fill this provided moveListener method to localize and alter the
    // robot's position with each movement
    // Continuous Localization
  }

  /**
   * This method is called whenever the robot finishes a movement. These movements
   * include rotations, and forward or backwards movements.
   * 
   * <p>
   * This method is intended to be used for Continuous Localization.
   */
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
  public static void setDistReadInterval(long distReadInterval) {
    Localizer.distReadInterval = distReadInterval;
  }
}
