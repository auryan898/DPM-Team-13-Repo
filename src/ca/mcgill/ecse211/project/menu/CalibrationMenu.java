package ca.mcgill.ecse211.project.menu;

import static ca.mcgill.ecse211.project.LocalResources.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ca.mcgill.ecse211.project.LocalResources;
import ca.mcgill.ecse211.project.Navigation;
import ca.mcgill.ecse211.tools.MenuCommand;
import ca.mcgill.ecse211.tools.SubMenu;
import lejos.robotics.SampleProvider;

/**
 * This is the Calibration Menu, named "Calibration Tools" in the Main Menu
 * options. By default, this class contains several routines which can be used
 * to easily re-calibrate coded parameters of the robot.
 * 
 * <p>
 * This menu is intended to store menu options for calibrating the robot's coded
 * parameters of its physical design. An example using MenuCommand is provided
 * below, and many more are stored in this class:
 * 
 * <pre>
 * menu.addItem("SquareDriveDemo", new MenuCommand() {
 *   public String getStatus() {
 *     return "" + BASE_WIDTH;
 *   }
 * 
 *   public void setStatus(int changeFactor) {
 *     BASE_WIDTH += Math.signum(changeFactor) * 0.01;
 *   }
 * 
 *   public void action() {
 *     updateNavigation();
 *     localNavigation.setAngularSpeed(100);
 *     localNavigation.setAngularAcceleration(50);
 *     localNavigation.rotate(360);
 *   }
 * });
 * </pre>
 * 
 * @author Ryan Au
 */
public class CalibrationMenu extends SubMenu {
  private static CalibrationMenu menu;

  /**
   * The navigation instance local to this class, constantly being updated by
   * various calibration methods, based on an updated BASE_WIDTH and WHEEL_RADIUS
   * from LocalResources.
   */
  protected static Navigation localNavigation;

  /**
   * This constructor should not be used to instantiate this class.
   */
  private CalibrationMenu() {
    super("Calibration Tools", LocalResources.display);
  }

  /**
   * Updates the localNavigation instance based on the current WHEEL_RADIUS and
   * BASE_WIDTH from LocalResources.
   */
  protected void updateNavigation() {
    localNavigation = LocalResources.initNavigation(WHEEL_RADIUS, BASE_WIDTH, motorLeft,
        motorRight);
  }

  /**
   * Creates and returns an instance of SubMenu that contains menu items specific
   * to the CalibrationMenu in the form of MenuAction instances or MenuCommand
   * instances.
   * <p>
   * Each of these actions are essentially run in the main thread, but the user
   * running the program gets to pick which commands to run at the times they wish
   * via the menu displayed on the LCD of the EV3.
   *
   * 
   * @see    ca.mcgill.ecse211.tools.MenuAction
   * @see    ca.mcgill.ecse211.tools.MenuCommand
   * 
   * @return Returns the same instance of
   *         CalibrationMenu every time.
   */
  public static SubMenu getInstance() {
    if (menu != null) {
      return menu;
    }

    menu = new CalibrationMenu();

    // Calibrates Wheel Radius, it should move backwards one tile
    menu.addItem("Wheel Radius", menu.calibrateWheelRadius());

    // Tests Ultrasonic Distance
    menu.addItem("U.S Dist Offset", menu.calibrateUltrasonicOffset());

    // Calibrates the Base Width
    menu.addItem("Base Width", menu.calibrateBaseWidth());

    // Checks the precision of the ultrasonic sensor
    menu.addItem("Ultrasonic Comparer", menu.calibrateUsDistanceTravel());

    // Helps in determining colors that the color sensor can identify
    menu.addItem("Color Gaussian", menu.calibrateColorSensor());

    // -----Add more menu items below this line---

    return menu;
  }

  /**
   * Creates a calibration tool that allows the user to identify the mean R, G,
   * and B values of a given color detected by the physical color sensor. It will
   * display on screen these values, but write the corresponding data to a file
   * and then subsequently delete the stored data upon file writing.
   *
   * <p>
   * On the robot, the RIGHT Button adds new data points to the temporary internal
   * array. The LEFT Button removes the previous data point from this internal
   * array. The DOWN Button calculates the mean and writes this along with the
   * data to a csv file, resetting the internal values for a new set of readings.
   * 
   * <p>
   * The UP Button exits the MenuCommand prompt.
   * 
   * @return a MenuCommand object to be added to a SubMenu instance
   */
  protected MenuCommand calibrateColorSensor() {
    return new MenuCommand() {
      ArrayList<Float[]> data = new ArrayList<>();
      float[] buffer = { 0, 0, 0 };
      float[] mean = { 0, 0, 0 };

      /**
       * Left button passes -1, which removes the last data point.
       * Right button passes +1, which adds a new data point.
       */
      public void setStatus(int changeFactor) {
        LocalResources.display.writeNext("Wait...", 2);
        try {
          Thread.sleep(1000); // A pause for stability
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        switch ((int) Math.signum(changeFactor)) {
          case -1:
            if (data.size() > 0) {
              data.remove(data.size() - 1);
            }
            break;
          case +1:
            colorSensor.fetchSample(buffer, 0);
            for (int i = 0; i < buffer.length; i++) {
              buffer[i] = (buffer[i] < 1) ? buffer[i] : Float.NaN;
            }
            data.add(new Float[] { buffer[0], buffer[1], buffer[2] });
            break;
          default:
        }
        calculateMeanOfColors(data, mean);
      }

      /**
       * Prints the running mean to the screen.
       */
      public String getStatus() {
        return "" + (int) (mean[0] * 1000) + "|\n" + "." + (int) (mean[1] * 1000) + "|\n" + "."
            + (int) (mean[2] * 1000) + "|";
      }

      /**
       * Down button writes the mean and data to a csv file on the robot.
       */
      public void action() {
        LocalResources.display.writeNext("Writing File...", 2);
        writeColorDataFile();
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          return;
        }
      }

      public void writeColorDataFile() {
        if (data.size() <= 0) {
          return;
        }
        FileWriter fr = null;
        try {
          fr = new FileWriter("ColorReadings" + System.currentTimeMillis() + ".csv");
          fr.write("Red,Green,Blue\n");
          for (int i = 0; i < data.size(); i++) {
            Float[] dat = data.get(i);
            fr.write(dat[0] + "," + dat[1] + "," + dat[2] + "\n");
          }
          fr.write("RedMean,GreenMean,BlueMean\n");
          fr.write(mean[0] + "," + mean[1] + "," + mean[2] + "\n");
          data.clear();
          mean[0] = 0;
          mean[1] = 0;
          mean[2] = 0;

        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (fr != null) {
            try {
              fr.close();
            } catch (IOException e) {
            }
          }
        }
      }
    };
  }

  /**
   * Given an ArrayList of a Float array, each of length 3, this method will
   * calculate the mean of the three corresponding data, and assign it to the mean
   * array, which should also be of length 3.
   * 
   * <p>
   * Originally intended to calculate the three means for R, G, and B, from the
   * RGB values collected from the color sensor.
   * 
   * 
   * @param data ArrayList of Float arrays, each of length 3
   * @param mean a float array to store the 3 means from the data
   */
  public void calculateMeanOfColors(ArrayList<Float[]> data, float[] mean) {
    float r = 0;
    float g = 0;
    float b = 0;
    for (int i = 0; i < data.size(); i++) {
      Float[] dat = data.get(i);
      r += dat[0];
      g += dat[1];
      b += dat[2];
    }
    mean[0] = r / (data.size());
    mean[1] = g / (data.size());
    mean[2] = b / (data.size());
  }

  /**
   * Creates a calibration tool that allows the user to have the robot travel a
   * specific distance determined by the stored and detected values from the
   * ultrasonic sensor. Note this tool does use the up to date US_OFFSET constant.
   *
   * <p>
   * On the robot, the RIGHT Button stores the distance detected by the ultrasonic
   * sensor as the baseline, the 0 distance. The LEFT Button stores the distance
   * between the detected distance and the baseline. The DOWN Button will have the
   * robot travel the distance stored by the LEFT Button (does not use ultrasonic
   * sensor in this action).
   * 
   * <p>
   * The UP Button exits the MenuCommand prompt.
   * 
   * @return a MenuCommand object to be added to a SubMenu instance
   */
  private MenuCommand calibrateUsDistanceTravel() {
    return new MenuCommand() {
      private SampleProvider sampler = ultrasonicSensor;
      private float[] buffer = { 0 };
      float storedDist = 0;
      float storedPoint = 0;

      /**
       * Left button passes -1, stores baseline.
       * Right button passes +1, stores distance from baseline.
       */
      public void setStatus(int changeFactor) {
        switch ((int) Math.signum(changeFactor)) {
          case -1:
            storedDist = buffer[0] * 100 + US_OFFSET * 100 - storedPoint;
            break;
          case +1:
            storedPoint = buffer[0] * 100 + US_OFFSET * 100;
            break;
          default:
        }
      }

      /**
       * Display the distance detected, relative to baseline.
       */
      public String getStatus() {
        sampler.fetchSample(buffer, 0);
        return "" + (buffer[0] * 100 + US_OFFSET * 100 - storedPoint);
      }

      /**
       * Down button makes the robot compensate to match the storedDist
       */
      public void action() {
        updateNavigation();
        localNavigation.setLinearAcceleration(70);
        localNavigation.setLinearSpeed(100);
        localNavigation.travel(-storedDist + (buffer[0] * 100 + US_OFFSET * 100 - storedPoint));
      }
    };
  }

  /**
   * Creates a calibration tool that allows the user to recalibrate the base
   * width, actively changing the BASE_WIDTH constant of the LocalResources class.
   *
   * <p>
   * On the robot, the RIGHT Button increases the BASE_WIDTH by a factor of
   * 0.01cm. The LEFT Button decrements the BASE_WIDTH by 0.01cm. The DOWN Button
   * will cause the robot to attempt to rotate 360 degrees, a full circle.
   * 
   * <p>
   * If the robot cannot rotate enough, increase the BASE_WIDTH, if it rotates too
   * much, then decrease the BASE_WIDTH.
   * 
   * <p>
   * The UP Button exits the MenuCommand prompt.
   * 
   * @return a MenuCommand object to be added to a SubMenu instance
   */
  protected MenuCommand calibrateBaseWidth() {
    return new MenuCommand() {

      /**
       * Prints the base width to the lcd display.
       */
      public String getStatus() {
        return "" + BASE_WIDTH;
      }

      /**
       * Left button passes -1, decrements the BASE_WIDTH.
       * Right button passes +1, increments the BASE_WIDTH.
       */
      public void setStatus(int changeFactor) {
        BASE_WIDTH += Math.signum(changeFactor) * 0.01;
      }

      public void action() {
        updateNavigation();
        localNavigation.setAngularSpeed(100);
        localNavigation.setAngularAcceleration(50);
        localNavigation.rotate(360);
      }
    };
  }

  /**
   * Creates a calibration tool that allows the user to recalibrate the ultrasonic
   * offset, actively changing the US_OFFSET constant of the LocalResources
   * class.
   *
   * <p>
   * On the robot, the RIGHT Button increases the US_OFFSET by a factor of
   * 0.001cm. The LEFT Button decrements the US_OFFSET by 0.001cm. The DOWN Button
   * will cause the robot to detect the object in front of it, usually a wall,
   * then travel the needed distance to be one TILE_WIDTH away from this wall.
   * 
   * <p>
   * If the robot cannot get close enough, decrease the US_OFFSET, if it gets too
   * far away, then increase the US_OFFSET. The goal is to get the robot's pivot
   * point to the TILE_WIDTH distance away from the obstacle.
   * 
   * <p>
   * The UP Button exits the MenuCommand prompt.
   * 
   * @return a MenuCommand object to be added to a SubMenu instance
   */
  protected MenuCommand calibrateUltrasonicOffset() {
    return new MenuCommand() {
      private SampleProvider sampler = ultrasonicSensor;
      private float[] buffer = { 0 };
      private float offset = US_OFFSET;

      /**
       * Left button passes -1 which decrements ultrasonic offset.
       * Right button passes +1 which increments ultrasonic offset.
       */
      public void setStatus(int changeFactor) {
        offset += 0.001f * Math.signum(changeFactor); // in centimeter
        US_OFFSET = offset;
      }

      /**
       * Prints the US_OFFSET to the display, along with the current detected
       * distance.
       */
      public String getStatus() {
        sampler.fetchSample(buffer, 0);
        return "" + (offset * 100) + "|" + (buffer[0] * 100 + offset * 100);
      }

      /**
       * Down button updates navigation then makes the robot travel to compensate its
       * position.
       */
      public void action() {
        updateNavigation();
        localNavigation.setLinearAcceleration(70);
        localNavigation.setLinearSpeed(100);
        localNavigation.travel(-TILE_WIDTH + (buffer[0] * 100 + offset * 100));
      }
    };
  }

  /**
   * Creates a calibration tool that allows the user to recalibrate the wheel
   * radius, actively changing the WHEEL_RADIUS constant of the LocalResources
   * class.
   *
   * <p>
   * On the robot, the RIGHT Button increases the WHEEL_RADIUS by a factor of
   * 0.01cm. The LEFT Button decrements the WHEEL_RADIUS by 0.01cm. The DOWN
   * Button
   * will cause the robot to attempt to travel backwards by one TILE_WIDTH;
   * 
   * <p>
   * If the robot cannot get close enough to the line, decrease the WHEEL_RADIUS,
   * if it gets too far away, then increase the WHEEL_RADIUS. The goal is to get
   * the robot to travel one TILE_WIDTH away from its starting position.
   * 
   * <p>
   * The UP Button exits the MenuCommand prompt.
   * 
   * @return a MenuCommand object to be added to a SubMenu instance
   */
  protected MenuCommand calibrateWheelRadius() {
    return new MenuCommand() {

      /**
       * Left button passes -1 which decrements wheel radius.
       * Right button passes +1 which increments wheel radius.
       */
      public void setStatus(int changeFactor) {
        WHEEL_RADIUS += Math.signum(changeFactor) * 0.01f;
      }

      /**
       * Prints the wheel radius to the lcd.
       */
      public String getStatus() {
        return "" + WHEEL_RADIUS;
      }

      /**
       * Down button calls this command, updates navigation and the robot then travels
       * backwards.
       */
      public void action() {
        updateNavigation();
        localNavigation.setLinearAcceleration(100);
        localNavigation.setLinearSpeed(100);
        localNavigation.travel(-TILE_WIDTH);
      }
    };
  }

}
