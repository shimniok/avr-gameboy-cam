/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbcam;

/**
 *
 * @author Michael Shimniok
 */
public class Utility {
    static public boolean isDigit(String str) {
        // first char is either digit or minus sign
        if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != '-')
            return false;
        // check the rest of the characters
        for (int i=1; i < str.length(); i++) {
          if (!Character.isDigit(str.charAt(i)))
              return false;
        }
        return true;
    }
}
