package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;

import org.junit.Test;

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
  public void testNavigationTravelto() {
    double[] actuals = testClassNavigation.travelTo(0, 0, 1, 1);
    assertArrayEquals(new double[] { Math.sqrt(2), 45 }, actuals, 0.0001);
  }

  @Test
  public void testNavigationTravelto2() {
    double[] actuals = testClassNavigation.travelTo(0, 0, 1, 1);
    assertNotNull(actuals);
  }

  @Test
  public void testNavigationTravelto3() {
    double[] actuals = testClassNavigation.travelTo(0, 0, 1, 1);
    assertArrayEquals(new double[] { Math.sqrt(2), 45 }, actuals, 0.1);
  }

  /*
   * turnTo(double theta, int absoluteHeading) tests
   */
  // Success
  @Test
  public void testNavigationTurnTo() {
    float actual = testClassNavigation.turnTo(30, 90);
    assertEquals(-60.0, actual, 0.0001);
  }

  @Test
  public void testNavigationTurnTo2() {
    float actual = testClassNavigation.turnTo(90, 30);
    assertEquals(60.0, actual, 0.0001);
  }

  @Test
  public void testNavigationTurnTo3() {
    float actual = testClassNavigation.turnTo(90, 30);
    assertEquals(60.0, actual, 0.1);
  }

}