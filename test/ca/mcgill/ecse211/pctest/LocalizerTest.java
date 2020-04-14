package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;
import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.Localizer;
import ca.mcgill.ecse211.project.Navigation;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import org.junit.Test;

/**
 * This is a JUnit test for Localization logic. This test can be run
 * individually or along with all of the tests using {@link RunAllTests}
 * 
 * <p>
 * Note: This test is designed to be run on a PC/Computer, not an EV3.
 * 
 * @see ca.mcgill.ecse211.project.Localizer
 */
public class LocalizerTest {

  /**
   * Checking that getRange() has good float-precision.
   */
  @Test
  public void testGetRangeFloatPrecision() {
    Float actual;

    // Threshold is narrower
    actual = testClass.getRange(2, 1);
    assertEquals(100.0, actual, 0.0001);

    // Threshold is wider
    actual = testClass.getRange(2, 1);
    assertEquals(100.0, actual, 0.01);

    // Range is larger
    actual = testClass.getRange(1, 2);
    assertEquals(-100.0, actual, 0.0001);

    actual = testClass.getRange(1, 2);
    assertEquals(-100.0, actual, 0.001);
  }

  /**
   * Checking that getRange() does not return null values.
   */
  @Test
  public void testGetRangeNonNull() {
    Float actual;
    // Range is larger
    actual = testClass.getRange(1, 2);
    assertNotNull(actual);

    actual = testClass.getRange(1, 2);
    assertEquals(-100.0, actual, 0.01);
  }

  /**
   * Tests scanDist(float startAngle, float angleAmount, float
   * intervalAngle,
   * double speed, float threshold,float range) tests
   */

  @Test
  // Scandist
  public void testScanDist() {

    RangeReadings returned = testClass.scanDist(0, 10, 10, 1.0, 2, 1);
    assertNotNull(returned);
  }
}

/**
 * A class housing testable localization methods.
 * 
 * @author Gorkem Yalcinoz
 */
class testClass {
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
  public static RangeReadings scanDist(float startAngle, float angleAmount, float intervalAngle,
      double speed, float threshold, float range) {

    intervalAngle = Navigation.absoluteHeading(intervalAngle);
    // turn to first heading
    // navigation.turnTo(startAngle);

    // loop for each interval
    float accHeading = 0;
    RangeReadings readings = new RangeReadings(0);
    // double oldSpeed = pilot.getAngularSpeed();
    // pilot.setAngularSpeed(speed);
    while (accHeading < angleAmount) {
      // pilot.rotate(intervalAngle);

      accHeading += intervalAngle;
      range = getRange(threshold, range); // Modified this line in order to suit it to the test
                                          // method.
      readings.add(new RangeReading(Navigation.absoluteHeading(accHeading + startAngle), range));

      try {
        Thread.sleep(Localizer.getDistReadInterval());
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }

    }
    // pilot.setAngularSpeed(oldSpeed);

    return readings;
  }

}
