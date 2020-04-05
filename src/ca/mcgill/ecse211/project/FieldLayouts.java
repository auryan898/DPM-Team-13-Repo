package ca.mcgill.ecse211.project;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FieldLayouts {
  private static final String BUNDLE_NAME = "ca.mcgill.ecse211.project.layout_settings"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private FieldLayouts() {
  }

  public static String getString(String key) {
    return getString(key, RESOURCE_BUNDLE);
  }

  public static String getString(String key, ResourceBundle bundle) {
    try {
      return bundle.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  public static HashMap<String, BigDecimal> getLayout(String fileName)
      throws NumberFormatException {
    ResourceBundle bundle = ResourceBundle.getBundle(fileName);
    HashMap<String, BigDecimal> map = new HashMap<>();

    for (String key : Collections.list(bundle.getKeys())) {
      map.put(key, new BigDecimal(getString(key, bundle)));
    }
    return map;
  }
}
