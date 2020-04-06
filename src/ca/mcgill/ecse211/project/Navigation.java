package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.absoluteHeading;

import lejos.robotics.chassis.Chassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;

/**
 * A simple custom navigation class that can direct the robot to travel to 
 * an x and y (cm) and turn to any angle heading, positive or negative angle.
 * Forward should be 90 degrees, and right is 0 degrees.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Navigation extends MovePilot {
  
  private OdometryPoseProvider odometry;
  private MovePilot pilot;

  public Navigation(Chassis chassis) {
    super(chassis);
    this.odometry = new OdometryPoseProvider(this);
    pilot = this;
  }
  
  /**
   * This method causes the robot to travel to the absolute field location (x, y), specified in tile
   * points. This method should continuously call turnTo(double theta) and then set
   * the motor speed to forward (straight). This will make sure that your heading is updated
   * until you reach your exact goal. This method will use the odometer.
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void travelTo(double x, double y) {
    double dx = x - odometry.getPose().getX();
    double dy = y - odometry.getPose().getY();
    // arctan plus math to add 180 when theta is past 90 deg
    double theta = Math.toDegrees(Math.atan2(dy,dx));
    double distance = Math.sqrt(dx * dx + dy * dy);
    
    this.turnTo(theta);
    pilot.travel((float)distance);
  }
  
  /**
   * This method causes the robot to turn (on point) to the absolute heading theta. This method
   * should turn a MINIMAL angle to its target.
   * @param theta the absolute heading to turn to
   */
  public void turnTo(double theta) {
    float heading = absoluteHeading(odometry.getPose().getHeading());
    float p1 = (float)(theta - heading) % 360;
    float turnAngle = Math.abs(p1) > 180 ? p1 - 360 * Math.signum(p1) : p1;
    pilot.rotate(turnAngle); // 
  }

  // TODO: javadoc
  public OdometryPoseProvider getOdometry() {
    return odometry;
  }
  
  public MovePilot getPilot() {
    return this;
  }
}
