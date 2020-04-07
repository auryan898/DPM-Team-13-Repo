package ca.mcgill.ecse211.project;

/**
 * This class contains the commands to attach the stranded vehicle to the robot. 
 * 
 * <p>Once the stranded vehicle is detected, the robot will scan around it, determine the long and 
 * short sides, position itself on one of the long sides (which is just the travelTo method), move 
 * into the stranded vehicle and use its forklift to lift the vehicle. 
 * 
 * @author Norman Kong, Ryan Au, Kaustav Das Sharma
 */

public class Attachment {
  
  /**
   * An object (type TBD) that stores the long and short sides of the vehicle.
   * (a double array is used as a placeholder for now).
   */
  private double[] entry;
  
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