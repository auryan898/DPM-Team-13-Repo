package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.mcgill.ecse211.project.Display;
import ca.mcgill.ecse211.project.LocalResources;
import ca.mcgill.ecse211.test.TestDisplay;

public class DisplayTester {

  private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
  private final PrintStream oldOut = System.out;
  private Display display;

  @Before
  public void setup() {
    System.setOut(new PrintStream(outStream));
    LocalResources.display = new TestDisplay();
    display = LocalResources.display;
  }

  @After
  public void destroy() {
    System.setOut(oldOut);
  }

  @Test
  public void test() {
    assertEquals("Initial line index zero", 0, display.getLineIndex());

    display.clear();
  }

}
