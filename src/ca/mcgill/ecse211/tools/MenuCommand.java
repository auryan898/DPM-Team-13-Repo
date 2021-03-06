package ca.mcgill.ecse211.tools;

/**
 * An interface to define the menu item which SubMenu will use to create a UI.
 * This UI will repeatedly print the (updated) value of getStatus(), binds the
 * left and right buttons of the EV3 to call setStatus(-1) and setStatus(+1),
 * and binds the down button to call action() of the MenuCommand instance.
 * 
 * <p>
 * The UP Button on the robot exits the generated prompt.
 * 
 * @author Ryan Au
 *
 */
public interface MenuCommand {

  /**
   * The updated status of the adjustable value.
   * 
   * @return a String, preferably containing a new value each time
   */
  public String getStatus();

  /**
   * Change your value based on this factor. It usually passes -1 or +1,
   * so the value can be incremented or decremented.
   * 
   * @param changeFactor -1 for left button or +1 for right button
   */
  public void setStatus(int changeFactor);

  /**
   * An action to perform. It can be anything, but try not to start any new
   * threads here.
   * 
   */
  public void action();
}
