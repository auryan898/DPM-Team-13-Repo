package pctest;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.TextLCD;

public class FakeTextLCD implements TextLCD {

  private int width = 8;
  private int height = 18;
  private char[][] pixels;

  public FakeTextLCD() {
    pixels = new char[height][width];
  }

  @Override
  public void refresh() {
    for (int row = 0; row < pixels.length; row++) {
      System.out.println(String.valueOf(pixels[row]));
    }
  }

  @Override
  public void clear() {
    for (int row = 0; row < pixels.length; row++) {
      clear(row);
    }
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    // TODO Auto-generated method stub
    return height;
  }

  @Override
  public byte[] getDisplay() {
    byte[] res = new byte[height * width];
    for (int row = 0; row < pixels.length; row++) {
      for (int col = 0; col < pixels[row].length; col++) {
        res[(row * height) + col] = (byte) pixels[row][col];
      }
    }
    return res;
  }

  @Override
  public byte[] getHWDisplay() {
    return getDisplay();
  }

  @Override
  public void setContrast(int contrast) {

  }

  @Override
  public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx, int dy, int w, int h,
      int rop) {

  }

  @Override
  public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte[] dst, int dw, int dh, int dx,
      int dy, int w, int h, int rop) {

  }

  @Override
  public void setAutoRefresh(boolean on) {

  }

  @Override
  public int setAutoRefreshPeriod(int period) {
    return 0;
  }

  @Override
  public void drawChar(char c, int x, int y) {
    if (x < width && y < height)
      pixels[y][x] = c;
    refresh();
  }

  @Override
  public void drawString(String str, int x, int y, boolean inverted) {
    if (y < height) {
      for (int i = 0; i < str.length() && i + x < width; i++) {
        pixels[y][i + x] = str.charAt(i);
      }
    }
    refresh();
  }

  @Override
  public void drawString(String str, int x, int y) {
    drawString(str, x, y, false);
  }

  @Override
  public void drawInt(int i, int x, int y) {
    drawString(i+"",x,y);
  }

  @Override
  public void drawInt(int i, int places, int x, int y) {
    drawString(i+"",x,y);
  }

  @Override
  public void clear(int x, int y, int n) {
    // TODO Auto-generated method stub

  }

  @Override
  public void clear(int y) {
    for (int col = 0; y < height && col < pixels[y].length; col++) {
      pixels[y][col] = ' ';
    }
  }

  @Override
  public void scroll() {

  }

  @Override
  public Font getFont() {
    return null;
  }

  @Override
  public int getTextWidth() {
    return 8;
  }

  @Override
  public int getTextHeight() {
    return 18;
  }

}
