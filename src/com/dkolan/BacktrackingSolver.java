package com.dkolan;

import java.util.ArrayList;
import java.util.List;

/**
 Class for a solver using the backtracking algorithm
 **/

public class BacktrackingSolver {
    private Board board;
    private List<Board> solutions = new ArrayList<Board>();
    private long elapsedTime;

    public BacktrackingSolver(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Board> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Board> solutions) {
        this.solutions = solutions;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    // Traverse stated row, return true if num found
    private static boolean isNumInRow(Board board, int num, int row) {
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getBoard()[row][i] == num) {
                return true;
            }
        }
        return false;
    }

    // Traverse stated col, return true if num found
    private static boolean isNumInCol(Board board, int num, int col) {
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getBoard()[i][col] == num) {
                return true;
            }
        }
        return false;
    }

    // Find [coord][coord] of upper-left cell in subgrid = coord - (coord % 3)
    // Iterate through all cells
    // TODO: Implement solving different size grids with modulo
    private static boolean isNumInSubgrid(Board board, int num, int row, int col) {
        int subRow = row - (row % 3);
        int subCol = col - (col % 3);

        // Iterate from upper left cell through each row (subRow + 3) and each col (subCol + 3)
        for (int i = subRow; i < subRow + 3; i++) {
            for (int j = subCol; j < subCol + 3; j++) {
                if (board.getBoard()[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    // Helper function to call all three space checkers
    private static boolean isNumValid(Board board, int num, int row, int col) {
        return !isNumInRow(board, num, row)
                && !isNumInCol(board, num, col)
                && !isNumInSubgrid(board, num, row, col);
    }

    // Iterate through rows/cols
    // Check if cell is "empty"
    // Try nums 1 - size and check if valid
    // Change cell to valid num
    // Backtracking: recursively call solve() to solve remaining cells
    // If recursive solve() returns false, then space is set to 0 and backtracks with the previous recursive call of solve()
    // If iterate through the board without solving, return false
    public boolean solve(Board board) {
        long startTime = System.nanoTime();

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getBoard()[row][col] == 0) {
                    for (int i = 1; i <= board.getSize(); i++) {
                        if (isNumValid(board, i, row, col)) {
                            board.getBoard()[row][col] = i;
                            if (solve(board)) {
                                return true;
                            } else {
                                board.getBoard()[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }

        long endTime = System.nanoTime();
        setElapsedTime(endTime - startTime);

        return true;
    }
}