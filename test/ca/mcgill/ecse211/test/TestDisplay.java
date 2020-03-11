package ca.mcgill.ecse211.test;
import static ca.mcgill.ecse211.project.LocalResources.END_PROGRAM;
import static ca.mcgill.ecse211.project.LocalResources.lcd;

import lejos.hardware.lcd.TextLCD;

/**
 * This class extends the project's Display class, but should override the run
 * method to do something else more useful
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class TestDisplay extends ca.mcgill.ecse211.project.Display {
  
  public TestDisplay(TextLCD lcd) {
    super(lcd);
  }

  static final long T_INTERVAL = 100;
  private int lineIndex = 0;
  private boolean shouldWait = false;

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
}
