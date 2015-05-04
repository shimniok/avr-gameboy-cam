# Introduction #

With a little extra work, it's possible to get GBCam to run on Snow Leopard (Mac OS X 10.6).  Here's how.

# Details #

GBCam relies on the RXTX serial communications software library.  It's that piece that we need to get working on Mac OS X as follows.

## RXTX jnilib ##
RXTX-2.1-7R2, and more specifically the native library supplied (librxtxSerial.jnilib) doesn't properly support the Snow Leopard / Java 1.6 environment.  [Robert Harder](http://blog.iharder.net) was kind enough to go through the difficult work of recompiling the jnilib under Snow Leopard and makes it available on his blog posting [here](http://blog.iharder.net/2009/08/18/rxtx-java-6-and-librxtxserial-jnilib-on-intel-mac-os-x/).

Place the jnilib in your /Library/Java/Extensions folder and, just to be safe, place RXTXcomm.jar in there as well.  A later version of GBCam will run on Mac OS X in a self contained jar.

## Serial Setup ##
Additionally, you'll need to set up your OS to support serial communications. That involves creation of a /var/lock and /var/uucp directory and giving yourself permissions to access those areas.  You can either download the init-setup.zip file or read the instructions on
[this helpful site](http://atelier.tkrworks.net/doc/about-picnomeserial?lang=en).