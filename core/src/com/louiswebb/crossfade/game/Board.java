package com.louiswebb.crossfade.game;

/**
 * Class representing the game board and its current state.
 */
class Board {

    static final int WIDTH = 5;
    private boolean[][] tiles;
    int moves;

    Board() {
        tiles = new boolean[WIDTH][WIDTH];
        moves = 0;
    }

    Board(boolean[][] level) {
        tiles = new boolean[WIDTH][WIDTH];
        for (int i = 0; i < level.length && i < WIDTH; i++) {
            for (int j = 0; j < level[i].length && j < WIDTH; j++) {
                tiles[i][j] = level[i][j];
            }
        }
    }

    boolean getTileValue(int row, int column) {
        if (row >= WIDTH || column >= WIDTH) return false;
        return tiles[row][column];
    }

    private void flipTile(int row, int column) {
        if (row < WIDTH && column < WIDTH) {
            tiles[row][column] = !tiles[row][column];
        }
    }

    boolean selectTile(int row, int column) {
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

    boolean isWinningState() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (getTileValue(i, j)) return false;
            }
        }
        return true;
    }

}
