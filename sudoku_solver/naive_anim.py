import math
import boards as boards

## method to print the board to console, will override with tKinter later
def print_board(board):
    ## iterate over each row, then iterate over each column
    ## and print the numbers
    for row in range(len(board)):
        ## print lines every 3rd row
        if row % 3 == 0 and row != 0:
            print('-----------')
        for col in range(len(board[0])):
            ## add vertical lines also
            if col % 3 == 0 and col != 0:
                print('|', end='')
            if col == (len(board) - 1):
                print(board[row][col])
            else:
                print(board[row][col], end = '')
    print('\r')

## check row, column, and square for duplicate of guess
def check_square(board, guess, entry):

    ## grab the row and col of the entry
    entry_row = entry[0]
    entry_col = entry[1]

    ## check row for duplicates
    for col in range(len(board[0])):
        if board[entry_row][col] == guess and entry_col != col:
            return False
    
    ## check col for dupes
    for row in range(len(board)):
        if board[row][entry_col] == guess and entry_row != row:
            return False

    ## check square for dupes
    ## just divide by 3 and take the floor to map 0 1 2 -> 1, etc
    box_row = math.floor(entry[0] / 3)
    box_col = math.floor(entry[1] / 3)

    for row in range(box_row * 3, (box_row + 1) * 3):
        for col in range(box_col * 3, (box_col + 1) * 3):
            if board[row][col] == guess and (row, col) != entry:
                return False

    ## if no duplicates found, return true
    return True

## naive next finder
def find_next_try(board):
    ## just go across rows and down cols to find most upper-left 0
    for row in range(len(board)):
        for col in range(len(board[0])):
            if board[row][col] == 0:
                return (row, col)

    ## if no 0's exist, return False so we know we are done
    return None

## recursive solving method
def naive_solve(board):

    ## see if there is a cell to be solved
    next_try = find_next_try(board)

    ## if not, we are done! exit.
    if not next_try:
        return True 
    ## else, get the row and column of that empty square
    else:
        row, col = next_try
        
    ## try and plug in all digits 1-9 into empty square
    for num in range(1,10):
        ## if one works, put it in and move on and recurse!
        ## recursing here just moves to the next 0 found
        if check_square(board, num, (row, col)):
            board[row][col] = num
            ## this if statement and return true make a "ladder"
            ## that does our backtracking
            ## it allows us to filter a False all the way up our recursion chain
            if naive_solve(board):
                return True
            ## only runs if a not True (ie False) is found
            ## set the last changed square to 0 before we backtrack
            board[row][col] = 0
            
    ## return a False if none of the numbers 1-10 worked in next empty cell
    return False