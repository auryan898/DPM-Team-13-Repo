package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;
import org.junit.Test;
import ca.mcgill.ecse211.project.Navigation;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.SampleProvider;
import static ca.mcgill.ecse211.project.LocalResources.*;
/*
 * To test the software logic without robot, our team split the methods into logic 
 * and readings. Later, we tested the logic using Junit. Whenever we could not avoid a statement that needs hardware, we commented out that line.
 * We also modified most of the methods in order to verify only the logic. The future engineering team will need to integrate these tests with hardware.
 * Any methods under testClasses are for debugging purposes and should not be modified. Otherwise, there maybe unexpected change in test results.
 */
public class NavigationTest {
  /*
   * travelTo(double x, double y, double dx, double dy) tests
   * 
   */
  //Success
  @Test
  public void testNavigationTravelto() {
    double[] actuals = testClassNavigation.travelTo(0, 0, 1, 1);
    assertArrayEquals(new double[] {Math.sqrt(2),45}, actuals, 0.0001);
  }
  @Test
  public void testNavigationTravelto2() {
    double[] actuals = testClassNavigation.travelTo(0, 0, 1, 1);
    assertNotNull(actuals);
  }
  @Test
  public void testNavigationTravelto3() {
    double[] actuals = testClassNavigation.travelTo(0, 0, 1, 1);
    assertArrayEquals(new double[] {Math.sqrt(2),45}, actuals, 0.1);
  }
  
  /*
   * turnTo(double theta, int absoluteHeading) tests
   */
  //Success
  @Test
  public void testNavigationTurnTo() {
    float actual = testClassNavigation.turnTo(30,90);
    assertEquals(-60.0,actual,0.0001);
  }
   
  @Test
  public void testNavigationTurnTo2() {
    float actual = testClassNavigation.turnTo(90,30);
    assertEquals(60.0,actual,0.0001);
  }
  @Test
  public void testNavigationTurnTo3() {
    float actual = testClassNavigation.turnTo(90,30);
    assertEquals(60.0,actual,0.1);
  }
  

}
class testClassNavigation {
  /**
   * TEST ONLY.
   * This method causes the robot to travel to the absolute field location (x, y), specified in tile
   * points. This method should continuously call turnTo(double theta) and then set
   * the motor speed to forward (straight). This will make sure that your heading is updated
   * until you reach your exact goal. This method will use the odometer.
   * @param x the x coordinate
   * @param y the y coordinate
   * @param dx delta x
   * @param delta y
   */
  public static double[] travelTo(double x, double y, double dx, double dy) {
    // arctan plus math to add 180 when theta is past 90 deg
    double theta = Math.toDegrees(Math.atan2(dy,dx));
    double distance = Math.sqrt(dx * dx + dy * dy);
   return new double[] {distance, theta};
  }
  
  /**
   * TEST ONLY.
   * This method causes the robot to turn (on point) to the absolute heading theta. This method
   * should turn a MINIMAL angle to its target.
   * @param theta the absolute heading to turn to
   */
  public static float turnTo(double theta, int absoluteHeading) {
    float heading = absoluteHeading;
    float p1 = (float)(theta - heading) % 360;
    float turnAngle = Math.abs(p1) > 180 ? p1 - 360 * Math.signum(p1) : p1;
    return turnAngle;
  }
  
}