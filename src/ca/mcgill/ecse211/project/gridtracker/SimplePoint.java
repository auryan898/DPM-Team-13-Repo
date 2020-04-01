package ca.mcgill.ecse211.project.gridtracker;

public class SimplePoint {
  protected float x;
  protected float y;
  protected float theta;

  public float distanceTo(SimplePoint p) {
    float a = x - p.x;
    float b = y - p.y;
    return (float) Math.sqrt(a * a + b * b);
  }

  /**
   * @return the x
   */
  public float getX() {
    return x;
  }

  /**
   * @param x the x to set
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * @return the y
   */
  public float getY() {
    return y;
  }

  /**
   * @param y the y to set
   */
  public void setY(float y) {
    this.y = y;
  }

  /**
   * @return the theta
   */
  public float getTheta() {
    return theta;
  }

  /**
   * @param theta the theta to set
   */
  public void setTheta(float theta) {
    this.theta = theta;
  }
}
