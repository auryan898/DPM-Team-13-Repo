package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.colorSensor;

/**
 * This class detects the color of an object. To minimize the undesirable variations in sensor 
 * recordings, this class will average several color samples, normalize its RGB value and then 
 * compare it to experimental data acquired beforehand. The error is modeled as normal distributions
 * and so the color is determined by selecting the smallest z-score.
 *  
 * @author Norman Kong, Kaustav Das Sharma, Ryan Au
 */

public class ColorDetection {

  /**
   * Experimental data acquired during Lab 5. These standard deviations and averages are used to 
   * calculate the z-score and classify the measured RGB value. 
   */
  private static final float[] GREEN_MEAN = {0.4652715682614525f, 
                                             0.8716384394527393f, 
                                             0.1521931106944226f};
  private static final float[] YELLOW_MEAN = {0.8479594500052327f, 
                                              0.5128551322689655f, 
                                              0.13291919375632036f};
  private static final float[] ORANGE_MEAN = {0.9546932751128298f, 
                                              0.2777835109422732f, 
                                              0.104133860962871f};
  private static final float[] BLUE_MEAN = {0.2242142313153791f, 
                                            0.6931567618538078f, 
                                            0.6813705004330141f};
  private static final float[] GREEN_STD = {0.00788269220822895f, 
                                            0.004803205374077032f, 
                                            0.02282209619561577f};
  private static final float[] YELLOW_STD = {0.003663400131882821f, 
                                             0.009305350127600396f, 
                                             0.013298960179338286f};
  private static final float[] ORANGE_STD = {0.004033579037438157f, 
                                             0.010961343607750352f, 
                                             0.020415427753297254f};
  private static final float[] BLUE_STD = {0.01819869174232276f, 
                                           0.04555264803687386f, 
                                           0.050888967245936736f};
  private static final float[] GROUND_MEAN = {0.5132043765273429f, 
                                              0.5982531733482047f, 
                                              0.6142292982582589f};
  private static final float[] GROUND_STD = {0.027300677383809424f, 
                                             0.010435169138117529f, 
                                             0.02413623650437421f};
  
  /** The maximum threshold for light values. */
  private static final float MAX_LIGHT_VALUE = 0.99f;
  
  /** The minimum threshold for brightness of sample. */
  private static final float MIN_BRIGHTNESS_LEVEL = 0.035f;
  
  /** number of samples read, averaged for accuracy. */
  private static final int NUM_SAMPLES_READ = 20;
  
  /** The light sample to be classified. */
  private static float[] lightSample;
  
  /**
   * Takes in 20 color samples and returns their average.
   * 
   * @return an array that stores the mean RGB value.
   */
  public static float[] getColorSamples(int numSamples) {
    
    lightSample = new float[3];
    float red = 0;
    float green = 0;
    float blue = 0;
    for (int i = 0; i < numSamples; i++) {
      colorSensor.fetchSample(lightSample, 0);
      red += (lightSample[0] >= MAX_LIGHT_VALUE) ? 0 : lightSample[0];
      green += (lightSample[1] >= MAX_LIGHT_VALUE) ? 0 : lightSample[1];
      blue += (lightSample[2] >= MAX_LIGHT_VALUE) ? 0 : lightSample[2];
    }

    float redAverage = red / numSamples;
    float greenAverage = green / numSamples;
    float blueAverage = blue / numSamples;
    
    float normalize = (float) Math.sqrt(power(redAverage, 2) 
                                        + power(greenAverage, 2) 
                                        + power(blueAverage, 2));

    float[] output = {(redAverage / normalize), 
                      (greenAverage / normalize), 
                      (blueAverage / normalize), normalize};
    return output;
  } 
  
  /**
   * Returns a to the power of b.
   * @param a the base
   * @param b the exponent
   * @return a to the power of b
   */
  private static float power(float a, float b) {
    return (float) (Math.pow(a, b));
  }
  
  /**
   * Determines the color of a given RGB value based on a z-score calculation.
   * 
   * @param x A given RGB value stored in a float array of the form [R value, G value, B value].
   * @return Returns the color of the given RGB value as a String.
   */
  public static String determineColor(float[] x) {
    float greenScore = calcZScore(x, GREEN_MEAN, GREEN_STD);
    float yellowScore = calcZScore(x, YELLOW_MEAN, YELLOW_STD);
    float orangeScore = calcZScore(x, ORANGE_MEAN, ORANGE_STD);
    float blueScore = calcZScore(x, BLUE_MEAN, BLUE_STD);
    float groundScore = calcZScore(x, GROUND_MEAN, GROUND_STD);
    
    float brightness = (x[3]);
    if (brightness < MIN_BRIGHTNESS_LEVEL || brightness > 1) {
      return "None";
    }
    if (greenScore < yellowScore && greenScore < orangeScore && greenScore < blueScore 
        && greenScore < groundScore) {
      return "Green";
    } else if (yellowScore < greenScore && yellowScore < orangeScore && yellowScore < blueScore
        && yellowScore < groundScore) {
      return "Yellow";
    } else if (orangeScore < yellowScore && orangeScore < greenScore && orangeScore < blueScore
        && orangeScore < groundScore) {
      return "Orange";
    } else if (blueScore < yellowScore && blueScore < greenScore && blueScore < orangeScore
        && blueScore < groundScore) {
      return "Blue";
    } else if (groundScore < blueScore && groundScore < yellowScore && groundScore < greenScore
        && groundScore < orangeScore) {
      return "Ground";
    }
    return "None";
  }
  
  /**
   * Calculates the average z-score of an RGB sample given its mean and standard deviation from a
   * normal distribution.
   * 
   * @param sample A color sample.
   * @param mean The mean for the sample's normal distribution.
   * @param std The standard deviation of the sample's normal distribution.
   * @return A z-score of the RGB values (averaged) for the given sample.
   */
  public static float calcZScore(float[] sample, float[] mean, float[] std) {
    float score = 0;
    float[] zscore = new float[3];
    
    // calculate z-score for R, G and B values
    for (int i = 0; i < zscore.length; i++) { 
      zscore[i] = Math.abs(sample[i] - mean[i]) / std[i];
    }
    
    // average the z-score for R, G and B into one score
    for (int i = 0; i < zscore.length; i++) {
      score += zscore[i];
    }
    return score / 3;
  }

  /**
   * Returns ring color as a String name.
   * 
   * @return None, Green, Orange, Blue, Yellow, or Ground
   */
  public static String getRingColour() {
    return ColorDetection.determineColor(ColorDetection.getColorSamples(NUM_SAMPLES_READ));
  }
  
}
