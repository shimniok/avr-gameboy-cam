# avr-gameboy-cam
Code for an AVR microcontroller to interface with a Game Boy Camera, pull images, and send over serial to a Java applet on a PC for display. Applet also controls camera settings.

Tested on Ardweeny (ATmega328P) and PC running XP

**Note**: No major work done on this project since Sep 15, 2010 :(

# Arduino Code
After you clone the repository, create a link from your Arduino sketchbook directory to the gbcam_arduino
directory. It should appear in the sketchbook when you open Arduino IDE.

## Checking Out Source
You can download the source, or rather check it out from the repository. To check out the source,
look for instructions on the Source tab. To browse the source, click Browse on the Source tab.

GBCam is checked in as a NetBeans IDE project. You can use NetBeans? to check out the files then open
the project.

GBCam relies on the RXTX serial communications software library. You'll want RXTX-2.1-7R2 (maybe later
versions will work, don't know). Place the appropriate jar and native library in the appropriate location
for your operating system.
