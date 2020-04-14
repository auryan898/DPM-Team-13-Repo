package ca.mcgill.ecse211.pctest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    LocalizerTest.class,
    ColorDetectionTest.class,
    NavigationTest.class,
    FieldLayoutsUnitTest.class
})
public class RunAllTests {

}
