package com.louiswebb.crossfade;

/**
 * Created by louiswebb on 10/12/15.
 */
public class Board {

    public static final int WIDTH = 5;
    private boolean[][] tiles;
    public int moves;

    public Board() {
        tiles = new boolean[WIDTH][WIDTH];
        moves = 0;
    }

    public Board(int[][] level) {
        tiles = new boolean[WIDTH][WIDTH];
        if (WIDTH == level.length) {
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    tiles[i][j] = level[WIDTH - i - 1][j] == 1;
                }
            }
        }
    }

    public boolean getTileValue(int row, int column) {
        if (row >= WIDTH || column >= WIDTH) return false;
        return tiles[row][column];
    }

    private void flipTile(int row, int column) {
        if (row < WIDTH && column < WIDTH) {
            tiles[row][column] = !tiles[row][column];
        }
    }

    public boolean selectTile(int row, int column) {
        if (row >= WIDTH || column >= WIDTH) return false;
        for (int i = 0; i < WIDTH; i++) {
            flipTile(row, i);
        }
        for (int i = 0; i < WIDTH; i++) {
            if (i != row) {
                flipTile(i, column);
            }
        }
        moves++;
        return isWinningState();
    }

    public boolean isWinningState() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (getTileValue(i, j)) return false;
            }
        }
        return true;
    }

}
