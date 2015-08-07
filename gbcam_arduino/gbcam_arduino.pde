/**
 * @file       main.c
 * @brief      MAIN for the gameboy camera interface for AT90S4433
 * 
 * @author     Laurent Saint-Marcel (lstmarcel@yahoo.fr)
 * @date       2005/07/05
 */

/* ------------------------------------------------------------------------ */
/* HOW TO CONNECT THE GAME BOY CAMERA                                       */
/* ------------------------------------------------------------------------ */
/*

  READ  -- D8,  PB0
  XCK   -- D9,  PB1
  XRST  -- D10, PB2
  LOAD  -- D11, PB3
  SIN   -- D12, PB4
  START -- D13, PB5
  VOUT  -- A3,  PC3
  
  */

/* ------------------------------------------------------------------------ */
/* INCLUDES                                                                 */
/* ------------------------------------------------------------------------ */
 
#include <Wire.h>

#include <avr/interrupt.h>
#include <avr/io.h>
#include <compat/deprecated.h>

#include "cam.h"
#include "camCom.h"

/* ------------------------------------------------------------------------ */
/* DEFINES                                                                  */
/* ------------------------------------------------------------------------ */

#define CAM_DATA_PORT     PORTB
#define CAM_DATA_DDR      DDRB
#define CAM_READ_PIN      8       // Arduino D8

#define CAM_LED_DDR       DDRB
#define CAM_LED_PORT      PORTB

// CAM_DATA_PORT
#define CAM_START_BIT 5
#define CAM_SIN_BIT   4
#define CAM_LOAD_BIT  3
#define CAM_RESET_BIT 2
#define CAM_CLOCK_BIT 1
#define CAM_READ_BIT  0
// CAM_LED_PORT
#define CAM_LED_BIT   4
// PORT C: Analogic/digital converter
#define CAM_ADC_PIN   3

// Pixel value threshold for bright pixel
#define THRESH 210

// Define end of row of obj/img buffers for pointer math
#define THEEND (2*128-1)

// Object maximum
#define MAXOBJS 128

#define FALSE 0
#define TRUE 1

/* ------------------------------------------------------------------------ */
/* GLOBALS                                                                  */
/* ------------------------------------------------------------------------ */

// default value for registers
// 155 0 0 30 1 0 1 7 
// no edge detection, exposure=0,30, offset=-27, vref=+1.0
unsigned char camReg[8]={ 155, 0, 0, 30, 1, 0, 1, 7 };

unsigned char camMode       = CAM_MODE_STANDARD;
unsigned char camClockSpeed = 0x07; // was 0x0A
unsigned char camPixMin =0;
unsigned char camPixMax =0xFF;
unsigned char camCompBuf=0;
unsigned char camCompI  =0;
unsigned int threshold = THRESH;
int ledPin = 13;

object obj[MAXOBJS];  // index of object structures to keep track of bounding boxes

/* ------------------------------------------------------------------------ */
/* MACROS                                                                   */
/* ------------------------------------------------------------------------ */

#define Serialwait()   while (Serial.available() == 0) ;

int dataIn;
int dataOut;
boolean dataReady;
unsigned char reg;


/* ------------------------------------------------------------------------ */
/* Initialize all components                                                */
/* ------------------------------------------------------------------------ */
void setup()
{
        dataReady = false;
  	Serial.begin(38400);
        //Wire.begin(TWI_CAMERA);
        //Wire.onReceive(recvByte);
        //Wire.onRequest(sendByte);
	camInit();
	/* enable interrupts */
	sei();
}

/* ------------------------------------------------------------------------ */
/* Program entry point                                                      */
/* ------------------------------------------------------------------------ */
void loop()
{
  while (Serial.available() == 0)
    delay(50);
  dataIn = Serial.read();
  switch (dataIn) {
    case CAM_COM_PING:
      dataOut = CAM_COM_PONG;
      Serial.write(CAM_COM_PONG);
      break;
    case CAM_COM_GET_OBJECTS:
      // Send 8 camera registers
      camReset();
      camSetRegisters();
      camReadPicture(false, true); // don't get pixels, do get objects
      camReset();
      break;
    case CAM_COM_TAKE_PICTURE:
      // Send 8 camera registers
      camReset();
      camSetRegisters();
      camReadPicture(true, true); // get both pixels and objects
      camReset();
      break;
    case CAM_COM_SET_REGISTERS:
      Serialwait();
      camStoreReg(0, Serial.read());
      Serialwait();
      camStoreReg(1, Serial.read());
      Serialwait();
      camStoreReg(2, Serial.read());
      Serialwait();
      camStoreReg(3, Serial.read());
      Serialwait();
      camStoreReg(4, Serial.read());
      Serialwait();
      camStoreReg(5, Serial.read());
      Serialwait();
      camStoreReg(6, Serial.read());
      Serialwait();
      camStoreReg(7, Serial.read());
      Serialwait();
      threshold = Serial.read();
      Serial.write(CAM_COM_GOT_REGISTERS);
      break;
    default:
      Serial.write(CAM_COM_ERROR);
      break;
  } // switch-case
  
} // loop

///////////////////////////////////////////////////////////////////////////
// PRIVATE FUNCTIONS
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// TWI HANDLERS
void recvByte(int howMany) {
  dataIn = Wire.read();
  dataReady = true;
}

void sendByte(void) {
  Wire.write(dataOut);
}

///////////////////////////////////////////////////////////////////////////
// CAM TIMING AND CONTROL

// cbi(port, bitId) = clear bit(port, bitId) = Set the signal Low
// sbi(port, bitId) = set bit(port, bitId) = Set the signal High

// Delay used between each signal sent to the AR (four per xck cycle).
void camStepDelay() {
  	unsigned char u=camClockSpeed;
	while(u--) {__asm__ __volatile__ ("nop");}
}
// Set the clock signal Low
inline void camClockL()
{
	cbi(CAM_DATA_PORT, CAM_CLOCK_BIT);
}
// Set the clock signal High
inline void camClockH()
{
	sbi(CAM_DATA_PORT, CAM_CLOCK_BIT);
}


// Initialise the IO ports for the camera
void camInit()
{
  pinMode(8, INPUT);   // READ
  pinMode(9, OUTPUT);  // XCK
  pinMode(10, OUTPUT);  // XRST
  pinMode(11, OUTPUT); // LOAD
  pinMode(12, OUTPUT); // SIN
  pinMode(13, OUTPUT); // START
  
  cbi(CAM_DATA_PORT, CAM_CLOCK_BIT);
  sbi(CAM_DATA_PORT, CAM_RESET_BIT);  // Reset is active low
  cbi(CAM_DATA_PORT, CAM_LOAD_BIT);
  cbi(CAM_DATA_PORT, CAM_START_BIT);
  cbi(CAM_DATA_PORT, CAM_SIN_BIT);
}


// Sends a 'reset' pulse to the AR chip.
// START:  XCK Rising Edge 
// FINISH: XCK Just before Falling Edge
void camReset()
{
  camClockH(); // clock high
  camStepDelay();
  camStepDelay();
 
  camClockL(); // clock low
  camStepDelay();
  cbi(CAM_DATA_PORT, CAM_RESET_BIT);
  camStepDelay();

  camClockH(); // clock high
  camStepDelay();
  sbi(CAM_DATA_PORT, CAM_RESET_BIT);
  camStepDelay();
}


// locally set the value of a register but do not set it in the AR chip. You 
// must run camSendRegisters1 to write the register value in the chip
void camStoreReg(unsigned char reg, unsigned char data) 
{
  camReg[reg] = data;
}

// Reset the camera and set the camera's 8 registers
// from the locally stored values (see camStoreReg)
void camSetRegisters(void)
{
  for(reg=0; reg<8; ++reg) {
    camSetReg(reg, camReg[reg]);
  }
}

// Sets one of the 8 8-bit registers in the AR chip.
// START:  XCK Falling Edge 
// FINISH: XCK Just before Falling Edge
void camSetReg(unsigned char regaddr, unsigned char regval)
{
  unsigned char bitmask;

  // Write 3-bit address.
  for(bitmask = 0x4; bitmask >= 0x1; bitmask >>= 1){
    camClockL();
    camStepDelay();
    // ensure load bit is cleared from previous call
    cbi(CAM_DATA_PORT, CAM_LOAD_BIT);
    // Set the SIN bit
    if(regaddr & bitmask)
      sbi(CAM_DATA_PORT, CAM_SIN_BIT);
    else
      cbi(CAM_DATA_PORT, CAM_SIN_BIT);
    camStepDelay();

    camClockH();
    camStepDelay();
    // set the SIN bit low
    cbi(CAM_DATA_PORT, CAM_SIN_BIT);
    camStepDelay();
  }

  // Write 7 most significant bits of 8-bit data.
  for(bitmask = 128; bitmask >= 1; bitmask>>=1){
    camClockL();
    camStepDelay();
    // set the SIN bit
    if(regval & bitmask)
      sbi(CAM_DATA_PORT, CAM_SIN_BIT);
    else
      cbi(CAM_DATA_PORT, CAM_SIN_BIT);
    camStepDelay();
    // Assert load at rising edge of xck
    if (bitmask == 1)
      sbi(CAM_DATA_PORT, CAM_LOAD_BIT);
    camClockH();
    camStepDelay();
    // reset the SIN bit
    cbi(CAM_DATA_PORT, CAM_SIN_BIT);
    camStepDelay();
  }
}

void delallobj(void)
{
  int i;
  for (i = 0; i < MAXOBJS; i++)
    delobj(i);
}

void delobj(objindex index)
{
  obj[index].exists = FALSE;
  obj[index].top = 0;
  obj[index].bottom = 0;
  obj[index].left = 0;
  obj[index].right = 0;
}

// Rather than having routines to generically expand the bounding
// box, use inlines specific to each task
#define addobjxmin(index, x) if (x < obj[index].left) obj[index].left = x
#define addobjxmax(index, x) if (x > obj[index].right) obj[index].right = x
#define addobjymin(index, x) if (y < obj[index].top) obj[index].top = y
#define addobjymax(index, x) if (y > obj[index].bottom) obj[index].bottom = y

//TODO:  Go through the object array and merge

void addobj(objindex index, coord x, coord y)
{
  if ( obj[index].exists == FALSE ) {
    obj[index].exists = TRUE;
    obj[index].top = y;
    obj[index].bottom = y;
    obj[index].left = x;
    obj[index].right = x;
  }
  else {
    //if (y < obj[index].top) // we may never need to do this, actually, since we're going from top to bottom
    //  obj[index].top = y;   // and never add an 'above' pixel to a new object, but add a current pixel to an above object

    if (x < obj[index].left)
      obj[index].left = x;

    if (y > obj[index].bottom)
      obj[index].bottom = y;

    if (x > obj[index].right)
      obj[index].right = x;
  }
  return;
}

objindex countobj(void) {
  objindex i;
  objindex count;

  count = 0;
  for (i = 0; i < MAXOBJS; i++)
    if (obj[i].exists == TRUE)
      count++;
      
  return count;
}

void filterobj(void)
{
  objindex i;
  coord left, right, top, bottom, x, y;
  unsigned int h, w, ratio;

  for (i = 0; i < MAXOBJS; i++) {
    if (obj[i].exists == TRUE) {
      top    = obj[i].top;
      left   = obj[i].left;
      bottom = obj[i].bottom;
      right  = obj[i].right;
      // Filter out "small" objects
      if ((bottom - top) < 3 || (right - left) < 3)
        obj[i].exists = FALSE;
    }
  }
}

void printobj(void)
{
  objindex i;

  Serial.write(CAM_COM_OBJECTS);
  Serial.write(countobj());

  for (i = 0; i < MAXOBJS; i++) {
    if (obj[i].exists == TRUE) {
      Serial.write(i);
      Serial.write(obj[i].left);
      Serial.write(obj[i].top);
      Serial.write(obj[i].right);
      Serial.write(obj[i].bottom);
    }
  }

  return;
}

// Take a picture, read it and send it trhough the serial port. 
//
// Use a simplified flood fill algorithm to identify bright objects
//
// getPicture -- send the pixel data to the requester
// getObjects -- send the object data to the requester
//
// START:  XCK Falling Edge 
// FINISH: XCK Just before Rising Edge
void camReadPicture(boolean getPixels, boolean getObjects)
{
  // We keep a 2-line buffer to represent which pixels are assigned to objects
  // we can read one pixel at a time, assign an object, then forget all about
  // that pixel, while checking for connectedness with objects in the previous row
  boolean searching=true;    // current state
  boolean bright=false;      // flag for pixel brightness
  unsigned int pixel;       // Buffer for pixel read in
  unsigned char curr;        // current read/flood-fill row
  unsigned char prev;        // previous flood-fill row
  objindex objref[2][128];   // Buffer to keep track of what object each img pixel belongs to
  objindex thisobject;       // the object to which we're adding new pixels
  objindex nextobject;       // the next available object to which pixels can be assigned
  objindex aboveobj;         // object assignment of pixel above
  objindex redoobj;           // indicate if we need to redo flood fill (obj assignment)
  coord x, y;                // the current x,y coordinate we're working on
  coord xstart;              // keep track of where the most recent object started
  coord x1;                  // temporary use for back-filling object
  short c;

  camCompBuf=0;
  camCompI=0;

  // make sure our previous objects are gone
  delallobj();

  // Seems to take a long time to send
  Serial.write(CAM_COM_SEND_START);
 
  // Camera START sequence
  camClockL();
  camStepDelay();
  // ensure load bit is cleared from previous call
  cbi(CAM_DATA_PORT, CAM_LOAD_BIT);
  // START rises before xck
  sbi(CAM_DATA_PORT, CAM_START_BIT);
  camStepDelay();

  camClockH();
  camStepDelay();
  // start valid on rising edge of xck, so can drop now
  cbi(CAM_DATA_PORT, CAM_START_BIT);
  camStepDelay();

  camClockL();
  camStepDelay();
  camStepDelay();
 
  // Wait for READ to go high
  while (1) {
    camClockH();
    camStepDelay();
    // READ goes high with rising XCK
    //    if ( inp(CAM_READ_PIN) & (1 << CAM_READ_BIT) )
    if (digitalRead(CAM_READ_PIN) == HIGH) // CAM pin on PB0/D8
      break;
    camStepDelay();

    camClockL();
    camStepDelay();
    camStepDelay();
  }
  
  if (getPixels)
    Serial.write(CAM_COM_START_READ);
  //sbi(CAM_LED_PORT, CAM_LED_BIT);

  camStepDelay();
   
  // Read pixels from cam until READ signal is low again

  // Set registers while reading the first 11-ish pixels

  // Initialize object detection stuff
  curr = 1;                                        // start the current row on the 2nd row (doesn't really matter
  prev = 0;                                        // start the previous row on the 1st row 
  thisobject = nextobject = 0;                     // first object is 1 because a 0 obj means no object
  redoobj = 0;                                     // make sure we don't try to flood-fill backwards unless we have to
  
  // Continue reading the rest of the pixels and flood fill to detect bright objects
  // The camera seems to be spitting out 128x128 even though the final 5 rows are junk

  for (y = 0; y < 123; y++) {

    searching = true;                                // start in 'searching' state
    for (x = 0; x < 128; x++) {

      camClockL();
      camStepDelay();
      // get the next pixel, buffer it, and send it out over serial
      pixel = analogRead(CAM_ADC_PIN) >> 2;
      bright = (pixel > threshold);   // if pixel value > threshold it is bright
      if (getPixels) {
        if (pixel > 200)
          Serial.write('#');
        else if (pixel > 100)
          Serial.write('+');
        else
          Serial.write(' ');
//        Serial.write(pixel);
      }
      aboveobj = objref[prev][x];     // pixel above's object reference (0 if no object)
      redoobj = 0;                    // reset redoobj
      camStepDelay(); // nix this and run the obj detection code instead

      // We're either searching for the first pixel of the next bright object (state=0)
      // or we're adding bright pixels to the current object (state=1) 
      //
      if (searching) {                             // searching for next bright pixel
        if (bright) {                              // (pixel >= threshold) -- found a new pixel
          if (aboveobj)                            // is the above pixel associated with an object?
            thisobject = aboveobj;                 // if so, then the current pixel is part of that object
          else
            thisobject = ++nextobject;             // if not, then we need to allocate a new object
          addobj(thisobject,x,y);                  //   and work out the bounding box for this object
          xstart = x;                              // keep track of starting place for later
          searching = false;                       // next, add more bright pixels
        } else {
          thisobject = 0;                          // make sure we set zero if not bright
        } // end if bright
      } // end if state
      else {                                       // adding contiguous bright pixels
        if (bright) {                              // if we found a bright pixel
          if ( aboveobj && 
               aboveobj != thisobject) {           // is the pixel above part of a different object?
            redoobj = thisobject;                  // re-do our work back to the start of the present object -- later
            thisobject = aboveobj;                 // delete the current object, use the above object number
          } // end if
        } else {                                   // if this pixel isn't bright, we're done adding
          searching = true;                        // go back to searching for the next bright object
          addobj(thisobject,x-1,y);                // only need to do this at the end
          thisobject = 0;                          // ensure we clear the object mask
        } // end if
      } //* end if state

      camClockH();
      camStepDelay();

      if (redoobj) {
        // delete object
        obj[redoobj].exists = false;               // if so, we've assigned the previous pixel(s) to the wrong object
        addobj(thisobject,xstart,y);               // we already know the leftmost x value so expand bounding box
        //for (x1 = x-1; x1 >= xstart; x1--)         // redo our work, painting objref backwards to xstart
        //  objref[curr][x1] = thisobject;
      }
 
      objref[curr][x] = thisobject;                // no matter what, assign thisobject to current pixel
      if (nextobject+1 >= MAXOBJS-1)               // is nextobject about to go out of bounds?
        nextobject = 0;                            // wrap nextobject now rather than next iteration

      camStepDelay();

    } // end for x
    curr ^= 0x01;       // increment: values are either 0 or 1; so use simple XOR toggle
    prev ^= 0x01;
    if (getPixels)
      Serial.write("\n"); // newline at end of every row helps with troubleshooting
  } /* for y */

  // Go through the remaining rows
  while ( digitalRead(CAM_READ_PIN) == HIGH ) { 
    camClockL();
    camStepDelay();
    camStepDelay();
    camClockH();
    camStepDelay();
    camStepDelay();
  }
  
  //filterobj();
  if (getObjects)
    printobj();
  Serial.write('!');

  camClockL();
  camStepDelay();
  camStepDelay();
}

