package ca.mcgill.ecse211.tools;

import ca.mcgill.ecse211.project.Display;
import java.util.ArrayList;
import lejos.hardware.Button;
import lejos.utility.TextMenu;

/**
 * Creates a menu that allows the user to add actions/methods as items to the
 * list displayed on the EV3.
 * 
 * <p>This menu system can be extended by first instantiating a SubMenu object,
 * then performing addItem() on this object to add menu items. These menu items
 * are always objects that implement the MenuAction and MenuCommand interfaces.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class SubMenu {

  private String title;
  private TextMenu menu;
  private ArrayList<MenuAction> actions;
  private ArrayList<String> actionNames;
  private boolean isExit;
  protected Display display;

  /**
   * Instantiates a menu with the given title.
   * 
   * @param title   the title of the menu
   * @param display the Display object to assist in writing custom messages to the
   *                TextLCD
   */
  public SubMenu(String title, Display display) {
    this.title = title;
    this.actions = new ArrayList<MenuAction>();
    this.actionNames = new ArrayList<String>();
    this.menu = null;
    this.display = display;
  }

  /**
   * Adds a new user-defined action to the menu, given the title of the menu
   * option, and an instance of the MenuAction interface.
   * 
   * @param title  name of menu item
   * @param action MenuAction instance to use when executing
   */
  public void addItem(String title, MenuAction action) {
    actions.add(action);
    actionNames.add(title);
  }

  /**
   * Adds a new user-defined action to the menu, given the title of the menu
   * option, and an instance of the MenuCommand interface. The difference between
   * this using MenuAction is that this will run a UI that prints the value of
   * getStatus(), binds the left and right buttons of the EV3 call setStatus(-1)
   * and setStatus(+1), and binds the down button to call action() of the
   * MenuCommand instance.
   * 
   * @param title   name of menu item displayed
   * @param command MenuCommand interface to utilize
   */
  public void addItem(final String title, final MenuCommand command) {
    actions.add(new MenuAction() {
      public boolean action() {
        String statusTitle = title;
        boolean endAction = false;
        display.writeNext(statusTitle, 1);
        while (!isExit && !endAction) {
          display.resetIndex(2);
          display.writeNext(command.getStatus(), true);
          display.writeNext("<<left -|+ right>>");
          display.writeNext("Up:exit, Down:act");

          switch (Button.getButtons()) {
            case Button.ID_LEFT:
              command.setStatus(-1);
              break;
            case Button.ID_RIGHT:
              command.setStatus(+1);
              break;
            case Button.ID_DOWN:
              command.action();
              break;
            case Button.ID_UP:
              endAction = true;
              break;
            default:
          }
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }
        }
        return false;
      }
    });
    actionNames.add(title);
  }

  /**
   * Activate the menu, displaying it to the robot's TextLCD and becoming
   * interactive. A menu item can be chosen, then the user can exit back into the
   * previous menu.
   * 
   * @return true indicates this menu exited properly, false if the menu fails
   */
  public boolean select() {
    String[] names = new String[actionNames.size()];
    menu = new TextMenu(actionNames.toArray(names), 1, this.title);
    int choice = 0;
    MenuAction chosen;
    isExit = false;
    do {
      choice = menu.select(choice);
      display.clear();
      if (choice == -1) {
        isExit = true;
      }
      try {
        chosen = actions.get(choice);
      } catch (Exception e) {
        chosen = null;
      }
      if (chosen != null) {
        isExit = chosen.action(); // Run the action specified
        display.clear();
      }
    } while (!isExit);
    return isExit;
  }
}