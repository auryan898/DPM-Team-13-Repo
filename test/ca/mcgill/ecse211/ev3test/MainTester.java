package ca.mcgill.ecse211.ev3test;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.LocalResources;
import ca.mcgill.ecse211.tools.SubMenu;
import lejos.hardware.motor.Motor;

/**
 * The main class that includes basic initialization methods for initializing
 * navigation, odometry, and the robot pilot, along with sensors.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class MainTester {
  // Every user made thread should define its own sleep interval
  private static final long T_INTERVAL = 10;
  public static float WHEEL_RADIUS = 4.4f;

  public static void main(String[] args) {
    // Initializing code goes here.
    LocalResources.initNavigation(WHEEL_RADIUS,BASE_WIDTH,Motor.A,Motor.D);
    
    SubMenu testsMenu = TestsMenu.createTests();
    while (!END_PROGRAM) {
      END_PROGRAM = testsMenu.select();

      try {

        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

  }

}
