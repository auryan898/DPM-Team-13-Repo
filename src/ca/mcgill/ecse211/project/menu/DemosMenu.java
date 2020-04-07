package ca.mcgill.ecse211.project.menu;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.LocalResources;
import ca.mcgill.ecse211.tools.MenuAction;
import ca.mcgill.ecse211.tools.SubMenu;

public class DemosMenu extends SubMenu {

  /**
   * Place all of your demo items here.
   */
  private DemosMenu() {
    super("Run the Demos",LocalResources.display);
  }
  
  // TODO: javadoc
  public static SubMenu createDemos() {
    SubMenu menu = new DemosMenu();
    
    // --------------------Add items here--------------------
    
    menu.addItem("First Map", new MenuAction() {
      public boolean action() {
        // place code here for the demo code here
        return false;
      }
    });
    
    return menu;
  }
}
