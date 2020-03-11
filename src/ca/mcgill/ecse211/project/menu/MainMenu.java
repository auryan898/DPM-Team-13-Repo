package ca.mcgill.ecse211.project.menu;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.DemosMenu;
import ca.mcgill.ecse211.project.TestsMenu;
import lejos.hardware.Button;

/**
 * A menu that runs in its thread, giving options to control/calibrate the ev3
 * robot that you are running. Just start the thread and make sure nothing else
 * is accessing the Display at the same time.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class MainMenu implements Runnable {
  private static final long T_INTERVAL = 100;
  private SubMenu mainMenu;
  private SubMenu demoMenu;
  private SubMenu testMenu;
  private SubMenu calibrationMenu;

  public MainMenu() {
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
        display.clear();
        while (!endAction) {
          display.resetIndex(1);
          display.writeNext("" + localizer.getRange(255));
          display.writeNext("" + (localizer.getColor()[0]));
          display.writeNext(odometry.getPose().toString());
          display.writeNext(pilot.getAngularSpeed() + "|" + pilot.getAngularSpeed());
          display.writeNext(pilot.getLinearAcceleration() + "|" + pilot.getLinearSpeed());

          if (Button.getButtons() == Button.ID_UP) {
            endAction = true;
          }

          try {
            Thread.sleep(T_INTERVAL);
          } catch (InterruptedException e) {
            break;
          }
        }
        display.clear();
        return false;
      }
    });
  }

  /**
   * Defines the sub menu for actions to be taken during the demo.
   * 
   * @return return true to exit the parent menu. false to remain in parent menu.
   */
  protected boolean createDemoMenu() {
    demoMenu = DemosMenu.createDemos();
    return false;
  }

  /**
   * Defines the sub menu for testing actions/commands.
   * 
   * @return return true to exit the parent menu. false to remain in parent menu.
   */
  protected boolean createTestMenu() {
    testMenu = TestsMenu.createTests();

    return false;
  }

  @Override
  public void run() {

    boolean isExit = false;
    display.pause();
    isExit = mainMenu.select();
    END_PROGRAM = isExit;
  }

  /**
   * Defines the calibration tools.
   * 
   * @return return true to exit the parent menu. false to remain in parent menu.
   */
  protected boolean createCalibrationMenu() {
    calibrationMenu = new CalibrationMenu();
    return false;
  }

}