package ca.mcgill.ecse211.project.menu;

import ca.mcgill.ecse211.project.Display;
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
public class TestsMenu extends SubMenu {

  /**
   * Place all of your test items here
   */
  private TestsMenu() {
    super("Run the Tests", new Display());
  }

  public static TestsMenu createTests() {
    final TestsMenu menu = new TestsMenu();

    // --------------------Add items here--------------------

    menu.addItem("Test:Navigation", new MenuAction() {
      public boolean action() {
        // place code here for the tests
        return false;
      }
    });

    menu.addItem("Test:Action1", new MenuAction() {
      public boolean action() {
        return menu.test1();
      }
    });

    return menu;
  }

  public boolean test1() {
    // also can place code here, using the method in menu item

    return false; // true exits the menu
  }
}
