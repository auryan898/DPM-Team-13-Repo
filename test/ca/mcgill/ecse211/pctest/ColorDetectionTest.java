package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;

import ca.mcgill.ecse211.project.ColorDetection;
import org.junit.Test;

/*
 * To test the software logic without robot, our team split the methods into
 * logic
 * and readings. Later, we tested the logic using Junit. Whenever we could not
 * avoid a statement that needs hardware, we commented out the line.
 * Any methods under testClasses are for debugging purposes and should not be
 * modified. Otherwise, there maybe unexpected change in test results.
 */
public class ColorDetectionTest {
  /*
   * power(float a, float b) tests
   */
  // Success
  @Test
  public void powerTest() {
    float actual = ColorDetection.power(2, 2);
    assertEquals(4f, actual, 0.001);
  }

  /*
   * getColorSamples(int numSamples, float red, float green, float blue) tests
   */
  @Test
  public void getColorSamplesTest() {
    float[] actuals = ColorDetection.getColorSamples(10, 0.95f, 0.462f, 0.224f);
    assertArrayEquals(new float[] { 0.8797352f, 0.42782912f, 0.20743231f, 0.10798704f }, actuals,
        0.0001f);
  }
  /*
   * String determineColor(float[] x, float greenScore, float yellowScore, float
   * orangeScore, float blueScore, float groundScore) tests
   */

  @Test
  public void determineColorTestGreen() {

    String actual = ColorDetection.determineColor(0.04f, 1f, 2f, 3f, 4f, 5f);
    assertEquals("Green", actual);
  }

  @Test
  public void determineColorTestYellow() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 1f, 3f, 4f, 5f);
    assertEquals("Yellow", actual);
  }

  @Test
  public void determineColorTestOrange() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 3f, 1f, 4f, 5f);
    assertEquals("Orange", actual);
  }

  @Test
  public void determineColorTestBlue() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 4f, 3f, 1f, 5f);
    assertEquals("Blue", actual);
  }

  @Test
  public void determineColorTestGround() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 5f, 3f, 4f, 1f);
    assertEquals("Ground", actual);
  }

  @Test
  public void determineColorTestNonTargetColor() {

    String actual = ColorDetection.determineColor(0.00001f, 1f, 5f, 3f, 4f, 2f);
    assertEquals("None", actual);

    actual = ColorDetection.determineColor(5f, 1f, 5f, 3f, 4f, 2f);
    assertEquals("None", actual);
    

    actual = ColorDetection.determineColor(0.00001f, 1f, 1f, 3f, 4f, 2f);
    assertEquals("None", actual);

    actual = ColorDetection.determineColor(0.04f, 2f, 2f, 2f, 2f, 2f);
    assertEquals("None", actual);
  }
}
