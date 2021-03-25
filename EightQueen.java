
package eightqueen;

import java.util.Arrays;
import java.util.Random;

public class EightQueen {
    final int row = 8;
    final int col = 8;
    int neighbors = 8;
    boolean newBoard = true;
    int queenLocations = 0;
    int moves = 0;
    int restart;
    int heuristic = 0;
    int[][] board = new int[row][col];
    int[][] tempBoard = new int[row][col];

    public static void main(String[] args) {
        EightQueen trial = new EightQueen();
        trial.initOrRestartBoard();
        trial.moveQueen();
    }

    //check for queens or restart the board
    public void initOrRestartBoard() {
        board = new int[row][col];
        queenLocations = 0;
        randomQueen();
    }

    // fill the grid with randomized queens
    public void randomQueen() {
	Random random = new Random();
            while (queenLocations < 8) {
                    int randomRowIndex = random.nextInt(7);
                    if (board[randomRowIndex][queenLocations] != 1) {
                            board[randomRowIndex][queenLocations] = 1;
                            queenLocations++;
                }
            }
        heuristic = heuristic(board);
    }

    // determines row or column conflicts
    public boolean rowColumnConfl(int[][] test, int a, boolean isRow) { 
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (test[isRow ? i : a][isRow ? a : i] == 1) {
                count++;
                if (count > 1) {
                    return true;
                }
            }
        }
	return false;
    }

    // determines diagonal conflicts
    public boolean diaConf(int[][] test, int a, int b) {
        for (int i = 1; i < 8; i++) {
            if (((a + i < 8) && (b + i < 8) && (test[a + i][b + i] == 1))
		|| ((a - i >= 0) && (b - i >= 0) && test[a - i][b - i] == 1)
		|| ((a - i >= 0) && (b + i < 8) && (test[a - i][b + i] == 1))) {
                return true;
                }
            }
        return false;
    }

    // Counts the number of queens in conflict
    public int heuristic(int[][] test) {
        int count = 0;
        boolean rowEx;
        boolean colEx;
        boolean diaEx;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (test[i][j] == 1) {
                    rowEx = rowColumnConfl(test, j, true);
                    colEx = rowColumnConfl(test, i, false);
                    diaEx = diaConf(test, i, j);

                    if (rowEx || colEx || diaEx) {
                        count++;
                    }
                }
            }
	}
	return count;
    }

    //to move queens
    public void moveQueen() {
        int[][] array = new int[8][8];
        int minCol;
        int minRow;
        int prevQueen = 0;
        int restarts = 0;
        newBoard = false;

        while (true) {
            // create a clone of the actual array
            tempBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
            for (int colIndex = 0; colIndex < 8; colIndex++) {
                for (int i = 0; i < 8; i++) {
                    tempBoard[i][colIndex] = 0;
                }
                    for (int i = 0; i < 8; i++) {
                        if (board[i][colIndex] == 1) {
                            prevQueen = i;
                        }
                        tempBoard[i][colIndex] = 1;
                        array[i][colIndex] = heuristic(tempBoard);
                        tempBoard[i][colIndex] = 0;
                    }
                tempBoard[prevQueen][colIndex] = 1;
            }

            int[] minRowColArr = minRowAndColState(array);
                    
            // determines whether restart is necessary
            if (neighbors == 0) {
                initOrRestartBoard();
                System.out.println("RESTART");
                restarts++;
            }
            
            minRow = minRowColArr[0];
            minCol = minRowColArr[1];

            for (int i = 0; i < 8; i++) {
                board[i][minCol] = 0;
            }

            board[minRow][minCol] = 1;
            moves++;
            heuristic = heuristic(board);

            if (heuristic == 0) {
                System.out.println("\nCurrent State");
                printBoard();
                System.out.println("Solution Found!");
                System.out.println("State changes: " + moves);
		System.out.println("Restarts: " + restarts);
		break;
            }

		System.out.println("\n");
		System.out.println("Current h: " + heuristic);
		System.out.println("Current State");
		printBoard();
		System.out.println("Neighbors found with lower h: " + neighbors);
		System.out.println("Setting new current State");
        }
    }

    //to print board
    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    // finds row and column of minimum neighbor state
    public int[] minRowAndColState(int[][] test) { 
        int minCol = 8;
        int minRow = 8;
        int minVal = 8;
        int count = 0;
        int[] minArr = new int[2];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (test[i][j] < minVal) {
                    minVal = test[i][j];
                    minCol = j;
                    minRow = i;
                }
                if (test[i][j] < heuristic) {
                    count++;
                }
            }
        }
        
        neighbors = count;
        minArr[0] = minRow;
        minArr[1] = minCol;
        return minArr;
    }
    
}
