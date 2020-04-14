package ca.mcgill.ecse211.pctest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    LocalizerTest.class,
    ColorDetectionTest.class,
    NavigationTest.class
})
public class RunAllTests {

}
