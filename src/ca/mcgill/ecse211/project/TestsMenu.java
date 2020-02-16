package ca.mcgill.ecse211.project;

import ca.mcgill.ecse211.project.tools.MenuAction;
import ca.mcgill.ecse211.project.tools.SubMenu;

public class TestsMenu extends SubMenu {

  public TestsMenu() {
    super("Tests Menu");
    // --------------------Add items here--------------------

    // add menu actions from this point, make sure they return false;
    this.addItem("Test:Localize", new MenuAction() {
      public boolean action() {
        // place test code here.
        return false;
      }
    });
  }

}
