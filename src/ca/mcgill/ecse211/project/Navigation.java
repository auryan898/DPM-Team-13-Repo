package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.absoluteHeading;

import lejos.robotics.chassis.Chassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;

/**
 * A simple custom navigation class that can direct the robot to travel to an x
 * and y (cm) and turn to any angle heading, positive or negative angle. Forward
 * should be 90 degrees, and right is 0 degrees.
 * 
 * <p>
 * See sections 2.3, 3.0, 5.1 and 5.2 from the "Software Documentation Final"
 * document for how the class is used and algorithms to be implemented.
 * 
 * @author Ryan Au auryan898@gmail.com
 */
public class Navigation extends MovePilot {

  private OdometryPoseProvider odometry;
  private MovePilot pilot;

  /**
   * Creates a Navigation instance which is a subclass of the lejos MovePilot, and
   * contains a reference to the lejos OdometryPoseProvider.
   * 
   * @param chassis a lejos chassis that controls the wheels as a single unit
   */
  public Navigation(Chassis chassis) {
    super(chassis);
    this.odometry = new OdometryPoseProvider(this);
    pilot = this;
  }

  /**
   * This method causes the robot to travel to the absolute field location (x, y),
   * specified in tile points. This method should continuously call turnTo and
   * then set the motor speed to forward (straight). This will make sure that your
   * heading is updated until you reach your exact goal.
   * 
   * <p>
   * If immediateReturn is true, then this method returns immediately, running its
   * actions concurrently through the existing thread(s) which control the
   * motors. If false, this method will return only when it has completed its
   * commands and stopped moving.
   * 
   * @param x               the x coordinate
   * @param y               the y coordinate
   * @param immediateReturn if true the method will return immediately, and block
   *                        if false
   */
  public void travelTo(double x, double y, boolean immediateReturn) {
    double x0 = odometry.getPose().getX();
    double y0 = odometry.getPose().getY();

    double theta = angleToPoint(x0, y0, x, y);
    double distance = distanceToPoint(x0, y0, x, y);

    this.turnTo(theta);
    pilot.travel((float) distance);
  }

  /**
   * This method causes the robot to travel to the absolute field location (x, y),
   * specified in tile points. This method should continuously call turnTo and
   * then set the motor speed to forward (straight). This will make sure that your
   * heading is updated until you reach your exact goal.
   * 
   * <p>
   * immediateReturn is false by default.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void travelTo(double x, double y) {
    travelTo(x, y, false);
  }

  /**
   * Helper method that computes the angle in degrees from one point
   * (x0,y0) to another point (x1,y1).
   * 
   * @param  x0 the x-coordinate of the first point
   * @param  y0 the y-coordinate of the first point
   * @param  x1 the x-coordinate of the second point
   * @param  y1 the y-coordinate of the second point
   * @return    the angle in degrees
   */
  public double angleToPoint(double x0, double y0, double x1, double y1) {
    double dx = x1 - x0;
    double dy = y1 - y0;
    // arctan plus math to add 180 when theta is past 90 deg
    return Math.toDegrees(Math.atan2(dy, dx));
  }

  /**
   * Helper method that computes the distance from one point (x0,y0) to another
   * point (x1,y1).
   * 
   * @param  x0 the x-coordinate of the first point
   * @param  y0 the y-coordinate of the first point
   * @param  x1 the x-coordinate of the second point
   * @param  y1 the y-coordinate of the second point
   * @return    the distance between the points
   */
  public double distanceToPoint(double x0, double y0, double x1, double y1) {
    double dx = x1 - x0;
    double dy = y1 - y0;
    return Math.sqrt(dx * dx + dy * dy);
  }

  /**
   * This method causes the robot to turn (on point) to the absolute heading
   * theta. This method should turn the MINIMAL angle to its target.
   * 
   * <p>
   * If immediateReturn is true, then this method returns immediately, running its
   * actions concurrently through the existing thread(s) which control the
   * motors. If false, this method will return only when it has completed its
   * commands and stopped moving.
   * 
   * @param theta the heading to turn to, 0 degrees is East
   */
  public void turnTo(double theta, boolean immediateReturn) {
    float heading = absoluteHeading(odometry.getPose().getHeading());
    float turnAngle = minimumAngleToHeading((float) theta, heading);
    pilot.rotate(turnAngle); //
  }

  /**
   * This method causes the robot to turn (on point) to the absolute heading
   * theta. This method should turn the MINIMAL angle to its target.
   * 
   * <p>
   * immediateReturn is false by default.
   * 
   * @param theta the heading to turn to, 0 degrees is East
   */
  public void turnTo(double theta) {
    turnTo(theta, false);
  }

  /**
   * Determines the smallest angle between a current heading, and the target
   * heading. The result is either negative or positive, where positive is
   * counter-clockwise.
   * 
   * @param  currHeading   the current heading in degrees
   * @param  targetHeading the target heading in degrees
   * @return               the smallest angle needed to turn from the current
   *                       heading to the target
   */
  public float minimumAngleToHeading(float currHeading, float targetHeading) {
    float p1 = (float) (currHeading - targetHeading) % 360;
    return Math.abs(p1) > 180 ? p1 - 360 * Math.signum(p1) : p1;
  }

  /**
   * Assigns a pose to the robot, where it assumes it is in the Cartesian plane.
   * Units should be in centimeters as is consistent with this code base. A lejos
   * Pose object contains the x and y coordinates of the robot, and its heading
   * with respect to the y-axis.
   * 
   * @param pose a lejos Pose object containing x, y, and theta
   */
  public void setPose(Pose pose) {
    odometry.setPose(pose);
  }

  /**
   * Assigns a pose to the robot, where it assumes it is in the Cartesian plane.
   * Units should be in centimeters as is consistent with this code base. Stores
   * the x and y coordinates of the robot, and its heading in degrees with respect
   * to the y-axis.
   * 
   * @param x       the x-coordinate in centimeters
   * @param y       the y-coordinate in centimeters
   * @param heading the heading in degrees
   */
  public void setPose(float x, float y, float heading) {
    this.setPose(new Pose(x, y, heading));
  }

  /**
   * Assigns a new x and y coordinate to the robot while preserving the heading.
   * 
   * @param x the new x-coordinate in centimeters
   * @param y the new y-coordinate in centimeters
   */
  public void setPosition(float x, float y) {
    Pose oldPose = getPose();
    setPose(x, y, oldPose.getHeading());
  }

  /**
   * Assigns a new heading to the robot while preserving the x and y coordinates.
   * 
   * @param theta the new heading in degrees
   */
  public void setHeading(float theta) {
    Pose oldPose = getPose();
    setPose(oldPose.getX(), oldPose.getY(), theta);
  }

  /**
   * Retrieves the Pose of the robot, which contains the x, y, and heading values
   * of the robot, in centimeters and degrees respectively.
   * 
   * @return a Pose object
   */
  public Pose getPose() {
    return odometry.getPose();
  }

  /**
   * Retrieves the x-coordinate of the robot, maintained by odometry.
   * 
   * @return the x-coordinate of the robot
   */
  public float getX() {
    return getPose().getX();
  }

  /**
   * Retrieves the y-coordinate of the robot, maintained by odometry.
   * 
   * @return the y-coordinate of the robot
   */
  public float getY() {
    return getPose().getY();
  }

  /**
   * Retrieves the heading of the robot, maintained by odometry.
   * 
   * @return the heading of the robot
   */
  public float getHeading() {
    return getPose().getHeading();
  }

  /**
   * Retrieves the internal instance OdometryPoseProvider.
   * 
   * @return the odometry controller which maintains position and heading
   */
  public OdometryPoseProvider getOdometry() {
    return odometry;
  }
}
