/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbcam;

import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;


/**
 *
 * @author Michael Shimniok
 */
public class ImagePanel extends JPanel {

    int xmax = 128;
    int ymax = 123;
    int[][] pixels = new int[xmax][ymax];    // used to paint the picture
    int[][] highlight= new int[xmax][ymax]; // used to paint colored boxes

    void setXY(int x, int y, int value) {
      if (0 <= y && y < ymax)
          if (0 <= x && x < xmax)
              pixels[x][y] = value;
    }

    int getXY(int x, int y) {
      int value = 0;
      if (0 <= y && y < ymax)
          if (0 <= x && x < xmax)
              value = pixels[x][y];
      return value;
    }

    void setHighlightXY(int x, int y) {
      if (0 <= y && y < ymax)
          if (0 <= x && x < xmax)
              highlight[x][y] = 240;
    }

    int getHighlightXY(int x, int y) {
      int value = 0;
      if (0 <= y && y < ymax)
          if (0 <= x && x < xmax)
              value = highlight[x][y];
      return value;
    }

    void clearHighlight() {
      for (int y = 0; y < ymax; y++)
        for (int x = 0; x < xmax; x++)
            highlight[x][y] = 0;
    }

    protected void paintComponent(Graphics g) {
        int red=0;
        int green=0;
        int blue=0;

        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        int ox = (getWidth() - 256) / 2;
        int oy = (getHeight() - 246) / 2;
        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                // if boxpixels set non-zero we need to draw
                //
                if (highlight[x][y] > 0) {
                    red = 0;
                    green = getHighlightXY(x,y);
                    blue = 0;
                }
                else {
                    red = getXY(x,y);
                    green = red;
                    blue = red;
                }
                try {
                    g.setColor(new Color(red, green, blue));
                    g.fillRect(x*2 + ox, y*2 + oy, 2, 2);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            } //for x
        } //for y
    } //paintComponent

}// ImagePanel
