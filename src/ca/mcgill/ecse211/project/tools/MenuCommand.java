package ca.mcgill.ecse211.project.tools;

public interface MenuCommand {
  
  /**
   * The updated status of the adjustable value.
   * 
   * @return a String, preferably containing a new value each time
   */
  public String getStatus();
  
  /**
   * Change your value based on this factor.  It usually gives -1 or +1, 
   * so the value can be incremented or decremented.
   * 
   * @param changeFactor -1 for left button or +1 for right button
   */
  public void setStatus(int changeFactor);
  
  /**
   * An action to perform.  It can be anything, but try not to start any new threads here.
   *  
   */
  public void action();
}
