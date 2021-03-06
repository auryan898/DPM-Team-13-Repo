package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.redTeam;

import java.util.ArrayList;

/**
 * This class is a template for the proposed searching algorithm.
 * 
 * <p>The robot will rotate and read ultrasonic distances to detect where the
 * stranded vehicle might be, which is what we call "scanning". It will then travel to the 
 * potential waypoints that may have the vehicle, and determine whether or not it is the vehicle. If
 * it is, the robot will use the Attachment class.  If not, it will perform a new scan and continue 
 * to the next detected objects. For documentation on algorithms to be implemented, see section 1.0,
 * the sequence diagram from section 4.0, and 6.3 from the "Software Documentation Final" document.
 * 
 * @author Norman Kong, Ryan Au, Kaustav Das Sharma
 *
 */
public class Search {

  /**
   * ArrayList that stores the data from the scan.
   */
  private ArrayList<Double> scannedDistances = new ArrayList<Double>();

  /**
   * Field of view (angle).
   */
  private final double fieldOfView = 180.0;

  /**
   * Vehicle color.
   */
  private final String vehicleColor;

  /**
   * The constructor for the Search class.
   */
  public Search() {

    // initialize the color of the vehicle
    if (redTeam == 13) {
      vehicleColor = "Red";
    } else {
      vehicleColor = "Green";
    }

  }

  /**
   * Scans the playing field, storing the ultrasonic distances and the
   * corresponding odometry
   * positions in ArrayLists.
   */
  public void scan() {

    // turn robot for fieldOfView degrees

    // scan distances

    // store distances in scannedDistances

    // store odometry position

  }

  /**
   * feature mapping: read the distances in the ArrayList and categorize them as
   * either object to
   * visit or non object.
   */
  public void featureMapping() {

  }

  /**
   * Detect the color of the object and determine if it is the stranded vehicle or
   * not.
   * 
   * @return True if the color detected corresponds to our assigned vehicle. False
   *         otherwise.
   */
  public boolean identifyObject() {

    // read color assuming the object will be one of the ring colors (not sure if
    // this is true)
    String color = ColorDetection.getObjectColor();

    // assuming the team color is the same color as the vehicle
    if (color.equals(vehicleColor)) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Updates the ArrayList of distances to prevent visiting the same objects
   * twice.
   */
  public void updateDistances() {

  }

  /**
   * Polar coordinates to Cartesian coordinates.
   * 
   * @param  polar an array of the form [r, theta] in centimeters and degrees
   * @return       an array of the form [x,y] for the cartesian point (x,y)
   */
  public double[] polarToCartesian(double[] polar) {
    return new double[] {
        polar[0] * Math.cos(Math.toRadians(polar[1])),
        polar[0] * Math.sin(Math.toRadians(polar[1]))
    };
  }

}
