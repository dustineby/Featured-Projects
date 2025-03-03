An assembler for the course NAND2tetris by Professors Shimon Schocken & Noam Nisan, written in Rust.

For details about the course, see https://www.nand2tetris.org/.

Background: 
    Prior to this project, the course has also built a "computer" from nand gates up, including a CPU and 16k RAM chip, which the "hack assembly language" and "hack binary" are both designed to run on.

    The assembler is designed to take in an assembly-like langauge ("hack assembly"), of 16-bit instructions, which has only 3 allowable instruction types:
        1. Addresses, either through direct RAM addresses or through named addresses.
        2. Computations, which can select both input and writing from either of two registers, or a selected memory address, as well as trigger jumps through a program counter
        3. Labels, which mark the instruction number to jump to for jump calls.

Design:
    The assembler is designed to take the instructions (here, via a file passed through the CLI) and translate them into binary (here, written to a .bin file).

    The general structure of the assembler is as follows:
        1. main.rs is the file called via CLI, it also takes in the arguments from the CLI, and makes sure that an additional argument (expected to be the .asm file) is provided.
            It then passes that additional argument through to the assembler.
        2. assembler.rs contains the Instruction enum used in multiple other files. When the main assembler function is called, it both creates the output file and reads the input file into a string.
            From here, it begins lexical analysis by breaking the input stream into individual instructions (assumed to be 1 per line), discarding comments and whitespace.
            It then performs the first half pass and second half pass through the symbol.rs files methods, first constructing the symbol table and then resolving it to produce lines free of 
            variables that can be parsed and tokenized cleanly.
            After that, the parser and econder methods are called to parse, tokenize, and encode each instruction, after which the assembler then writes the encoding to the file.
        3. symbols.rs holds the symbol table (hashmap) for labels and variables, as well as a counter used for building the table. 
            It has a method to add predefined variables to the hashmap, called when a new symbol table is made,
            as well as the two half passes that make up the first full pass.
            The first looks only for labels, coded as '(LABELS)', adding them to the symbol table, along with their correpsonding instruction set line.
            The second half pass looks for variables, coded as '@VAR', adding them to the symbol table if not present, and replacing them in the instruction set
            with the corresponding RAM address, using the pointer to keep track of the next empty RAM address to use if a new variable name is found. 
        4. parser.rs takes in a string, presumed to be a valid post-symbol instruction, and first tokenizes it to see which type of instruction it is.
            It also has functions to further tokenize both instruction types, either into one single address or into dest, comp, and jump fields.
        5 encoder.rs takes in tokenized instructions and converts them to unsigned 16-bit integers via matching different instruction parts
            and then doing a single bitwise-or with an empty u16. 


Errors, edgecases, and events not handled:
    -Runtime inputs:
        Incorrect input file type (.asm assumed)
    -Line seperation/cleaning:
        .asm file is assumed to be error-free in syntax, only valid instructions, labels, comments, and empty lines
    -Label/variable names:
        Labels and variables are assumed well-named, free of any characters meant to break them such as extra paranthesis or numbers that overwrite predefined RAM addresses, or addresses already accounted for
 