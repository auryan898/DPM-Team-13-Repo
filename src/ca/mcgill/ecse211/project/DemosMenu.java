package ca.mcgill.ecse211.project;

import ca.mcgill.ecse211.project.tools.MenuAction;
import ca.mcgill.ecse211.project.tools.SubMenu;

public class DemosMenu extends SubMenu {
  
  // test comment

  /**
   * Place all of your demo items here
   */
  public DemosMenu() {
    super("Run the Demos");
    
    // --------------------Add items here--------------------

    this.addItem("First Map", new MenuAction() {
      public boolean action() {
        // place code here for the demo code here
        return false;
      }
    });
  }
  
}
