package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FakeMotorTest {

  private FakeRegulatedMotor motor;

  @Before
  public void setupMotor() {
    motor = new FakeRegulatedMotor(100);
  }

  @Test
  public void sanityCheck() {
    assertNull(null);
  }

  @Test
  public void testMotorForward() {
    motor.forward();
    assertTrue(motor.isMoving());
    assertFalse(motor.isStalled());
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertNotEquals(0, motor.getTachoCount());
    motor.stop();
    assertFalse(motor.isMoving());
  }
  
  @Test
  public void testMotorLimitMovement() {
    assertFalse(motor.isMoving());
    motor.rotate(180);
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertFalse(motor.isMoving());
    motor.stop();
  }

  @After
  public void destroy() {
    motor.close();
  }
}
