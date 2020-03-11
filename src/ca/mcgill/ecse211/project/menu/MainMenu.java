package ca.mcgill.ecse211.project.menu;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.tools.MenuAction;
import ca.mcgill.ecse211.tools.SubMenu;
import lejos.hardware.Button;

/**
 * A menu that runs in its thread, giving options to control/calibrate the ev3
 * robot that you are running. Just start the thread and make sure nothing else
 * is accessing the Display at the same time.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class MainMenu extends SubMenu {
  private static final long T_INTERVAL = 100;
  private SubMenu demoMenu;
  private SubMenu calibrationMenu;

  private MainMenu() {
    super("Competition");
  }

  public static MainMenu createMainMenu() {
    final MainMenu menu = new MainMenu();
    
    menu.createDemoMenu();
    menu.createCalibrationMenu();

    menu.addItem("Robot Demo", new MenuAction() {
      public boolean action() {
        menu.demoMenu.select(); // Repeats the selection logic until the exit item is chosen.
        return false;
      }
    });
    menu.addItem("Calibration Tools", new MenuAction() {
      public boolean action() {
        menu.calibrationMenu.select(); // Repeats the selection logic until the exit item is chosen.
        return false;
      }
    });
    menu.addItem("Robot Status", new MenuAction() {
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
    
    return menu;
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
   * Defines the calibration tools.
   * 
   * @return return true to exit the parent menu. false to remain in parent menu.
   */
  protected boolean createCalibrationMenu() {
    calibrationMenu = CalibrationMenu.createCalibrations();
    return false;
  }
}