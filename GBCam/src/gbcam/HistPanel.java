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
public class HistPanel extends JPanel {

    int xmax = 256;
    int ymax = 50;
    int[][] pixels = new int[xmax][ymax];

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

    @Override protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                int pixel = getXY(x,y);
                g.setColor(new Color(pixel,pixel,pixel));
                g.fillRect(x, y, 1, 1);
            }
        }
    }
}