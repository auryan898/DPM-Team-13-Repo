package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;

import ca.mcgill.ecse211.project.menu.MainMenu;

/**
 * The main class that includes basic initialization methods for initializing
 * navigation, odometry, and the robot pilot, along with sensors.
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
    
    // Initialize Main Menu
    MainMenu mainMenu = MainMenu.createMainMenu();

    // Start main thread
    while (!END_PROGRAM) {
      END_PROGRAM = mainMenu.select();

      try {
        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

  }
}
