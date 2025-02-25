Errors, edgecases, and events not handled:

-Runtime inputs:
    Incorrect input file type (.asm assumed)

-Line seperation/cleaning:
    .asm file is assumed to be error-free in syntax,
        only valid instructions, labels, comments, and empty lines

-Label/variable names:
    Labels and variables are assumed well-named, free of any characters meant to
        break them such as extra paranthesis or numbers that overwrite predefined RAM addresses,
        or addresses already accounted for
 