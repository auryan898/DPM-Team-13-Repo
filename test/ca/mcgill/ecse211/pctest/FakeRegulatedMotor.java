package ca.mcgill.ecse211.pctest;

import lejos.hardware.motor.MotorRegulator;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.utility.Delay;

/**
 * Creates a mock motor for robot-free testing purposes. Use it in place of any
 * instance of a typical lejos motor.
 * 
 * @author Ryan Au
 *
 */
public class FakeRegulatedMotor implements RegulatedMotor, MotorRegulator {
  private static final int DEFAULT_ACCELERATION = 2;
  private static final int DEFAULT_SPEED = 50;
  private static final int DEFAULT_STALL_ERROR = 0;
  private static final int DEFAULT_MIN_STALL_TIME = 0;
  protected static final int NO_LIMIT = 0x7fffffff;
  private static final int MAX_SPEED_AT_9V = 900;
  private static float ASSUMED_EV3_VOLTAGE = 8f;

  private MotorMonitor motorMonitor = new MotorMonitor();
  private Thread motorThread = new Thread(motorMonitor);
  protected final MotorRegulator reg;

  private boolean activeDevice = true;
  private int tachoCount = 0;

  private float speed = 360;
  private int acceleration = 6000;
  private RegulatedMotor motor;
  private RegulatedMotorListener listener;
  private boolean moving; // true if the thread determines it is moving
  public boolean started; // true if a move is started
  private float currVelocity; // current velocity tracked by the thread
  private float currPosition; // current position tracked by the thread
  private float currSpeed; // the speed to achieve during a move
  private int currAcc; // the acceleration used to achieve the speed
  private int currLimit; // the limit of the current move

  private long msInterval; // millisecond time per cycle of the thread
  private double frequencyPerSec; // how many times per second a cycle runs
  private int oldTachoCount;

  public FakeRegulatedMotor(long msInterval) {
    reg = this;
    this.msInterval = msInterval;
    this.frequencyPerSec = 1000f / msInterval;
    setAcceleration(DEFAULT_ACCELERATION);
    setSpeed(DEFAULT_SPEED);
    setStallThreshold(DEFAULT_STALL_ERROR, DEFAULT_MIN_STALL_TIME);
    resetTachoCount();

    motorThread.setDaemon(true);
    motorThread.start();
  }

  @Override
  public void forward() {
    newMove(speed, acceleration, +NO_LIMIT, true, false);
  }

  @Override
  public void backward() {
    newMove(speed, acceleration, -NO_LIMIT, true, false);
  }

  @Override
  public void stop() {
    newMove(0, acceleration, 0, false, true);
  }

  @Override
  public void stop(boolean immediateReturn) {
    newMove(0, acceleration, 0, false, !immediateReturn);
  }

  @Override
  public void flt() {
    newMove(0, acceleration, 0, false, true);
  }

  @Override
  public void flt(boolean immediateReturn) {
    newMove(0, acceleration, 0, false, !immediateReturn);
  }

  @Override
  public boolean isMoving() {
    return moving;
  }

  @Override
  public int getRotationSpeed() {
    return Math.round(this.getCurrentVelocity());
  }

  @Override
  public int getTachoCount() {
    return tachoCount;
  }

  @Override
  public void resetTachoCount() {
    if (!isMoving()) {
      tachoCount = 0;
    }
  }

  @Override
  public void addListener(RegulatedMotorListener listener) {
    reg.addListener(this, listener);
  }

  @Override
  public RegulatedMotorListener removeListener() {
    RegulatedMotorListener old = listener;
    listener = null;
    return old;
  }

  @Override
  public void waitComplete() {
    while (this.isMoving()) {
      Delay.msDelay(1);
    }
  }

  @Override
  public void rotateTo(int limitAngle, boolean immediateReturn) {
    newMove(speed, acceleration, limitAngle, true, !immediateReturn);
  }

  @Override
  public void rotateTo(int limitAngle) {
    rotateTo(limitAngle, false);

  }

  @Override
  public void rotate(int angle, boolean immediateReturn) {
    rotateTo(Math.round(reg.getPosition()) + angle, immediateReturn);
  }

  @Override
  public void rotate(int angle) {
    rotate(angle, false);
  }

  @Override
  public int getLimitAngle() {
    return currLimit;
  }

  @Override
  public void setSpeed(int speed) {
    this.speed = Math.abs(speed);
    reg.adjustSpeed(this.speed);
  }

  public void setSpeed(float speed) {
    this.speed = Math.abs(speed);
    reg.adjustSpeed(this.speed);
  }

  @Override
  public int getSpeed() {
    return Math.round(speed);
  }

  @Override
  public float getMaxSpeed() {
    // Max voltage on an EV3 is 8V, and the EV3 motors are assumed to have a max
    // speed of 100 degree/second * Voltage. 90% of this value is taken for
    // realistic usage with other motors.
    return ASSUMED_EV3_VOLTAGE * MAX_SPEED_AT_9V / 9.0f * 0.9f;
  }

  @Override
  public boolean isStalled() {
    return false;
  }

  @Override
  public void setStallThreshold(int error, int time) {
    return;
  }

  @Override
  public void setAcceleration(int acceleration) {
    this.acceleration = Math.abs(acceleration);
    reg.adjustAcceleration(this.acceleration);
  }

  @Override
  public void synchronizeWith(RegulatedMotor[] syncList) {
    // Create list of regualtors and pass it on!
    MotorRegulator[] rl = new MotorRegulator[syncList.length];
    for (int i = 0; i < syncList.length; i++) {
      rl[i] = ((FakeRegulatedMotor) syncList[i]);
    }
    reg.synchronizeWith(rl);
  }

  @Override
  public void startSynchronization() {
    return;
  }

  @Override
  public void endSynchronization() {
    endSynchronization(true);
  }

  @Override
  public void close() {
    activeDevice = false;
  }

  class MotorMonitor implements Runnable {
    long startCycleTime;

    @Override
    public void run() {
      long elapsedMilliseconds;
      double displacement;
      double targetDisplacement;
      while (activeDevice) {
        while (!started) {
          try {
            Thread.sleep(5);
          } catch (InterruptedException e) {
            return;
          }
        }
        if (started) { // START: updating motor values if start is true
          synchronized (this) {
            startCycleTime = System.currentTimeMillis();
            targetDisplacement = Math.round(currLimit - oldTachoCount);
            // tacho value update loop
            moving = true;
            currPosition = currPosition + currVelocity * msInterval * 0.001f
                + 0.5f * currAcc * msInterval * 0.001f * msInterval * 0.001f;
            currVelocity = currVelocity
                + currAcc * msInterval * (float) Math.signum(targetDisplacement);

            // Reaching the speed limit, set acceleration to 0
            if (Math.signum(currVelocity) == Math.signum(currSpeed)
                && Math.abs(currVelocity) > Math.abs(currSpeed)) {
              currVelocity = Math.abs(currSpeed) * (float) Math.signum(targetDisplacement);
              currAcc = 0;
            }

            // Reaching the movement limit
            displacement = tachoCount - oldTachoCount;
            targetDisplacement = Math.round(currLimit - oldTachoCount);
            if (Math.signum(targetDisplacement) == 0
                || (Math.signum(displacement) == Math.signum(targetDisplacement)
                    && Math.abs(displacement) > Math.abs(targetDisplacement))) {
              tachoCount = oldTachoCount + currLimit;
              currSpeed = 0;
              currAcc = 0;
              moving = false;
            }
            tachoCount = (int) currPosition;
            try {
              elapsedMilliseconds = (System.currentTimeMillis() - startCycleTime);
              if (msInterval > elapsedMilliseconds) {
                Thread.sleep(msInterval - elapsedMilliseconds);
              }
            } catch (InterruptedException e) {
              return;
            }
          }
          // END: updating motor values
        } else {
          try {
            wait(5);
          } catch (InterruptedException e) {
            return;
          }
        }
        checkComplete();
      }
    }

    protected synchronized void checkComplete() {
      if (started && !isMoving()) {
        started = false;
        if (listener != null) {
          listener.rotationStopped(motor, getTachoCount(), isStalled(), System.currentTimeMillis());
        }
      }
    }

  }

  @Override
  public void setControlParamaters(int typ, float moveP, float moveI, float moveD, float holdP,
      float holdI, float holdD, int offset) {
    // Stop the motor if needed
    newMove(0, 1000, NO_LIMIT, false, true);
    return;
  }

  @Override
  public float getCurrentVelocity() {
    return currVelocity;
  }

  @Override
  public float getPosition() {
    return tachoCount;
  }

  @Override
  public void newMove(float speed, int acceleration, int limit, boolean hold,
      boolean waitComplete) {
    synchronized (motorThread) {
      // Ignore repeated commands
      // if (isMoving()) {
      // return;
      // }
      // save the move parameters
      this.oldTachoCount = tachoCount;
      this.currPosition = tachoCount;
      this.currSpeed = speed;
      this.currAcc = acceleration;
      this.currLimit = limit;
      this.started = true;
      this.moving = true;
    }
    if (waitComplete) {
      waitComplete();
    }
  }

  @Override
  public void adjustSpeed(float newSpeed) {
    speed = newSpeed;
  }

  @Override
  public void adjustAcceleration(int newAcc) {
    acceleration = newAcc;
  }

  @Override
  public void addListener(RegulatedMotor motor, RegulatedMotorListener listener) {
    this.motor = motor;
    this.listener = listener;
  }

  @Override
  public void endSynchronization(boolean b) {
    return;
  }

  @Override
  public void synchronizeWith(MotorRegulator[] rl) {
    return;
  }
}
