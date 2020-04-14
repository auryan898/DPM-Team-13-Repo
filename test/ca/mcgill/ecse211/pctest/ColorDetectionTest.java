package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;

import ca.mcgill.ecse211.project.ColorDetection;
import org.junit.Test;

/**
 * This is a JUnit test for Color Detection math and logic. This test can be run
 * individually or along with all of the tests using {@link RunAllTests}
 * 
 * <p>
 * Note: This test is designed to be run on a PC/Computer, not an EV3.
 * 
 * @see ca.mcgill.ecse211.project.ColorDetection
 */
public class ColorDetectionTest {
  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#power(float, float)}.
   */
  @Test
  public void powerTest() {
    float actual = ColorDetection.power(2, 2);
    assertEquals(4f, actual, 0.001);
  }

  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#getColorSamples(int, float, float, float)}.
   */
  @Test
  public void getColorSamplesTest() {
    float[] actuals = ColorDetection.getColorSamples(10, 0.95f, 0.462f, 0.224f);
    assertArrayEquals(new float[] { 0.8797352f, 0.42782912f, 0.20743231f, 0.10798704f }, actuals,
        0.0001f);
  }

  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#determineColor(float, float, float, float, float, float)}.
   * Performed for the color green.
   */
  @Test
  public void determineColorTestGreen() {

    String actual = ColorDetection.determineColor(0.04f, 1f, 2f, 3f, 4f, 5f);
    assertEquals("Green", actual);
  }

  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#determineColor(float, float, float, float, float, float)}.
   * Performed for the color yellow.
   */
  @Test
  public void determineColorTestYellow() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 1f, 3f, 4f, 5f);
    assertEquals("Yellow", actual);
  }

  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#determineColor(float, float, float, float, float, float)}.
   * Performed for the color orange.
   */
  @Test
  public void determineColorTestOrange() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 3f, 1f, 4f, 5f);
    assertEquals("Orange", actual);
  }

  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#determineColor(float, float, float, float, float, float)}.
   * Performed for the color blue.
   */
  @Test
  public void determineColorTestBlue() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 4f, 3f, 1f, 5f);
    assertEquals("Blue", actual);
  }

  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#determineColor(float, float, float, float, float, float)}.
   * Performed for the color of the grey blue playing field ground.
   */
  @Test
  public void determineColorTestGround() {

    String actual = ColorDetection.determineColor(0.04f, 2f, 5f, 3f, 4f, 1f);
    assertEquals("Ground", actual);
  }

  /**
   * This tests
   * {@link ca.mcgill.ecse211.project.ColorDetection#determineColor(float, float, float, float, float, float)}.
   * Performed for the default None, when none of the colors should be tested.
   * Ensures, no false-positives.
   */
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
