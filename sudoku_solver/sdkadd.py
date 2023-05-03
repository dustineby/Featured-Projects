## small function to help solve Killer Sudokus

## given a target sum, a number of blocks, and a list of excluded digits,
## this func will reutrn all possible valid digit combinations


def find_combinations(target, blocks, excl):

    ## upper limit on useable digits
    ## (could be reduced more for block counts > 2 to speedup but search space is already v small)
    upper = (10) if (target > 9) else (target)
    ## create list, removing any excluded digits
    allowed = [x for x in range(1, upper) if x not in excl]

    ## create 1-block iteration of list of possible permutations
    ## also acts as our first 'loop' for following section, hence "blocks - 1"
    perms = [(x,) for x in allowed]

    ## itaratively build new list of possible permutations per block
    ## nested list comprehension
    ## y = tuples of previous permutations that get unpacked
    ## x = new candidates that get added to each existing permutation
    ## then we pack back into a tuple
    for loop in range(blocks - 1):
        perms = [(*y, x) for x in allowed for y in perms if x > y[loop]]

    ## check each permutation for matching target sum, add to list of solutions if so
    sols = [combo for combo in perms if sum(combo) == target]

    ## print valid solutions
    print(sols)

find_combinations(28, 5, [])
