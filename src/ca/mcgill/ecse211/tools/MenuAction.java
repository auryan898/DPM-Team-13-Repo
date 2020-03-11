package ca.mcgill.ecse211.tools;

/**
 * Interface where a single method action() is defined, something to do for a
 * listed item in the SubMenu.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public interface MenuAction {
  
  /**
   * Do something.  Return false if you wish to stay in the current menu.  
   * Return true to exit the original SubMenu.select() call, and possibly 
   * go back to a parent menu.
   * 
   * @return true to exit menu, false to stay
   */
  public boolean action();
}