/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbcam;
import com.jdotsoft.jarloader.JarClassLoader;

/**
 *
 * @author Michael Shimniok
 */
public class Launcher {

    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        System.out.println("Launching GBCamApp...");
        try {
            jcl.invokeMain("gbcam.GBCamApp", args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    } // main()

} // class Launcher
