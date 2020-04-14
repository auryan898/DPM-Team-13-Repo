package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;
import org.junit.Test;
import ca.mcgill.ecse211.project.Localizer;
import ca.mcgill.ecse211.project.Navigation;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.SampleProvider;
import static ca.mcgill.ecse211.project.LocalResources.*;

/*
 * To test the software logic without robot, our team split the methods into logic 
 * and readings. Later, we tested the logic using Junit. Whenever we could not avoid a statement that needs hardware, we commented out the line.
 * Any methods under testClasses are for debugging purposes and should not be modified. Otherwise, there maybe unexpected change in test results.
 */
public class LocalizerTest {

  /*
   * getRange(float threshold, float range) unit tests
   */
  
  //Success cases
  @Test
  public void testLocalizerGetRangeFinite_success() {
    //Threshold is larger
    float actual = testClass.getRange(2, 1);
    assertEquals(100.0, actual, 0.0001);
  }
  @Test
  public void testLocalizerGetRangeFinite2_success() {
    //Range is larger
    float actual = testClass.getRange(1, 2);
    assertEquals(-100.0, actual, 0.0001);
  }
  @Test
  public void testLocalizerGetRangeFinite3_success() {
    //Range is larger
    float actual = testClass.getRange(1, 2);
    assertNotNull(actual);
  }
  
  //Fail cases
  @Test
  public void testLocalizerGetRangeFinite_fail() {
    float actual = testClass.getRange(2, 1);
    assertNotEquals(100.0, actual,0.001);
  }
  @Test
  public void testLocalizerGetRangeFinite2_fail() {
    float actual = testClass.getRange(2, 1);
    assertNotEquals(100.0, actual,0.01);
  }
  @Test
  public void testLocalizerGetRangeFinite3_fail() {
    float actual = testClass.getRange(1, 2);
    assertNotEquals(-100.0, actual,0.01);
  }
  @Test
  public void testLocalizerGetRangeFinite4_fail() {
    float actual = testClass.getRange(1, 2);
    assertNotEquals(-100.0, actual,0.001);
  }
 
  /*
   * RangeReadings scanDist(float startAngle, float angleAmount, float intervalAngle,
        double speed, float threshold,float range) tests
   */
  
  @Test
  //Scandist
  public void testScanDist_success() {
    
    RangeReadings returned = testClass.scanDist(0, 10, 10, 1.0, 2 ,1);
    assertNotNull(returned);
  }
  
  
  
}
  
   //For debugging, we moved some methods here.
  class testClass {
    
    /**
     * Gets the range detected by the ultrasonic sensor, within a given threshold.
     * FOR TEST PURPOSE ONLY.
     * 
     * @param  threshold the cm threshold of the range sensor
     * @param range detected by ultrasonic sensor
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
        double speed, float threshold,float range) {

      intervalAngle = Navigation.absoluteHeading(intervalAngle);
      // turn to first heading
     // navigation.turnTo(startAngle);
      
      // loop for each interval
      float accHeading = 0;
      RangeReadings readings = new RangeReadings(0);
     // double oldSpeed = pilot.getAngularSpeed();
     // pilot.setAngularSpeed(speed);
      while (accHeading < angleAmount) {
      //  pilot.rotate(intervalAngle);

        accHeading += intervalAngle;
        range = getRange(threshold,range); //Modified this line in order to suit it to the test method.
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
