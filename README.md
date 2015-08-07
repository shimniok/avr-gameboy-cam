# avr-gameboy-cam
Code for an AVR microcontroller to interface with a Game Boy Camera, pull images, and send over serial to a Java applet on a PC for display. Applet also controls camera settings.

Tested on Ardweeny (ATmega328P) and PC running XP

Note: this file was last updated Sep 15, 2010 :(

# Arduino Code
Use Arduino IDE to create a new project called gbcam_arduino and close Arduino IDE.

Use git to checkout the Arduino code into your gbcam_arduino directory

Reopen Arduino IDE and you should be able to edit the gbcam_arduino sketchbook now.

# PC/Mac Java Client
Checking Out Source
You can download the source, or rather check it out from the repository. To check out the source,
look for instructions on the Source tab. To browse the source, click Browse on the Source tab.

GBCam is checked in as a NetBeans IDE project. You can use NetBeans? to check out the files then open
the project.

# Mac OS X
With a little extra work, it's possible to get GBCam to run on Snow Leopard (Mac OS X 10.6). Here's how.

GBCam relies on the RXTX serial communications software library. It's that piece that we need to get
working on Mac OS X as follows.

RXTX-2.1-7R2, and more specifically the native library supplied (librxtxSerial.jnilib) doesn't properly 
support the Snow Leopard / Java 1.6 environment. Robert Harder was kind enough to go through the
difficult work of recompiling the jnilib under Snow Leopard and makes it available on his blog posting here.

Place the jnilib in your /Library/Java/Extensions folder and, just to be safe, place RXTXcomm.jar in there
as well. A later version of GBCam will run on Mac OS X in a self contained jar.

Serial Setup
Additionally, you'll need to set up your OS to support serial communications. That involves creation of
a /var/lock and /var/uucp directory and giving yourself permissions to access those areas. You can either 
download the init-setup.zip file or read the instructions on this helpful site.
