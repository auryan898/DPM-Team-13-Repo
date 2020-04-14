package ca.mcgill.ecse211.project.menu;

import ca.mcgill.ecse211.project.Display;
import ca.mcgill.ecse211.project.LocalResources;
import ca.mcgill.ecse211.tools.MenuAction;
import ca.mcgill.ecse211.tools.SubMenu;

/**
 * This is the Tests Menu, named "Tests Menu" from the Main Menu options. By
 * default, this class contains an example item, and space is left to the next
 * programmers to fill in the further menu options that they wish to use.
 * 
 * <p>
 * This menu is intended to store menu options for controlling the robot for any
 * sort of test that integrates or operates the other aspects of the
 * robot.
 * 
 * <p>
 * This menu is originally provided as an extra sub menu to store routines that
 * are specifically for testing, however this menu is does not need to be used,
 * and it does not need to be used for this purpose. Instead, every routine can
 * be stored in the DemosMenu.
 * 
 * @see    ca.mcgill.ecse211.project.menu.DemosMenu
 * 
 * @author Ryan Au
 */
public class TestsMenu extends SubMenu {
  private static TestsMenu menu;

  private TestsMenu() {
    super("Tests Menu", LocalResources.display);
  }

  /**
   * Creates and returns an instance of SubMenu that contains menu items specific
   * to the TestsMenu in the form of MenuAction instances or MenuCommand
   * instances.
   * Each of these actions are essentially run in the main thread, but the user
   * running the program gets to pick which commands to run at the times they wish
   * via the menu displayed on the LCD of the EV3.
   *
   * <p>
   * see @see ca.mcgill.ecse211.tools.MenuAction and see @see
   * ca.mcgill.ecse211.tools.MenuCommand for more information on actions.
   * 
   * @return Returns the same instance of TestsMenu every time.
   */
  public static TestsMenu getInstance() {
    if (menu != null) {
      return menu;
    }
    menu = new TestsMenu();

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

  /**
   * An example test method that can be run by a menu option.
   * 
   * @return true to exit the current menu, false to stay in it.
   */
  public boolean test1() {
    // also can place code here, using the method in menu item
    return false; // return true exits the menu
  }
}
