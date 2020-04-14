package ca.mcgill.ecse211.pctest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.mcgill.ecse211.project.FieldLayouts;
import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class FieldLayoutsUnitTest {
  private static String[] expectedLayoutKeys = {
      "RedTeam",
      "RedCorner",
      "GreenTeam",
      "GreenCorner",
      "Red_LL_x",
      "Red_LL_y",
      "Red_UR_x",
      "Red_UR_y",
      "Green_LL_x",
      "Green_LL_y",
      "Green_UR_x",
      "Green_UR_y",
      "Island_LL_x",
      "Island_LL_y",
      "Island_UR_x",
      "Island_UR_y",
      "TNR_LL_x",
      "TNR_LL_y",
      "TNR_UR_x",
      "TNR_UR_y",
      "TNG_LL_x",
      "TNG_LL_y",
      "TNG_UR_x",
      "TNG_UR_y",
      "SZR_LL_x",
      "SZR_LL_y",
      "SZR_UR_x",
      "SZR_UR_y",
      "SZG_LL_x",
      "SZG_LL_y",
      "SZG_UR_x",
      "SZG_UR_y"
  };

  HashMap<String, BigDecimal> layout;

  @Before
  public void setupFirstLayout() {
    layout = FieldLayouts
        .getPlayingFieldLayout("ca.mcgill.ecse211.project.fieldlayout1");

  }

  @Test
  public void testStoredLayoutLengths() {
    assertEquals(32, layout.size());
  }

  @Test
  public void testStoredLayoutKeys() {
    for (String key : expectedLayoutKeys) {
      assertTrue(layout.containsKey(key));
    }
  }
  
  @After
  public void destroy() {
    layout = null;
  }
}
