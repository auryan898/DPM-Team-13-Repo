package ca.mcgill.ecse211.project.tools;

import static ca.mcgill.ecse211.project.Resources.*;

import ca.mcgill.ecse211.project.Main;
import lejos.hardware.Button;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.Navigator;

/**
 * A menu that runs in its thread, giving options to control/calibrate the ev3
 * robot that you are running. Just start the thread and make sure nothing else
 * is accessing the Display at the same time.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class MenuDisplay implements Runnable {
  private static final long T_INTERVAL = 100;
  private SubMenu mainMenu;
  private SubMenu demoMenu;
  private SubMenu testMenu;
  private SubMenu calibrationMenu;
  
  public MenuDisplay() {
    createDemoMenu();
    createCalibrationMenu();
    createTestMenu();
    
    
    mainMenu = new SubMenu("Lab 5 - Colors");
    mainMenu.addItem("Robot Tests", new MenuAction() {
      public boolean action() {
        testMenu.select(); // Repeats the selection logic until the exit item is chosen.
        return false;
      }
    });
    mainMenu.addItem("Robot Demo", new MenuAction() {
      public boolean action() {
        demoMenu.select(); // Repeats the selection logic until the exit item is chosen.
        return false;
      }
    });
    mainMenu.addItem("Calibration Tools", new MenuAction() {
      public boolean action() {
        calibrationMenu.select(); // Repeats the selection logic until the exit item is chosen.
        return false;
      }
    });
    mainMenu.addItem("Robot Status", new MenuAction() {
      public boolean action() {
        boolean endAction = false;
        display.writeNext("Robot Status");
        while (!endAction) {
          display.resetIndex(1);
          display.writeNext("" + localizer.getRange(255));
          display.writeNext("" + localizer.getColor());
          display.writeNext(odometry.getPose().toString());
          display.writeNext(pilot.getAngularSpeed() + "|" + pilot.getAngularSpeed());
          display.writeNext(pilot.getLinearAcceleration() + "|" + pilot.getLinearSpeed());
          
          if (Button.getKeyClickLength() > 1) {
            endAction = true;
            break;
          }
          
          try {
            Thread.sleep(T_INTERVAL);
          } catch (InterruptedException e) {
            break;
          }
        }
        return false;
      }
    });
  }

  /**
   * Defines the sub menu for actions to be taken during the demo.
   * @return return true to exit the parent menu. false to remain in parent menu.
   */
  protected boolean createDemoMenu() {
    demoMenu = new SubMenu("Demo Actions");
    demoMenu.addItem("First Map", new MenuAction() {
      public boolean action() {
        // place code here for the demo code.
        return false;
      }
    });
    return false;
  }

  /**
   * Defines the sub menu for testing actions/commands.
   * @return return true to exit the parent menu. false to remain in parent menu.
   */
  protected boolean createTestMenu() {
    testMenu = new SubMenu("Testing Runs");

    // add menu actions from this point
    testMenu.addItem("Test:Localize", new MenuAction() {
      public boolean action() {
        // place test code here.
        return false;
      }
    });

    return false;
  }

  @Override
  public void run() {
    

    boolean isExit = false;
    display.pause();
    isExit = mainMenu.select();
//    while (!END_PROGRAM && !isExit) {
//
//      try {
//        Thread.sleep(T_INTERVAL);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//        break;
//      }
//    }
    END_PROGRAM = isExit;
  }

  /**
   * Defines the calibration tools.
   * @return return true to exit the parent menu. false to remain in parent menu.
   */
  protected boolean createCalibrationMenu() {
    calibrationMenu = new SubMenu("Calibration Tools");

    // Tests Wheel Radius, it should move backwards one tile.
    calibrationMenu.addItem("Wheel Radius", new MenuCommand() {
      public void setStatus(int changeFactor) {
        Main.WHEEL_RADIUS += changeFactor * .05;
      }

      public String getStatus() {
        return "" + Main.WHEEL_RADIUS;
      }

      public void action() {
        int angle = (int)(TILE_WIDTH / Main.WHEEL_RADIUS * 180 / Math.PI);
        motorLeft.setSpeed(100);
        motorRight.setSpeed(100);
        motorLeft.rotate(-angle);
        motorRight.rotate(-angle);
      }
    });

    // Tests Ultrasonic Distance. Test on known distance, (Tile?)
    calibrationMenu.addItem("U.S Dist Offset", new MenuCommand() {
      private SampleProvider sampler = ultrasonicSensorDevice.getDistanceMode();
      private float[] buffer = { 0 };
      private float offset = 0;

      public void setStatus(int changeFactor) {
        offset += 0.2; // in centimeter
      }

      public String getStatus() {
        sampler.fetchSample(buffer, 0);
        return TILE_WIDTH + "" + (buffer[0] * 100 + offset);
      }

      public void action() {
        pilot.travel(-TILE_WIDTH);
      }
    });

    // Tests Base Width. Should rotate 360 deg.
    calibrationMenu.addItem("Base Width", new MenuCommand() {

      public String getStatus() {
        return "" + BASE_WIDTH;
      }

      public void setStatus(int changeFactor) {
        BASE_WIDTH += changeFactor * 0.01;
      }

      public void action() {
        int angle = (int)(BASE_WIDTH / Main.WHEEL_RADIUS);
        motorLeft.setSpeed(100);
        motorRight.setSpeed(100);
        motorLeft.rotate(-angle);
        motorRight.rotate(+angle);
      }
    });

    return false;
  }

}