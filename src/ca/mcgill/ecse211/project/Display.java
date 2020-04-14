package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.LocalResources.*;

/**
 * Wrapper class for the TextLCD on the ev3 robot. Includes very useful methods
 * such as {@code writeNext(String line)} or its equivalent {@code println(String line)} and a
 * {@code clear()}.
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class Display implements Runnable {
  static final long T_INTERVAL = 100;
  protected int lineIndex = 0;
  protected boolean shouldWait = false;

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
    lcd.clear();
    resetIndex();
  }

  /**
   * Resets the default line to start writing text to the LCD, using
   * writeNext(String) or println(String).
   * 
   * @param defaultY the y-value of the LCD display from which to start writing
   */
  public void resetIndex(int defaultY) {
    this.lineIndex = defaultY;
  }

  /**
   * Resets the default line to start writing text to the 0 y-value.
   */
  public void resetIndex() {
    resetIndex(0);
  }

  /**
   * Writes String to the y-value indicated by the index, limit 0-7.
   * 
   * @param line  String to print
   * @param index which y-value index to print on (0 to 7 inclusive)
   */
  public void println(String line, int index) {
    writeNext(line, index);
  }

  /**
   * Tries to write to the next available line.
   * 
   * @param line String to print out
   */
  public void println(String line) {
    writeNext(line);
  }

  /**
   * Tries to write to the next available line.
   * 
   * @param line String to print out
   */
  public void writeNext(String line) {
    writeNext(line, getLineIndex());
    lineIndex = getLineIndex() + 1;
  }

  /**
   * Writes String to the line indicated by the index, limit 0-7.
   * 
   * @param line  String to print
   * @param index line to print on 0 to 7 inclusive
   */
  public void writeNext(String line, int index) {
    lcd.clear(index);
    lcd.drawString(line, 0, index);
  }

  /**
   * Writes the String to the lcd screen, but writes a new line at each '\n' it
   * finds. If wordWrap is set to true, it will do this. It isn't actually word
   * wrapping, but will properly start a new line at each '\n'.
   * 
   * @param line     the string to be written
   * @param wordWrap true to use this behavior
   */
  public void writeNext(String line, boolean wordWrap) {
    if (!wordWrap) {
      writeNext(line);
    }
    String[] lines = line.split("\n");
    for (int i = 0; i < lines.length; i++) {
      writeNext(lines[i]);
    }
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

  /**
   * Gets the internal counter of the next line to print to, initially set by
   * resetIndex(int) and incremented with each usage of println(String) or
   * writeNext(String). Note: writeNext(String,int) or println(String,int) does
   * not increment this counter.
   * 
   * @return the next line to print to
   */
  public int getLineIndex() {
    return lineIndex;
  }
}
