package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.mcgill.ecse211.project.Navigation;

/**
 * This is a JUnit test for Navigation math and logic. This test can be run
 * individually or along with all of the tests using {@link RunAllTests}
 * 
 * <p>
 * Note: This test is designed to be run on a PC/Computer, not an EV3.
 * 
 * @see ca.mcgill.ecse211.project.Navigation
 */
public class NavigationTest {

  /**
   * Tests
   * {@link ca.mcgill.ecse211.project.Navigation#minimumAngleToHeading(float, float)}.
   */
  @Test
  public void testMinimumAngle() {
    assertEquals(179, Navigation.minimumAngleToHeading(0, 181), 0.001);
    assertEquals(-179, Navigation.minimumAngleToHeading(0, 179), 0.001);
    assertEquals(180, Navigation.minimumAngleToHeading(0, -180), 0.001);

  }

  /**
   * Tests
   * {@link ca.mcgill.ecse211.project.Navigation#absoluteHeading(float)}.
   */
  @Test
  public void testAbsoluteHeading() {
    assertEquals(180, Navigation.absoluteHeading(180), 0.001);
    assertEquals(180, Navigation.absoluteHeading(-180), 0.001);
    assertEquals(340, Navigation.absoluteHeading(-20), 0.001);
  }

  /**
   * Tests
   * {@link ca.mcgill.ecse211.project.Navigation#negativeHeading(float)}.
   */
  @Test
  public void testNegativeHeading() {
    assertEquals(-90, Navigation.negativeHeading(270), 0.001);
    assertEquals(-179, Navigation.negativeHeading(181), 0.001);
    assertEquals(-1, Navigation.negativeHeading(359), 0.001);
  }
  
  /**
   * Tests
   * {@link ca.mcgill.ecse211.project.Navigation#angleToPoint(double, double, double, double)}.
   */
  @Test
  public void testAngleToPoint() {
    assertEquals(90, Navigation.angleToPoint(0, 0, 0, 1), 0.001);
    // Add more test cases for different angles
  }

  /**
   * Tests
   * {@link ca.mcgill.ecse211.project.Navigation#distanceToPoint(double, double, double, double)}.
   */
  @Test
  public void testDistanceToPoint() {
    assertEquals(5, Navigation.distanceToPoint(0, 0, 3, 4), 0.001);
    // Add more test cases for different distances
  }

}