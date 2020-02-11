package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.SampleProvider;


/**
 * Base localizer class that can read distances, color light values, 
 * and has a distance scanning method.  
 * 
 * <p>It is best to use the odometry, pilot, and navigation static 
 * instances to control/manage the robot's movement.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Localizer implements Runnable {
  private static final long T_INTERVAL = 10;
  private SampleProvider ultrasensor;
  private SensorMode lightsensor;
  private float[] lightSample;
  private float[] distSample;
  private long distReadInterval = 20;

  /**
   * Initializes the Localizer instance, but also gets the sensor 
   * modes of the light and ultrasonic sensors.
   */
  public Localizer() {
    // offset addition in meters, distance from center to sensor
    ultrasensor = new AdditionFilter(ultrasonicSensorDevice.getDistanceMode(), (float)(0.074));
    lightsensor = colorSensorDevice.getColorIDMode();
    lightSample = new float[3];
    distSample = new float[1];
  }

  /**
   * The implemented run method for usage in a Thread.
   */
  public void run() {
    while (true) {
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
   * @return the lightSample array size 3 for the possible different modes
   */
  public float[] getColor() {
    this.lightsensor.fetchSample(lightSample, 0);
    return lightSample;
  }
  
  /**
   * Gets the range detected by the ultrasonic sensor, within a given threshold.
   * 
   * @param threshold the cm threshold of the range sensor
   * @return
   */
  public float getRange(float threshold) {
    this.ultrasensor.fetchSample(distSample, 0);
    float range = distSample[0];
    if (range > threshold || Float.isInfinite(range) || Float.isNaN(range)) {
      range = -1;
    } // invalid range value
    return range;
  }
  
  /**
   * Does a scan starting from one heading, stopping at regular intervals and
   * waiting, until it reaches another heading.
   * 
   * @param startAngle    - starting heading
   * @param angleAmount   - ending heading
   * @param intervalAngle - angle to stop at each interval
   * @param speed         - the angular speed for each interval turn
   * @param threshold     - the maximum cm distance to read, invalid is value -1
   * @return the distance detected at each degree interval, starting from 0
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
}
