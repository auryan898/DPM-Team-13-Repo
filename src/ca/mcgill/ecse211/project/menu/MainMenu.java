package ca.mcgill.ecse211.project.menu;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.LocalResources;
import ca.mcgill.ecse211.tools.MenuAction;
import ca.mcgill.ecse211.tools.SubMenu;
import lejos.hardware.Button;

/**
 * This is the Main Menu of the robot. By default it contains two menu items,
 * the Demos Menu, and the Calibration Menu. If either of these two options are
 * chosen from the robot, that respective menu's options will be displayed. All
 * of the options of this class run in the original thread that calls select()
 * on this instance, and all menus afterward would do the same.
 * 
 * <p>
 * More menu options can be added to this list by adding to getInstance()
 * defined in this class.
 * 
 * <p>
 * Example of adding a new menu option:
 * {@code 
 * menu.addItem("Title of option", new MenuAction() {
 *   public boolean action() {
 *     System.out.println("I called a method from a menu item");
 *     return false; // return true to exit the current menu
 * }
 * });
 * }
 * 
 * @author Ryan Au auryan898@gmail.com
 */
public class MainMenu extends SubMenu {
  private static final long T_INTERVAL = 100;
  private SubMenu demoMenu;
  private SubMenu calibrationMenu;
  private static MainMenu menu;

  /**
   * This constructor should not be used to instantiate this class.
   */
  private MainMenu() {
    super("Competition", LocalResources.display);
  }

  /**
   * Creates and returns an instance of SubMenu that contains menu items specific
   * to the MainMenu in the form of MenuAction instances or MenuCommand instances.
   * Each of these actions are essentially run in the main thread, but the user
   * running the program gets to pick which commands to run at the times they wish
   * via the menu displayed on the LCD of the EV3.
   *
   * <p>
   * see @see ca.mcgill.ecse211.tools.MenuAction and see @see
   * ca.mcgill.ecse211.tools.MenuCommand for more information on actions.
   * 
   * @return Returns the same instance of MainMenu every time.
   */
  public static MainMenu getInstance() {
    if (menu != null) {
      return menu;
    }
    menu = new MainMenu();

    menu.demoMenu = menu.createDemoMenu();
    menu.calibrationMenu = menu.createCalibrationMenu();

    // Adds a sub menu to this list, and when it is chosen it will display that one
    // until it exits, and then returns to this menu
    menu.addItem("Robot Demos", new MenuAction() {
      public boolean action() {
        menu.demoMenu.select(); // Repeats selection until the nested menu exits
        return false;
        // return true instead to exit this menu, which would end this whole program
        // that is running on the robot

      }
    });
    menu.addItem("Calibration Tools", new MenuAction() {
      public boolean action() {
        menu.calibrationMenu.select(); // Repeats selection until the nested menu exits
        return false;
      }
    });

    return menu;
  }

  /**
   * Gets the menu instance created in the DemosMenu class.
   * 
   * @return a SubMenu instance containing predefined actions
   */
  protected SubMenu createDemoMenu() {
    return DemosMenu.getInstance();
  }

  /**
   * Gets the menu instance created in the CalibrationMenu class
   * 
   * @return a SubMenu instance containing predefined actions
   */
  protected SubMenu createCalibrationMenu() {
    return CalibrationMenu.getInstance();
  }
}