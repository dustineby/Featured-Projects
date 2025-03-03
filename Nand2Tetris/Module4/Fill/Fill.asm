// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

// Turn the entire screen black when a key is pressed
// Screen memory map: 16384 (0x4000) to 24575 (0x5FFF)
// Keyboard memory map: 24576 (0x6000)

(LOOP)
@KBD    
D=M     //D = value at keyboard memory map (0 if no key is pressed)

@BLACK  
D;JNE   //if a key is pressed (D != 0), jump to BLACK

@WHITE  
0;JMP   //if no key is pressed (D == 0), jump to WHITE

(BLACK)
@color  
M=-1    //set color to -1 (all 16 bits set to 1)
@FILL   
0;JMP   //jump to FILL

(WHITE)
@color  
M=0     //et color to 0 (all 16 bits set to 0)
@FILL   
0;JMP   //jump to FILL

(FILL)
@SCREEN 
D=A     //D = 16384 (base address of screen)
@addr   
M=D     //Set addr = 16384

(LOOP2)
@color  
D=M     //load color into D

@addr   
A=M     //set A to the current address in addr
M=D     //set the current screen word to the color value

@addr   
M=M+1   //increment addr by 1 to move to next word in screen

@KBD    
D=A     //D = 24576 (KBD), which immediately follows the end of the screen RAM

@addr
D=D-M   //D = 24576 - addr (check if addr has reached the end of the screen)

@LOOP2
D;JGT   //if addr < 24576, repeat the loop until end of screen

@LOOP   
0;JMP   //jump back to main loop to check if keyboard state has changed