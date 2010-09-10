/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbcam;

import java.util.*;
import java.io.*;

/**
 *
 * @author Michael Shimniok
 */
public class Preferences {

    static public Properties p = new Properties();

    static public void load() throws IOException {
        try {
            p.load(new FileInputStream(System.getProperty("user.home") +
                   System.getProperty("file.separator") + ".gbcamview.properties"));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    static public void save() throws IOException {
        try {
            p.store(new FileOutputStream(System.getProperty("user.home") +
                    System.getProperty("file.separator") +
                    ".gbcamview.properties"), "Camera Viewer Settings");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    static public int[] loadRegisters() {
        int [] r = new int[8];
        int regcount;
        String c = p.getProperty("gbcamview.regcount");

        if (c == null) {
            return null;
        }
        else {
            regcount = Integer.parseInt(c);

            for (int i=0; i < regcount; i++) {
                c = p.getProperty("gbcamview.register"+Integer.toString(i));
                if (c == null)
                    return null;
                else
                    r[i] = Integer.parseInt(c);
            }
        }

        return r;
    }

    // TODO: load/save prefs for getWhatComboBox

    static public void saveRegisters(int[] r) {
        p.setProperty("gbcamview.regcount", Integer.toString(r.length));

        for (int i=0; i < r.length; i++) {
            p.setProperty("gbcamview.register"+Integer.toString(i), Integer.toString(r[i]));
        }
    }

    static public String loadSerialPortName() {
        return p.getProperty("gbcamview.port");
    }

    static public void saveSerialPortName(String serialPortName) {
        p.setProperty("gbcamview.port", serialPortName);
    }

}
