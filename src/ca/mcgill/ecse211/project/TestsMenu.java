package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;
import ca.mcgill.ecse211.project.menu.MenuAction;
import ca.mcgill.ecse211.project.menu.SubMenu;

public class TestsMenu extends SubMenu {

  /**
   * Place all of your test items here
   */
  private TestsMenu() {
    super("Run the Tests");
  }
  public static SubMenu createTests() {
    SubMenu menu = new TestsMenu();
    
    // --------------------Add items here--------------------
    
    menu.addItem("Test:Navigation", new MenuAction() {
      public boolean action() {
        // place code here for the test code here
        return false;
      }
    });
    
    return menu;
  }
}
