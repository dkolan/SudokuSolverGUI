package com.dkolan;

/**
 Board class to allow for expanding to larger boards than 9x9 in the future.
 **/

public class Board {
    private int size;
    private int [][] board;

    public Board(int [][] board, int size) {
        this.board = board;
        this.size = size;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getSize() {
        return size;
    }

    public void printBoard() {
        for (int row = 0; row < getSize(); row++) {
            for (int col = 0; col < getSize(); col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println("");
        }
    }
}