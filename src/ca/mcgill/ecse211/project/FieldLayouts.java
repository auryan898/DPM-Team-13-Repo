package ca.mcgill.ecse211.project;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A utility class to load sample layouts stored as .properties files locally on
 * the robot. Uses ResourceBundle to access the key/value pairs from the stored
 * file. Edit the fieldlayout1.properties file, provided as an example, to setup
 * your own offline playing field parameters.
 * 
 * <p>
 * Use FieldLayouts to load offline versions of the wifiParameters, instead of
 * the usual call to receiveWifiParameters(). This is useful if the user cannot
 * run the laptop/pc server for some reason, but still wishes to test features
 * that rely on playing field parameters in the code.
 * 
 * <p>
 * Example:
 * { @code
 * wifiParameters = FieldLayouts.getPlayingFieldLayout(resource_path);
 * receiveWifiParameters(); // This is now skipped because of the previous line
 * }
 * 
 * @author Ryan Au
 *
 */
public class FieldLayouts {

  private FieldLayouts() {
  }

  /**
   * getString
   * Retrieves a string value from a given resource bundle, based on the key
   * string. return the key name padded with ! when key is not available.
   * 
   * @param  key    name of value to retrieve from resource bundle
   * @param  bundle resource bundle that contains key/value pairs
   * @return        the value stored in the bundle
   */
  public static String getString(String key, ResourceBundle bundle) {
    try {
      return bundle.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  /**
   * Retrieves all of the values of a bundle as a HashMap of String/BigDecimal
   * pairs. Throws exception when a value in the bundle cannot be parsed as a
   * BigDecimal format.
   * 
   * <p>
   * Accepts filename of format 'ca.mcgill.ecse211.project.fieldlayout1' when the
   * containing package is 'ca.mcgill.ecse211.project' and the filename is
   * actually 'fieldlayout1.properties'.
   * 
   * @param  filename              follows format as defined above
   * @return                       HashMap of String/BigDecimal pairs
   * @throws NumberFormatException when value in properties file cannot be parsed
   *                               as number
   */
  public static HashMap<String, BigDecimal> getPlayingFieldLayout(String filename)
      throws NumberFormatException {
    ResourceBundle bundle = ResourceBundle.getBundle(filename);
    HashMap<String, BigDecimal> map = new HashMap<>();

    for (String key : Collections.list(bundle.getKeys())) {
      map.put(key, new BigDecimal(getString(key, bundle)));
    }
    return map;
  }
}
