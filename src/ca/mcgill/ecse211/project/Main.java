package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.menu.MainMenu;

/**
 * The main class of the ECSE211 search and rescue project. This class is where
 * a user would write method calls for utilizing navigation, odometry, and the
 * robot pilot, along with sensors.
 * 
 * <p>
 * Typically the programmer who writes in this
 * class will write a single set of commands for the robot to run in succession
 * for this project, and that is possible to do. However a menu system has been
 * implemented by creating subclasses of
 * {@link ca.mcgill.ecse211.tools.SubMenu},
 * and then adding menu items (that are to be executed) to that menu. See the
 * package
 * {@link ca.mcgill.ecse211.project.menu} for more information on each existing
 * menu.
 * 
 * <p>
 * <b>Go to</b>
 * {@link ca.mcgill.ecse211.project.menu.DemosMenu}
 * <b>for example usage of menu system</b>
 * 
 * <p>
 * The goal of the project is to create a robot that can search for a stranded
 * vehicle, attach to it and return it back to the starting base. A visual
 * provided
 * by the client is shown here:
 * 
 * <img src="" placeholder="image of competition playing field">
 * 
 * <p>
 * The program begins by receiving the game parameters from the game marshal.
 * The robot must
 * localize, travel across the tunnel to the search zone, and search for its
 * assigned stranded
 * vehicle. Once found, it will attach to the vehicle and return to the zone in
 * which it started.
 * The task must be completed within 5 minutes, and without colliding into any
 * obstacle, including
 * the opposing team's robot.
 * 
 * <p>
 * Please see the document "Software Documentation Final" for further details of
 * the software architecture and its functionality.
 * 
 * @see    ca.mcgill.ecse211.project.menu.MainMenu
 * @see    ca.mcgill.ecse211.project.menu.DemosMenu
 * @see    ca.mcgill.ecse211.project.menu.CalibrationMenu
 * @see    ca.mcgill.ecse211.project.menu.TestsMenu
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Main {
  // Every user made thread should define its own sleep interval
  private static final long T_INTERVAL = 10;

  /**
   * Entry point of the program.
   * 
   * @param args command-line arguments
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    if (!END_PROGRAM && ENABLE_DEBUG_WIFI_PRINT) {
      System.out.println("Starting main program");
    }
    // -----Begin writing your code below this line-----

    // Either run code such as:
    // squareDriverTest();

    // Or Initialize the MainMenu (see this class for example usage)
    MainMenu mainMenu = MainMenu.getInstance();
    // And place your code in any of the menu classes: DemosMenu, TestsMenu, CalibrationMenu
    // as menu options to be selected.

    // Start main thread
    while (!END_PROGRAM) {
      // Displays the options for the MainMenu (ca.mcgill.ecse211.project.menu)
      END_PROGRAM = mainMenu.select(); // comment this out if not using menus

      try {
        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

  }

  /**
   * An example of the square driver test, that can be used for calibratin robot
   * parameters, or testing odometry. It can be called from the main method.
   */
  private static void squareDriverTest() {
    navigation.travel(4 * TILE_WIDTH);
    navigation.rotate(-90);
    navigation.travel(4 * TILE_WIDTH);
    navigation.rotate(-90);
    navigation.travel(4 * TILE_WIDTH);
    navigation.rotate(-90);
    navigation.travel(4 * TILE_WIDTH);
  }
}
