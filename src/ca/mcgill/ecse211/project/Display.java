package ca.mcgill.ecse211.project;

import lejos.hardware.lcd.TextLCD;
import static ca.mcgill.ecse211.project.LocalResources.*;
import static ca.mcgill.ecse211.project.Resources.lcd;

/**
 * Wrapper class for the TextLCD on the ev3 robot. Includes very useful methods 
 * such as writeNext(String line) or its equivalent println(String line) and a clear().
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Display implements Runnable {
  static final long T_INTERVAL = 100;
  protected int lineIndex = 0;
  protected boolean shouldWait = false;
  protected TextLCD lcd;
  
  public Display(TextLCD lcd) {
    this.lcd = lcd;
  }

  /**
   * Display thread loop that display the status of several 
   * items of relevant information.
   * 
   */
  public void run() {
    resetIndex();
    writeNext("Nothing to see here.");
    
    while (!END_PROGRAM) {
      resetIndex(1);
      
      
      try {
        Thread.sleep(T_INTERVAL);
        if (shouldWait) {
          this.wait();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }
  }
  
  public void clear() {
    this.lcd.clear();
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
  

  /**
   * Shows the text on the LCD, line by line.
   * 
   * @param strings comma-separated list of strings, one per line
   */
  public void showText(String... strings) {
    lcd.clear();
    for (int i = 0; i < strings.length; i++) {
      lcd.drawString(strings[i], 0, i);
    } // for loop
  } // end showText method

  /**
   * unpauses the display.
   */
  public void restart() {
    shouldWait = false;
    this.notify();
  }
  
  /**
   * unpauses the display.
   * 
   */
  public void unpause() {
    restart();
  }

  /**
   * Tells the thread to wait, to be later notified and run again.
   */
  public void pause() {
    shouldWait = true;
  }
}
