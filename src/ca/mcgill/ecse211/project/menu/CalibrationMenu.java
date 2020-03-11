package ca.mcgill.ecse211.project.menu;

import static ca.mcgill.ecse211.project.LocalResources.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ca.mcgill.ecse211.project.Main;
import ca.mcgill.ecse211.tools.MenuCommand;
import ca.mcgill.ecse211.tools.SubMenu;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class CalibrationMenu extends SubMenu {

  public CalibrationMenu() {
    super("Calibration Tools");
  }

  public static SubMenu createCalibrations() {
    CalibrationMenu menu = new CalibrationMenu();
    // Tests Wheel Radius, it should move backwards one tile.
    menu.addItem("Wheel Radius", new MenuCommand() {
      public void setStatus(int changeFactor) {
        Main.WHEEL_RADIUS += changeFactor * .01;
      }

      public String getStatus() {
        return "" + Main.WHEEL_RADIUS;
      }

      public void action() {
        Wheel wheelLeft = WheeledChassis.modelWheel(motorLeft, Main.WHEEL_RADIUS)
            .offset(-BASE_WIDTH / 2);
        Wheel wheelRight = WheeledChassis.modelWheel(motorRight, Main.WHEEL_RADIUS)
            .offset(BASE_WIDTH / 2);
        Chassis chassis = new WheeledChassis(new Wheel[] { wheelLeft, wheelRight },
            WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot mover = new MovePilot(chassis);
        mover.setLinearAcceleration(100);
        mover.setLinearSpeed(100);
        mover.travel(-TILE_WIDTH);
      }
    });

    // Tests Ultrasonic Distance. Test on known distance, (Tile?)
    menu.addItem("U.S Dist Offset", new MenuCommand() {
      private SampleProvider sampler = ultrasonicSensorDevice.getDistanceMode();
      private float[] buffer = { 0 };
      private float offset = US_OFFSET;

      public void setStatus(int changeFactor) {
        offset += 0.001 * changeFactor; // in centimeter
        US_OFFSET = offset;
      }

      public String getStatus() {
        sampler.fetchSample(buffer, 0);
        return "" + (offset * 100) + "|" + (buffer[0] * 100 + offset * 100);
      }

      public void action() {
        Wheel wheelLeft = WheeledChassis.modelWheel(motorLeft, Main.WHEEL_RADIUS)
            .offset(-BASE_WIDTH / 2);
        Wheel wheelRight = WheeledChassis.modelWheel(motorRight, Main.WHEEL_RADIUS)
            .offset(BASE_WIDTH / 2);
        Chassis chassis = new WheeledChassis(new Wheel[] { wheelLeft, wheelRight },
            WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot mover = new MovePilot(chassis);

        mover.setLinearAcceleration(70);
        mover.setLinearSpeed(100);
        mover.travel(-TILE_WIDTH + (buffer[0] * 100 + offset * 100));
      }
    });

    // Tests Base Width. Should rotate 360 deg.
    menu.addItem("Base Width", new MenuCommand() {

      public String getStatus() {
        return "" + BASE_WIDTH;
      }

      public void setStatus(int changeFactor) {
        BASE_WIDTH += changeFactor * 0.01;

      }

      public void action() {
        Wheel wheelLeft = WheeledChassis.modelWheel(motorLeft, Main.WHEEL_RADIUS)
            .offset(-BASE_WIDTH / 2);
        Wheel wheelRight = WheeledChassis.modelWheel(motorRight, Main.WHEEL_RADIUS)
            .offset(BASE_WIDTH / 2);
        Chassis chassis = new WheeledChassis(new Wheel[] { wheelLeft, wheelRight },
            WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot mover = new MovePilot(chassis);
        mover.setAngularSpeed(100);
        mover.setAngularAcceleration(50);
        mover.rotate(360);
      }
    });

    menu.addItem("Ultrasonic Comparer", new MenuCommand() {
      private SampleProvider sampler = ultrasonicSensorDevice.getDistanceMode();
      private float[] buffer = { 0 };
      float storedDist = 0;
      float storedPoint = 0;

      public void setStatus(int changeFactor) {
        switch (changeFactor) {
          case -1:
            storedDist = buffer[0] * 100 + US_OFFSET * 100 - storedPoint;
            break;
          case +1:
            storedPoint = buffer[0] * 100 + US_OFFSET * 100;
            break;
          default:
        }
      }

      public String getStatus() {
        sampler.fetchSample(buffer, 0);
        return "" + (buffer[0] * 100 + US_OFFSET * 100 - storedPoint);
      }

      public void action() {
        Wheel wheelLeft = WheeledChassis.modelWheel(motorLeft, Main.WHEEL_RADIUS)
            .offset(-BASE_WIDTH / 2);
        Wheel wheelRight = WheeledChassis.modelWheel(motorRight, Main.WHEEL_RADIUS)
            .offset(BASE_WIDTH / 2);
        Chassis chassis = new WheeledChassis(new Wheel[] { wheelLeft, wheelRight },
            WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot mover = new MovePilot(chassis);

        mover.setLinearAcceleration(70);
        mover.setLinearSpeed(100);
        mover.travel(-storedDist + (buffer[0] * 100 + US_OFFSET * 100 - storedPoint));
      }
    });

    menu.addItem("Color Gaussian", new MenuCommand() {
      ArrayList<Float[]> data = new ArrayList<>();

      SampleProvider rgbColor = colorSensorDevice.getRGBMode();
      float[] buffer = { 0, 0, 0 };
      float[] mean = { 0, 0, 0 };

      public void setStatus(int changeFactor) {
        display.writeNext("Wait...", 2);
        try {
          Thread.sleep(1000); // A pause for stability
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        switch (changeFactor) {
          case -1:
            if (data.size() > 0) {
              data.remove(data.size() - 1);
            }
            break;
          case +1:
            rgbColor.fetchSample(buffer, 0);
            for (int i = 0; i < buffer.length; i++) {
              buffer[i] = (buffer[i] < 1) ? buffer[i] : Float.NaN;
            }
            data.add(new Float[] { buffer[0], buffer[1], buffer[2] });
            break;
          default:
        }
        calculateMean();
      }

      public String getStatus() {
        return "" + (int) (mean[0] * 1000) + "|\n" + "." + (int) (mean[1] * 1000) + "|\n" + "."
            + (int) (mean[2] * 1000) + "|";
      }

      public void action() {
        display.writeNext("Writing File...", 2);
        makeFile();
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
      }

      public void calculateMean() {
        float r = 0;
        float g = 0;
        float b = 0;
        for (int i = 0; i < data.size(); i++) {
          Float[] dat = data.get(i);
          r += dat[0];
          g += dat[1];
          b += dat[2];
        }
        mean[0] = r / (data.size());
        mean[1] = g / (data.size());
        mean[2] = b / (data.size());
      }

      public void makeFile() {
        FileWriter fr = null;
        try {
          fr = new FileWriter("ColorReadings" + System.currentTimeMillis() + ".csv");
          fr.write("Red,Green,Blue\n");
          for (int i = 0; i < data.size(); i++) {
            Float[] dat = data.get(i);
            fr.write(dat[0] + "," + dat[1] + "," + dat[2] + "\n");
          }
          fr.write("RedMean,GreenMean,BlueMean\n");
          fr.write(mean[0] + "," + mean[1] + "," + mean[2] + "\n");
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (fr != null) {
            try {
              fr.close();
            } catch (IOException e) {
            }
          }
        }
      }
    });
    return menu;
  }

}
