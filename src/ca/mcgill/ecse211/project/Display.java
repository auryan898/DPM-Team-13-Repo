package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

/**
 * Wrapper class for the TextLCD on the ev3 robot. Includes very useful methods 
 * such as writeNext(String line) or its equivalent println(String line) and a clear().
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Display implements Runnable {
  private static final long T_INTERVAL = 100;
  private int lineIndex = 0;

  /**
   * Display thread loop that display the status of several 
   * items of relevant information.
   * 
   */
  public void run() {
    resetIndex();
    writeNext("Nothing to see here.");
    
    while (true) {
      resetIndex(1);
      
      try {
        Thread.sleep(T_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }
  }
  
  public void clear() {
    lcd.clear();
    resetIndex();
  }
  
  public void resetIndex(int val) {
    this.lineIndex = val;
  }
  
  /**
   * Resets the line writing index for the thread run loop.
   */
  public void resetIndex() {
    resetIndex(0);
  }

  /**
   * Writes String to the line indicated by the index, limit 0-7.
   * @param line String to print
   * @param index line to print on 0 to 7 inclusive
   */
  public void println(String line, int index) {
    writeNext(line,index);
  }

  /**
   * Tries to write to the next available line.
   * @param line String to print out
   */
  public void println(String line) {
    writeNext(line);
  }
  
  /**
   * Tries to write to the next available line.
   * @param line String to print out
   */
  public void writeNext(String line) {
    writeNext(line,lineIndex);
    lineIndex++;
  }
  
  /**
   * Writes String to the line indicated by the index, limit 0-7.
   * @param line String to print
   * @param index line to print on 0 to 7 inclusive
   */
  public void writeNext(String line, int index) {
    lcd.clear(index);
    lcd.drawString(line, 0, index);
  }
}
