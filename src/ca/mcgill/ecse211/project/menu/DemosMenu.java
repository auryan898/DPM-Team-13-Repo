package ca.mcgill.ecse211.project.menu;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.LocalResources;
import ca.mcgill.ecse211.tools.MenuAction;
import ca.mcgill.ecse211.tools.SubMenu;

/**
 * This is the Demos Menu, named "Robot Demos" from the Main Menu options. By
 * default, this class contains an example item, and space is left to the next
 * programmers to fill in the further menu options that they wish to use.
 * 
 * <p>
 * This menu is intended to store menu options for controlling the robot for any
 * sort of test or demo that integrates or operates the other aspects of the
 * robot. An example is provided below, and in the code:
 * { @code
 * menu.addItem("SquareDriveDemo", new MenuAction() {
 * public boolean action() {
 * // Any of the static resources from LocalResources can be used
 * navigation.travel(4 * TILE_WIDTH);
 * navigation.rotate(-90);
 * navigation.travel(4 * TILE_WIDTH);
 * navigation.rotate(-90);
 * navigation.travel(4 * TILE_WIDTH);
 * navigation.rotate(-90);
 * navigation.travel(4 * TILE_WIDTH);
 * return false;
 * }
 * });
 * }
 * 
 * @author Ryan Au
 */
public class DemosMenu extends SubMenu {
  private static DemosMenu menu;

  /**
   * This constructor should not be used to instantiate this class.
   */
  private DemosMenu() {
    super("Run the Demos", LocalResources.display);
  }

  /**
   * Creates and returns an instance of SubMenu that contains menu items specific
   * to the DemosMenu in the form of MenuAction instances or MenuCommand
   * instances.
   * Each of these actions are essentially run in the main thread, but the user
   * running the program gets to pick which commands to run at the times they wish
   * via the menu displayed on the LCD of the EV3.
   *
   * <p>
   * see @see ca.mcgill.ecse211.tools.MenuAction and see @see
   * ca.mcgill.ecse211.tools.MenuCommand for more information on actions.
   * 
   * @return Returns the same instance of DemosMenu every time.
   */
  public static SubMenu getInstance() {
    if (menu != null) {
      return menu;
    }

    menu = new DemosMenu();

    // An example of adding a demo/test to this class
    menu.addItem("SquareDriveDemo", new MenuAction() {
      public boolean action() {
        // Any of the static resources from LocalResources can be used
        navigation.travel(4 * TILE_WIDTH);
        navigation.rotate(-90);
        navigation.travel(4 * TILE_WIDTH);
        navigation.rotate(-90);
        navigation.travel(4 * TILE_WIDTH);
        navigation.rotate(-90);
        navigation.travel(4 * TILE_WIDTH);
        return false;
      }
    });

    // An example of adding a demo/test to this class
    menu.addItem("WifiUsageDemo", new MenuAction() {
      public boolean action() {
        wifiUsageDemo();
        return false;
      }
    });
    // --------------------Add more menu items here--------------------

    menu.addItem("First Map", new MenuAction() {
      public boolean action() {
        // place code here for the demo code here
        return false;
      }
    });

    return menu;
  }

  /**
   * This method provides a short example of using the wifi parameters, but also
   * creating a custom MenuAction.
   */
  protected static void wifiUsageDemo() {
    System.out.println(tnr.getHeight() + ":Length of Red Tunnel");
  }
}
