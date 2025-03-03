// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

// Works for negatives

// psuedo code

// mult (a,b) -> (v)
// i = a
// if (a < 0):
// 	n = 1
// 	i = -a
// j = b
// if (b < 0):
// 	m = 1
//	j = -b
// v = 0	
// for (i = a; i > 0; i--) {
//	v = v + b
// }
// if (n+m =/= 1) {
// v = -v
// }
// output v


// REAL CODE

@R2 
M=0 //set RAM[2] to 0

@R0
D=M //load a from RAM[0]

@END
D;JEQ //end and output 0 if a = 0

@i  
M=D //set i to a

@APOS
D;JGE //if a >= 0, skip to APOS

@i
M=-D //if a < 0, flip to positive
@n
M=1  //and set flag

(APOS)
@R1
D=M //load b from RAM[1]

@END
D;JEQ //end and output 0 if b = 0

@j 
M=D //set j to b

@BPOS
D;JGE //if b >= 0, skip to BPOS

@j
M=-D //if b < 0, flip to positive
@m
M=1 //and set flag

(BPOS)
@v 
M=0 //set v to 0


(LOOP)
@i
D=M-1
M=D  //i = i-1

@STOP
D;JLT //if i < 0, end loop

@j
D=M //store B in dreg

@v
M=D+M //store (current value of v) + B into v

@LOOP
0;JMP //restart loop

(STOP)
@n
D=M //store value of n flag in D register
@m
D=D+M //add value of m flag to n flag in D register

@POS
D-1;JNE //jump to POS if n+m-1 =/= 0, which will happen iff n = m = 1 or n = m = 0 

@v
M=-M //if only one of n or m is negative, negate value in v

(POS)
@v
D=M //grab value in v

@R2
M=D //store in RAM[2]

(END)
@END
0;JMP //end loop














