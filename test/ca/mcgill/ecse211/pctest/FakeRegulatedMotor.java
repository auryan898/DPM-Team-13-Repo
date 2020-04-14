package ca.mcgill.ecse211.pctest;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

public class FakeRegulatedMotor implements RegulatedMotor {
  private static final int DEFAULT_ACCELERATION = 0;
  private static final int DEFAULT_SPEED = 0;
  private static final int DEFAULT_STALL_ERROR = 0;
  private static final int DEFAULT_MIN_STALL_TIME = 0;
  private boolean activeDevice = true;
  private int tachoCount = 0;

  private float dist = 0;
  private float deltaDist = 0;

  private int motorSpeed = 0;
  private int motorAcc = 0;

  public FakeRegulatedMotor() {
    setAcceleration(DEFAULT_ACCELERATION);
    setSpeed(DEFAULT_SPEED);
    setStallThreshold(DEFAULT_STALL_ERROR, DEFAULT_MIN_STALL_TIME);
    resetTachoCount();
  }

  @Override
  public void forward() {
    // TODO: implement this method
  }

  @Override
  public void backward() {
    // TODO: implement this method
  }

  @Override
  public void stop() {
    // TODO: implement this method
  }

  @Override
  public void flt() {
    // TODO: implement this method
  }

  @Override
  public boolean isMoving() {
    // TODO: implement this method
    return false;
  }

  @Override
  public int getRotationSpeed() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getTachoCount() {
    return tachoCount;
  }

  @Override
  public void resetTachoCount() {
    tachoCount = 0;
  }

  @Override
  public void addListener(RegulatedMotorListener listener) {
    // TODO: implement this method

  }

  @Override
  public RegulatedMotorListener removeListener() {
    // TODO: implement this method
    return null;
  }

  @Override
  public void stop(boolean immediateReturn) {
    // TODO: implement this method

  }

  @Override
  public void flt(boolean immediateReturn) {
    // TODO: implement this method

  }

  @Override
  public void waitComplete() {
    // TODO: implement this method

  }

  @Override
  public void rotate(int angle, boolean immediateReturn) {
    // TODO: implement this method

  }

  @Override
  public void rotate(int angle) {
    // TODO Auto-generated method stub

  }

  @Override
  public void rotateTo(int limitAngle) {
    // TODO Auto-generated method stub

  }

  @Override
  public void rotateTo(int limitAngle, boolean immediateReturn) {
    // TODO Auto-generated method stub

  }

  @Override
  public int getLimitAngle() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setSpeed(int speed) {

  }

  @Override
  public int getSpeed() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getMaxSpeed() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isStalled() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setStallThreshold(int error, int time) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setAcceleration(int acceleration) {
    // TODO Auto-generated method stub

  }

  @Override
  public void synchronizeWith(RegulatedMotor[] syncList) {
    // TODO Auto-generated method stub

  }

  @Override
  public void startSynchronization() {
    // TODO Auto-generated method stub

  }

  @Override
  public void endSynchronization() {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

  class RunningMotor implements Runnable {
    @Override
    public void run() {
      while (activeDevice) {

      }
    }
  }

}
