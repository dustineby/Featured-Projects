import math
import boards as boards

## method to print the board to console, will override with tKinter later
def print_board(board):
    ##  iterate over each row, then iterate over each column
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

## fill initial dictionary with empty cells and their possible values
def generate_cell_choice_list(board, dict):
    ## fills the passed dictionary
    ## with tuple keys corresponding to cells
    ## and their values representing all possible values that cell can still take
    ## eg (2,3) : [1,3,4,6,7]

    ## iterate over all cells, check all digits 1-9 for uniqueness
    for row in range(len(board)):
        for col in range(len(board[0])):
            ## only make lists for cells that aren't filled
            if board[row][col] == 0:
                ## make empty list, add unique digits to list
                choices = []
                for num in range(1,10):
                    if check_cell_unique(board, num, (row, col)):
                        choices.append(num)
                dict[(row, col)] = choices
    return dict

## check row, column, and square for duplicate of guess
def check_cell_unique(board, guess, cell):

    ## grab the row and col of the entry
    entry_row = cell[0]
    entry_col = cell[1]

    ## check row for duplicates
    for col in range(len(board[0])):
        if board[entry_row][col] == guess and entry_col != col:
            return False
    
    ## check col for dupes
    for row in range(len(board)):
        if board[row][entry_col] == guess and entry_row != row:
            return False

    ## check square for dupes
    ## just divide by 3 and take the floor to map 0 1 2 -> 0, etc
    box_row = math.floor(cell[0] / 3)
    box_col = math.floor(cell[1] / 3)

    for row in range(box_row * 3, (box_row + 1) * 3):
        for col in range(box_col * 3, (box_col + 1) * 3):
            if board[row][col] == guess and (row, col) != cell:
                return False

    ## if no duplicates found, return true
    return True

## remove filled digits from choice lists of neighbors
def constrain_neighbors(board, guess, cell, dict):
    ## after trying to fill a cell, remove that digit from the possible choices list
    ## for all of that cells neighbors
    ## we build a list and pass it back, in case that cell choice is incorrect
    ## so we can undo it

    newly_constrained = []

    ## grab the row and col of the cell
    entry_row = cell[0]
    entry_col = cell[1]

    ## check every unfilled neighbor in this row
    for col in [num for num in range(len(board[0])) if board[entry_row][num] == 0]:
        ## if the added digit was in their choices, remove it
        if guess in dict[(entry_row, col)]:
            ## add that cell to the new constraints list
            newly_constrained.append((entry_row, col))
            dict[(entry_row, col)].remove(guess)
    
    ## same for the column
    for row in [num for num in range(len(board)) if board[num][entry_col] == 0]:
        if guess in dict[(row, entry_col)]:
            newly_constrained.append((row, entry_col))
            dict[(row, entry_col)].remove(guess)

    ## then the square
    box_row = math.floor(cell[0] / 3)
    box_col = math.floor(cell[1] / 3)

    for row in range(box_row * 3, (box_row + 1) * 3):
        for col in range(box_col * 3, (box_col + 1) * 3):
            if board[row][col] == 0:
                if guess in dict[(row, col)]:
                    newly_constrained.append((row, col))
                    dict[(row, col)].remove(guess)

    ## return the list of cells constrained
    return newly_constrained

## if a cell choic ewas wrong, undo constraints made before backtracking
def relax_neighbors(list, guess, dict):

    ## for each cell constrained, add that guess back
    for cell in list:
        if cell in dict.keys():
            dict[cell].append(guess)
            dict[cell].sort()
        else:
            dict[cell] = []
            dict[cell].append(guess)

## constrained next finder
def find_next_try(dict):
    
    ## check if there's anything left in the dictionary
    if len(dict) != 0:
        ## if so, sort based on possible choices
        ## then pop whatever has the least choices (most constrained cell)
        return sorted(dict, key = lambda cell: len(dict[cell])).pop(0)

    ## if the dict is empty, the puzzle is solved 
    return None
