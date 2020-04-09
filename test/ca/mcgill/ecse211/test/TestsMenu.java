package ca.mcgill.ecse211.test;

import ca.mcgill.ecse211.project.Display;
import ca.mcgill.ecse211.tools.MenuAction;
import ca.mcgill.ecse211.tools.SubMenu;

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
