package pctest;

import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Move;
import lejos.utility.Matrix;

public class FakeChassis implements Chassis {

  private double linearSpeed;
  private double angularAcceleration;
  private double linearAcceleration;
  private double angularSpeed;

  @Override
  public double getLinearSpeed() {
    return linearSpeed;
  }

  @Override
  public void setLinearSpeed(double linearSpeed) {
    this.linearSpeed = linearSpeed;
  }

  @Override
  public double getAngularSpeed() {
    return angularSpeed;
  }

  @Override
  public void setAngularSpeed(double angularSpeed) {
    this.angularSpeed = angularSpeed;
  }

  @Override
  public double getLinearAcceleration() {
    return linearAcceleration;
  }

  @Override
  public void setLinearAcceleration(double linearAcceleration) {
    this.linearAcceleration = linearAcceleration;
  }

  @Override
  public double getAngularAcceleration() {
    return angularAcceleration;
  }

  @Override
  public void setAngularAcceleration(double angularAcceleration) {
    this.angularAcceleration = angularAcceleration;
  }

  @Override
  public Matrix getCurrentSpeed() {
    
    return new Matrix(new double[][] {{}});
  }

  @Override
  public boolean isMoving() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setVelocity(double linearSpeed, double angularSpeed) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setVelocity(double linearSpeed, double direction, double angularSpeed) {
    // TODO Auto-generated method stub

  }

  @Override
  public void travelCartesian(double xSpeed, double ySpeed, double angularSpeed) {
    // TODO Auto-generated method stub

  }

  @Override
  public void travel(double linear) {
    // TODO Auto-generated method stub

  }

  @Override
  public void arc(double radius, double angle) {
    // TODO Auto-generated method stub

  }

  @Override
  public double getMaxLinearSpeed() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getMaxAngularSpeed() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void waitComplete() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isStalled() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public double getMinRadius() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setSpeed(double linearSpeed, double angularSpeed) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setAcceleration(double forwardAcceleration, double angularAcceleration) {
    // TODO Auto-generated method stub

  }

  @Override
  public PoseProvider getPoseProvider() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void moveStart() {
    // TODO Auto-generated method stub

  }

  @Override
  public Move getDisplacement(Move move) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void rotate(double angular) {
    // TODO Auto-generated method stub

  }

  @Override
  public double getLinearVelocity() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getLinearDirection() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getAngularVelocity() {
    // TODO Auto-generated method stub
    return 0;
  }

}
