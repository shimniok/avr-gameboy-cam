/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbcam;

import gnu.io.*;
import java.io.*;

/**
 *
 * @author Michael Shimniok
 */
public class Communication {

    public static int getByte(InputStream in, int timeout)
        throws IOException {
        int c = -1;
        int times;
        // If nothing available delay until timeout and...
        try {
            for (times = 0; times < 100; times++) {
            if (in.available() > 0) break;
                Thread.sleep(50);
            }
        }
        catch (Exception e) {
            throw new IOException("Internal error with thread sleep() delay");
        }

        // ...try again (or, if we had data already)
        if (in.available() > 0) {
            c = in.read();
        }
        else {
            throw new IOException("Communication timeout");
        }
        return c;
    }

}
