import tkinter as tk
import sys
from threading import*
from functools import partial
import boards as brd
import naive_anim as nv_slv
import recurs_anim as rc_slv

## GUI class
class SudokuGUI:
    def __init__(self):

        ## create main window
        self.window = tk.Tk()
        self.window.withdraw()

        ## vars for tkinter frames
        self.naive_count = 0
        self.recur_count = 0

        ## create empty board
        self.board = [[0 for x in range(9)] for y in range(9)]

        ## 2d array to hold our board labels
        self.num = [[0] * 9 for _ in range(9)]

        ## tkinter frames
        self.solve_frame = tk.Frame(self.window)
        self.solve_frame.grid(row = 0, column = 0)

        self.button_frame = tk.Frame(self.window)
        self.button_frame.grid(row = 1, column = 0, columnspan = 4)

        ## buttons for both solve types
        self.naive_button = tk.Button(self.button_frame, text = 'Naive Solve', command = self.start_solve_naive, font=("Arial", 14))
        self.naive_button.grid(row = 0, column = 0, padx = 5, pady = 4)

        self.heur_button = tk.Button(self.button_frame, text = 'Heuristic Solve', command = self.start_solve_recur, font=("Arial", 14))
        self.heur_button.grid(row = 0, column = 2, padx = 5, pady = 4)


        ## frame and buttons for loading puzzles od different difficulty
        self.load_frame = tk.Frame(self.window)
        self.load_frame.grid(row = 0, column = 3, rowspan = 3)

        self.load_ex = tk.Button(self.load_frame, text = '★☆☆☆☆', command = partial(self.load_board, brd.ex_board), font=("Arial", 14, 'bold'))
        self.load_ex.grid(row = 0, column = 0)
        
        self.load_ez = tk.Button(self.load_frame, text = '★★☆☆☆', command = partial(self.load_board, brd.ez_board), font=("Arial", 14, 'bold'))
        self.load_ez.grid(row = 1, column = 0)

        self.load_med = tk.Button(self.load_frame, text = '★★★☆☆', command = partial(self.load_board, brd.med_board), font=("Arial", 14, 'bold'))
        self.load_med.grid(row = 2, column = 0)

        self.load_hard1 = tk.Button(self.load_frame, text = '★★★★☆', command = partial(self.load_board, brd.hard_board), font=("Arial", 14, 'bold'))
        self.load_hard1.grid(row = 3, column = 0)

        self.load_hard2 = tk.Button(self.load_frame, text = '★★★★★', command = partial(self.load_board, brd.hard_board2), font=("Arial", 14, 'bold'))
        self.load_hard2.grid(row = 4, column = 0)     

        ## create an entry screen
        self.play = tk.Toplevel()

        ## play button to start main screen
        self.playbutt = tk.Button(self.play, text = 'Play!', command = self.game_window)
        self.playbutt.grid()

        ## start the tkinter window
        tk.mainloop()

    ## make the main game window
    def game_window(self): 

        ## get rid of the play button window
        self.play.destroy()

        ## initiate board
        self.set_board()
                
        ## show main window
        self.window.deiconify()

    def set_board(self):

        ## create our game grid
        ## loop over both rows and columns, setting both background colors to better differ cell blocks
        ## as well as setting text color for initial values
        for x in range(9):
            for y in range(9):

                bg_color = 'white'

                fg_color = 'blue' if self.board[x][y] != 0 else 'black'
                
                if x in [0, 1, 2, 6, 7, 8] and y in [0, 1, 2, 6, 7, 8]:
                    bg_color = 'darkgrey'
                elif x in [3, 4, 5] and y in [3, 4, 5]:
                    bg_color = 'darkgrey'
                
                ## our arrays use 0 for empty cells, for better ascii reading, but we want to just display empty cells
                show_num = ' ' if self.board[x][y] == 0 else self.board[x][y]

                self.num[x][y] = tk.Label(self.solve_frame, text = show_num, width = 3, height = 2, relief = 'solid', bg = bg_color, fg = fg_color, font=("Arial", 20, 'bold'))
                self.num[x][y].grid(row = x, column = y, padx = 0, pady = 0)

    def update_board(self):

        ## update our game grid
        for x in range(9):
            for y in range(9):
                show_num = ' ' if self.board[x][y] == 0 else self.board[x][y]
                self.num[x][y].config(text = show_num)

    ## call to load a new board from various buttons
    def load_board(self, board):
        ## make a deep copy here, shallow copy will make any given board unable to run twice
        ## making comparing algos hard
        self.board = [row[:] for row in board]
        self.set_board()

    ## start naive solve
    def start_solve_naive(self):
        self.solve_thread = Thread(target = self.solve1)
        self.solve_thread.start()
        return
    
    ## start dynamic solve
    def start_solve_recur(self):
        self.solve_thread = Thread(target = self.solve2)
        self.solve_thread.start()
        return

    def solve1(self):
        naive_solve(self, self.board)
        return

    def solve2(self):
        constrained_solve_start(self, self.board)
        return


## recursive solving method
def naive_solve(GUI, board):

    GUI.update_board()

    ## see if there is a cell to be solved
    next_try = nv_slv.find_next_try(board)

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
        if nv_slv.check_square(board, num, (row, col)):
            board[row][col] = num
            ## this if statement and return true make a "ladder"
            ## that does our backtracking
            ## it allows us to filter a False all the way up our recursion chain
            if naive_solve(GUI, board):
                return True
            ## only runs if a not True (ie False) is found
            ## set the last changed square to 0 before we backtrack
            board[row][col] = 0
            
    ## return a False if none of the numbers 1-10 worked in next empty cell
    return False


## initiates solver, then calls recursive method
def constrained_solve_start(GUI, board):

    ## create empty dictionary for cell-choice key-value pairs
    cell_choice_dict = {}

    ## fill the dict
    rc_slv.generate_cell_choice_list(board, cell_choice_dict)

    ## run the recursive method
    constrained_solve(GUI, board, cell_choice_dict)

## recursive part of the solver
def constrained_solve(GUI, board, dict):

    GUI.update_board()

    ## see if there is a cell to be solved
    next_try = rc_slv.find_next_try(dict)

    ## if not, we are done! exit.
    if not next_try:
        return True 
    ## else, get the row and column of that empty square
    ## pop it from the dict to remove it from candidates next call
    else:
        row, col = next_try
        next_try_list = dict.pop(next_try)

    ## if the list is size 0, we have over-constrained a cell
    ## and we automatically fail, and must backtrack
    ## this is our forward-check step
    if len(next_try_list) > 0:
        ## try all possible digits in the cells choice list
        for num in next_try_list:
            ## fill the board BEFORE we constrain so our constraint method
            ## doesn't cause an error when reaching the current cell 
            board[row][col] = num
            ## constrain the cells neighbors, and keep them in a list
            constrain_new = rc_slv.constrain_neighbors(board, num, next_try, dict)

            ## this if statement and return true make a "ladder"
            ## that does our backtracking
            ## it allows us to filter a False all the way up our recursion chain
            if constrained_solve(GUI, board, dict):
                return True
            ## only runs if a not True (ie False) is found
            ## set the last changed square to 0 before we backtrack or try another digit
            board[row][col] = 0
            ## relax the neighbors if a tried digit doesn't work
            rc_slv.relax_neighbors(constrain_new, num, dict)
        ## add this cell and its choice list back to the dict if none of the choices worked,
        ## ie we have to backtrack 
        dict[next_try] = next_try_list

            
    # return a False if none of the numbers 1-10 worked in next empty cell
    return False

spawn_gui = SudokuGUI()

