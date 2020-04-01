package ca.mcgill.ecse211.tools;

import java.util.ArrayList;

import ca.mcgill.ecse211.project.Display;
import lejos.hardware.Button;
import lejos.utility.TextMenu;

/**
 * A menu that allows the user to add actions as items to the list instead of
 * just strings as is the case with lejos.utility.TextMenu.
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

  public SubMenu(String title) {
    this.title = title;
    this.actions = new ArrayList<MenuAction>();
    this.actionNames = new ArrayList<String>();
    this.menu = null;
  }

  public void addItem(String title, MenuAction action) {
    actions.add(action);
    actionNames.add(title);
  }

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