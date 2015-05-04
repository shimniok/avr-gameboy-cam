<wiki:gadget up\_ad\_client="ca-pub-2823773255215457" up\_ad\_slot="0876451708" width="728" height="90" border="0" up\_keywords="arduino hobby robotics camera sensor" url="http://goo.gl/R5bvK" />

# Summary #
Code for an AVR microcontroller to interface with a Game Boy Camera, pull images, and send over serial to a Java applet on a PC for display.  Applet also controls camera settings.

Tested on Ardweeny (ATmega328P) and PC running XP

# Getting Started #
## Arduino Code ##
Use [Arduino IDE](http://www.arduino.cc/) to create a new project called `gbcam_arduino` and close Arduino IDE.

Use a standalone Subversion client to checkout the Arduino code into your `gbcam_arduino` directory

Reopen Arduino IDE and you should be able to edit the gbcam\_arduino sketchbook now.

## PC/Mac Java Client ##
### Checking Out Source ###
You can download the source, or rather check it out from the repository.  To check out the source, look for instructions on the Source tab.  To browse the source, click Browse on the Source tab.

GBCam is checked in as a [NetBeans IDE](http://netbeans.org/) project.  You can use NetBeans to check out the files then open the project.
### Windows XP ###
A Windows XP client is available as a JAR on the Downloads tab.  The jar is self-contained so it should launch and work a-ok.  I've not tested it on any OS beyond XP.

### Mac OS X ###
A little extra work is required to get GBCam running on Snow Leopard.  Read wiki page MacDeploy to see how I did it.