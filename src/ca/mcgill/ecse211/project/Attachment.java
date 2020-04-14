package ca.mcgill.ecse211.project;

/**
 * This class contains the commands to attach the stranded vehicle to the robot. 
 * 
 * <p>Once the stranded vehicle is detected, the robot will move around it and scan the ultrasonic 
 * distance to determine the long and short sides. It will then position itself on one of the long 
 * sides (using the travelTo method from the Navigation class), move into the stranded vehicle and 
 * use its forklift to lift the vehicle. See section 1.0 System Flowchart of the "Software 
 * Documentation Final" document for a deeper explanation of the algorithm. 
 * 
 * @author Norman Kong, Ryan Au, Kaustav Das Sharma
 */

public class Attachment {
  
  /**
   * An object (type TBD) that stores the long and short sides of the vehicle.
   * (a double array is used as a placeholder for now).
   */
  private double[] entryList;
  
  /**
   * Determines the long and short sides of the vehicle.
   */
  public void determineEntry() {
    // a ton of moving around and scanning with both US and light sensor.
  }
  
  /**
   * Moves into the stranded vehicle and lifts it. 
   */
  public void lift() {
    // activate motors
  }
  
}