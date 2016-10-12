package com.louiswebb.crossfade.game;

import com.badlogic.gdx.math.MathUtils;

/**
 * Class representing the game board and its current state.
 */
public class Board {

    public static final int WIDTH = 5;
    private boolean[][] tiles;
    private boolean[][] originalState;
    private int moves;

    public Board() {
        originalState = new boolean[WIDTH][WIDTH];
        reset();
    }

    public Board(boolean[][] level) {
        initializeLevel(level);
    }

    public void initializeLevel(boolean[][] level) {
        this.originalState = level;
        reset();
    }

    public int getMoves() {
        return moves;
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

    public void makeRandomLevel(boolean solvable) {
        if (solvable) {
            //Wipe board clean.
            tiles = new boolean[WIDTH][WIDTH];
            //Create random solvable board by making a random series of moves from a blank board.
            int randomMoves = MathUtils.random(3, WIDTH * WIDTH * 2 / 3);
            for (int i = 0; i < randomMoves; i++) {
                selectTile(MathUtils.random(WIDTH - 1), MathUtils.random(WIDTH - 1));
            }
            //Copied randomized solvable board to original state.
            originalState = new boolean[WIDTH][WIDTH];
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    originalState[i][j] = tiles[i][j];
                }
            }
        } else {
            originalState = new boolean[WIDTH][WIDTH];
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    originalState[i][j] = MathUtils.randomBoolean();
                }
            }
        }
        reset();
    }

    public void reset() {
        tiles = new boolean[WIDTH][WIDTH];
        for (int i = 0; i < originalState.length && i < WIDTH; i++) {
            for (int j = 0; j < originalState[i].length && j < WIDTH; j++) {
                tiles[i][j] = originalState[i][j];
            }
        }
        moves = 0;
    }

}
