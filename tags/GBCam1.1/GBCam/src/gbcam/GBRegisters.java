/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbcam;

/**
 *
 * @author Michael Shimniok
 */
public class GBRegisters {
    private int[] register = new int[8];

    // Set default values
    public GBRegisters() {
        register[0] = 0x9B;    // Z O
        register[1] = 0x00;    // N VH G
        register[2] = 0x00;    // C1
        register[3] = 0x3C;    // C0
        register[4] = 0x01;    // P
        register[5] = 0x00;    // M
        register[6] = 0x01;    // X
        register[7] = 0x07;    // E I V

        /*
        register[0] = 0x80;    // Z=10 O=0
        register[1] = 0x03;    // N=0 VH=0 G=0x03
        register[2] = 0x00;    // C1
        register[3] = 0x30;    // C0
        register[4] = 0x01;    // P
        register[5] = 0x00;    // M
        register[6] = 0x01;    // X
        register[7] = 0x21;    // E=0010 I=0 V=001

         */
    }

    public void setRegisters(int[] r) {
        if (r != null)
            for (int i=0; i < r.length; i++) {
                register[i] = r[i];
                System.out.println("reg"+Integer.toString(i)+": "+Integer.toHexString(register[i]));
            }
    }

    public int[] getRegisters() {
        return register;
    }

    //////////////////////////////////////////////////////////////////////////
    // C0
    //////////////////////////////////////////////////////////////////////////
    public void setC0(int c0) {
        System.out.println("reg3: "+Integer.toHexString(register[3]));
        register[3] = c0;
        System.out.println("reg3: "+Integer.toHexString(register[3]));
    }

    public int getC0() {
        return register[3];
    }

    //////////////////////////////////////////////////////////////////////////
    // C1
    //////////////////////////////////////////////////////////////////////////
    public void setC1(int c1) {
        System.out.println("reg2: "+Integer.toHexString(register[2]));
        register[2] = c1;
        System.out.println("reg2: "+Integer.toHexString(register[2]));
    }

    public int getC1() {
        return register[2];
    }

    //////////////////////////////////////////////////////////////////////////
    // E
    //////////////////////////////////////////////////////////////////////////
    public void setE(int e) {
        System.out.println("reg7: "+Integer.toHexString(register[7]));
        // clear
        register[7] &= 0x8F;          // BX000XXXX
        // set
        register[7] |= ((e & 0x07) << 4); // BX...XXXX
        System.out.println("reg7: "+Integer.toHexString(register[7]));
    }

    public int getE() {
        return (register[7] & 0x07)>>4;
    }

    //////////////////////////////////////////////////////////////////////////
    // G
    //////////////////////////////////////////////////////////////////////////
    public void setG(int g) {
        System.out.println("reg1: "+Integer.toHexString(register[1]));
        // clear
        register[1] &= ~0x1F; // reset the last 5 bits b11100000
        // set
        register[1] |= g & 0x1F;
        System.out.println("reg1: "+Integer.toHexString(register[1]));
    }

    public int getG() {
        return register[1] & 0x1F;
    }

    // TODO: Set E mode

    //////////////////////////////////////////////////////////////////////////
    // I
    //////////////////////////////////////////////////////////////////////////
    public void toggleI() {
        System.out.println("reg7: "+Integer.toHexString(register[7]));
        register[7] ^= 0x08;
        System.out.println("reg7: "+Integer.toHexString(register[7]));
    }

    public void setI(boolean i) {
        System.out.println("reg7: "+Integer.toHexString(register[7]));
        if (i)
            register[7] |= 0x08;
        else
            register[7] &= ~0x08;
        System.out.println("reg7: "+Integer.toHexString(register[7]));
    }

    public boolean getI() {
        return (register[7] & 0x08) == 0x08;
    }

    //////////////////////////////////////////////////////////////////////////
    // M
    //////////////////////////////////////////////////////////////////////////
    public int getM() {
        return register[5];
    }

    public void setM(int m) {
       System.out.println("reg5: "+Integer.toHexString(register[5]));
       register[5] = m;
       System.out.println("reg5: "+Integer.toHexString(register[5]));
    }

    //////////////////////////////////////////////////////////////////////////
    // N
    //////////////////////////////////////////////////////////////////////////
    public void toggleN() {
        System.out.println("reg1: "+Integer.toHexString(register[1]));
        register[1] ^= 0x80;
        System.out.println("reg1: "+Integer.toHexString(register[1]));
    }

    public void setN(boolean n) {
        System.out.println("reg1: "+Integer.toHexString(register[1]));
        if (n)
            register[1] |= 0x80;
        else
            register[1] &= ~0x80;
        System.out.println("reg1: "+Integer.toHexString(register[1]));
    }

    public boolean getN() {
        return (register[1] & 0x80) == 0x80;
    }

    //////////////////////////////////////////////////////////////////////////
    // O
    //////////////////////////////////////////////////////////////////////////
    public void setO(int o) {
        System.out.println("reg0: "+Integer.toHexString(register[0]));
        if (o < 0)
            register[0] &= 0xDF; // unset positive bit (bXX0XXXXX)
        else
            register[0] |= 0x20; // set positive bit (bXX1XXXXX)

        // reset the last 5 bits
        register[0] &= 0xE0;
        // set the last 5 bits
        register[0] |= Math.abs(o) & 0x1F;
        System.out.println("reg0: "+Integer.toHexString(register[0]));
    }

    public int getO() {
        int o = register[0] & 0x1F; // get all but positive bit
        // check positive bit, if not set, make number negative
        if ( o != 0 && (register[0] & 0x20) == 0x00 )
            o *= -1;
        return o;
    }

    //////////////////////////////////////////////////////////////////////////
    // P
    //////////////////////////////////////////////////////////////////////////
    public int getP() {
        return register[4];
    }

    public void setP(int p) {
        System.out.println("reg4: "+Integer.toHexString(register[4]));
        register[4] = p;
        System.out.println("reg4: "+Integer.toHexString(register[4]));
    }

    //////////////////////////////////////////////////////////////////////////
    // V
    //////////////////////////////////////////////////////////////////////////
    public void setV(int v) {
        System.out.println("reg7: "+Integer.toHexString(register[7]));
        register[7] &= 0xF8; // reset the last 3 bits bXXXXX000
        register[7] |= (v & 0x07);
        System.out.println("reg7: "+Integer.toHexString(register[7]));
    }

    public int getV() {
        return register[7] & 0x07; // last 3 bits bXXXXX000
    }

    //////////////////////////////////////////////////////////////////////////
    // X
    //////////////////////////////////////////////////////////////////////////
    public int getX() {
        return register[6];
    }

    public void setX(int x) {
        System.out.println("reg6: "+Integer.toHexString(register[6]));
        register[6] = x;
        System.out.println("reg6: "+Integer.toHexString(register[6]));
    }

    //////////////////////////////////////////////////////////////////////////
    // Z
    //////////////////////////////////////////////////////////////////////////
    public int getZ() {
        return  (register[0] & 0xC0) >> 6;
    }

    public void setZ(int z) {
        System.out.println("reg0: "+Integer.toHexString(register[0]));
        register[0] &= 0x3F; // B00XXXXXX
        register[0] |= ((z & 0x03) << 6);
        System.out.println("reg0: "+Integer.toHexString(register[0]));
    }

}
