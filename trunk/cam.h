/**
 * @file       cam.c
 * @brief      gameboy camera interface for AT90S4433
 * 
 * @author     Laurent Saint-Marcel (lstmarcel@yahoo.fr)
 * @date       2005/07/05
 */

#ifndef __CAM_H__
#define __CAM_H__

/* ------------------------------------------------------------------------ */
/* TYPEDEFS                                                                 */
/* ------------------------------------------------------------------------ */

typedef unsigned char coord;
typedef unsigned char objindex;

typedef struct {
  unsigned char exists;
  coord left, right, top, bottom; /* bounding box */
} object;

/* ------------------------------------------------------------------------ */
/* FUNCTIONS                                                                */
/* ------------------------------------------------------------------------ */

/**
 * @brief Initialise the IO ports for the camera
 */
void camInit(void);

/**
 * @brief Send reset sequence to the camera
 */
void camReset(void);

/**
 * @brief Send register data to camera
 */
void camSetReg(unsigned char regaddr, unsigned char regval);

/**
 * @brief Take a picture, read it and send it trhough the serial port. 
 */
void camReadPicture(void);

/**
 * @brief "delete" object from object array
 */
void delobj(objindex index);

/**
 * @brief "add" object from object array; calculate bounding box
 */
void addobj(objindex index, coord x, coord y);

/**
 * @brief print list of existing objects
 */
void printobj(void);

#endif

