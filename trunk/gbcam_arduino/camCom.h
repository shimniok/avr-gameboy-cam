/**
 * @file       camCom.h
 * @brief      Communication protocol for the gameboy camera
 * 
 * @author     Laurent Saint-Marcel (lstmarcel@yahoo.fr)
 * @date       2005/05/5
 */
 
#ifndef __CAM_COM_H__
#define __CAM_COM_H__

#define TWI_CAMERA 0x01 // TWI (I2C) address of the camera

// orders from PC -> ATMEL
#define CAM_COM_PING            '?'
#define CAM_COM_SET_REGISTERS   'R'
#define CAM_COM_TAKE_PICTURE    'T'
#define CAM_COM_GET_OBJECTS     'O'
#define CAM_COM_SET_MODE        'M'
#define CAM_COM_SET_MIN_MAX     'X'
#define CAM_COM_SET_CLOCK_SPEED 'C'

// answers from ATMEL -> PC
#define CAM_COM_PONG            '!'
#define CAM_COM_GOT_REGISTERS   'K'
#define CAM_COM_SEND_START      'S'
#define CAM_COM_START_READ      'R'
#define CAM_COM_OBJECTS         'O'
#define CAM_COM_STOP_READ       'E'
#define CAM_COM_ERROR           'X'


// modes of the camera
#define CAM_MODE_STANDARD       0x20 // send one byte per pixel = 128*128 bytes data = [0x01:0xFF]
#define CAM_MODE_NO_PIC         0x21 // do not send the picture
#define CAM_MODE_ONE_BIT        0x22 // send a black&white picture, every pixel is sent one 1 bit
#define CAM_MODE_TWO_PIX        0x23 // one pix=4 bytes
#define CAM_MODE_ONE_OVER_FOUR  0x24 // send one pixel over 4 => image size = 64*64

#endif

