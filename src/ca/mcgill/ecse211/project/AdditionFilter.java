package ca.mcgill.ecse211.project;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

/**
 * Adds a specified offset to every float value in the sample. 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class AdditionFilter extends AbstractFilter {

  private float addition;

  public AdditionFilter(SampleProvider source, float addition) {
    super(source);
    this.addition = addition;
  }

  @Override
  public void fetchSample(float[] sample, int off) {
    super.fetchSample(sample, off);
    for (int i = 0; i < sample.length; i++) {
      sample[i + off] += this.addition;
    }
  }

}