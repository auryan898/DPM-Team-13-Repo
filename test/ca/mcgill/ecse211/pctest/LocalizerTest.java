package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;
import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.Localizer;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import org.junit.Test;

/*
 * To test the software logic without robot, our team split the methods into
 * logic
 * and readings. Later, we tested the logic using Junit. Whenever we could not
 * avoid a statement that needs hardware, we commented out the line.
 * Any methods under testClasses are for debugging purposes and should not be
 * modified. Otherwise, there maybe unexpected change in test results.
 */
public class LocalizerTest {

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
  
  @Test
  public void testGetRangeNonNull() {
    Float actual;
    // Range is larger
    actual = testClass.getRange(1, 2);
    assertNotNull(actual);

    actual = testClass.getRange(1, 2);
    assertEquals(-100.0, actual, 0.01);
  }

  /*
   * RangeReadings scanDist(float startAngle, float angleAmount, float
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

