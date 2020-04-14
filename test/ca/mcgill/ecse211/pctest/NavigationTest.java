package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.mcgill.ecse211.project.Navigation;

/**
 * To test the software logic without robot, our team split the methods into
 * logic
 * and readings. Later, we tested the logic using Junit. Whenever we could not
 * avoid a statement that needs hardware, we commented out that line.
 * We also modified most of the methods in order to verify only the logic. The
 * future engineering team will need to integrate these tests with hardware.
 * Any methods under testClasses are for debugging purposes and should not be
 * modified. Otherwise, there maybe unexpected change in test results.
 */
public class NavigationTest {
  /*
   * travelTo(double x, double y, double dx, double dy) tests
   * 
   */
  // Success

  @Test
  public void testMinimumAngle() {
    assertEquals(179, Navigation.minimumAngleToHeading(0, 181), 0.001);
    assertEquals(-179, Navigation.minimumAngleToHeading(0, 179), 0.001);
    assertEquals(180, Navigation.minimumAngleToHeading(0, -180), 0.001);

  }

  @Test
  public void testAbsoluteHeading() {
    assertEquals(180, Navigation.absoluteHeading(180), 0.001);
    assertEquals(180, Navigation.absoluteHeading(-180), 0.001);
    assertEquals(340, Navigation.absoluteHeading(-20), 0.001);
  }

  @Test
  public void testNegativeHeading() {
    assertEquals(-90, Navigation.negativeHeading(270), 0.001);
    assertEquals(-179, Navigation.negativeHeading(181), 0.001);
    assertEquals(-1, Navigation.negativeHeading(359), 0.001);
  }

  @Test
  public void testAngleToPoint() {
    assertEquals(90, Navigation.angleToPoint(0, 0, 0, 1), 0.001);
    // Add more test cases for different angles
  }

  @Test
  public void testDistanceToPoint() {
    assertEquals(5, Navigation.distanceToPoint(0, 0, 3, 4), 0.001);
 // Add more test cases for different distances
  }

}