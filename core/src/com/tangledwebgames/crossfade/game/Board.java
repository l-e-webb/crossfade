package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.data.SettingsManager;

/**
 * Class representing the game board and its current state.
 */
public class Board extends GameController {

    static boolean debug = false;

    public static final int WIDTH = 5;

    static final float TILE_PADDING_RATIO = 0.005f;
    static final Color TILE_ON_COLOR = new Color(0, 1, 0, 1);
    static final Color TILE_ON_ACTIVE_COLOR = new Color(1f, 0.55f, 0, 1);
    static final Color TILE_OFF_COLOR = new Color(0.25f, 0.25f, 0.25f, 1);
    static final Color TILE_OFF_ACTIVE_COLOR = new Color(0.275f, 0.16f, 0.1f, 1);

    static final float FLIP_DURATION = 0.25f;
    static final float HALF_FLIP_DURATION = FLIP_DURATION / 2;
    static final float FLIP_DELAY = 0f;

    private static boolean radialFlips = false;
    private static Tile.FlipDirection nonRadialDirection = Tile.FlipDirection.HORIZONTAL;

    ShapeRenderer renderer;

    private BoardGroup boardGroup;
    private Tile[][] tiles;
    private boolean[][] originalState;
    private boolean canWin = true;

    public Board(Viewport viewport, ShapeRenderer renderer) {
        super(viewport);
        this.renderer = renderer;
        boardGroup = new BoardGroup(WIDTH);
        addActor(boardGroup);
        tiles = boardGroup.tiles;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[i][j].init(this);
            }
        }
        originalState = new boolean[WIDTH][WIDTH];
        reset();
        setDebugAll(debug);
    }

    public boolean isWinningState() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (getTileValue(i, j)) return false;
            }
        }
        return true;
    }

    public boolean[][] getBoardState() {
        boolean[][] boardState = new boolean[WIDTH][WIDTH];
        for (int row = 0; row < WIDTH; row++) {
            for (int column = 0; column < WIDTH; column++) {
                boardState[row][column] = getTileValue(row, column);
            }
        }
        return boardState;
    }

    boolean getTileValue(int row, int column) {
        if (row < 0 || row >= WIDTH || column < 0 || column >= WIDTH) return false;
        return tiles[row][column].value;
    }

    private void toggleTile(int row, int column) {
        if (row >= 0 && row < WIDTH && column >= 0 && column < WIDTH) {
            tiles[row][column].toggle();
        }
    }

    private void flipTile(int row, int column, Tile.FlipDirection direction, float delay) {
        if (row >= 0 && row < WIDTH && column >= 0 && column < WIDTH) {
            tiles[row][column].flip(direction, delay);
        }
    }

    void selectTile(int row, int column) {
        selectTile(row, column, SettingsManager.isAnimateTiles());
    }

    void selectTile(int row, int column, boolean animateTiles) {
        if (row < 0 || row >= WIDTH || column < 0 || column >= WIDTH) return;
        for (int i = 0; i < WIDTH; i++) {
            toggleTile(row, i);
        }
        for (int i = 0; i < WIDTH; i++) {
            if (i != row) {
                toggleTile(i, column);
            }
        }
        if (animateTiles) {
            if (!radialFlips) {
                flipTile(row, column, nonRadialDirection, 0);
            }
            for (int d = 1; d < 5; d++) {
                if (radialFlips) {
                    float delay = d * FLIP_DELAY;
                    flipTile(row + d, column, Tile.FlipDirection.VERTICAL, delay);
                    flipTile(row - d, column, Tile.FlipDirection.VERTICAL, delay);
                    flipTile(row, column + d, Tile.FlipDirection.HORIZONTAL, delay);
                    flipTile(row, column - d, Tile.FlipDirection.HORIZONTAL, delay);
                } else {
                    flipTile(row + d, column, nonRadialDirection, 0);
                    flipTile(row - d, column, nonRadialDirection, 0);
                    flipTile(row, column + d, nonRadialDirection, 0);
                    flipTile(row, column - d, nonRadialDirection, 0);
                }
            }
        }
        moves++;
        clearActiveTiles();
        if (isWinningState() && winListener != null && canWin) {
            winListener.onWin();
        }
    }

    protected void initializeLevel(boolean[][] level) {
        this.originalState = level;
        reset();
    }

    public void makeRandomLevel(boolean solvable) {
        if (solvable) {
            //Reset board
            originalState = new boolean[WIDTH][WIDTH];
            reset();
            canWin = false;
            //Create random solvable board by making a random series of moves from a blank board.
            int randomMoves = MathUtils.random(3, WIDTH * WIDTH * 2 / 3);
            for (int i = 0; i < randomMoves; i++) {
                selectTile(MathUtils.random(WIDTH - 1), MathUtils.random(WIDTH - 1), false);
            }
            if (isWinningState()) {
                //If the randomized level is already a win-state, make another.
                makeRandomLevel(true);
                return;
            }
            //Copied randomized solvable board to original state.
            originalState = new boolean[WIDTH][WIDTH];
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    originalState[i][j] = tiles[i][j].value;
                }
            }
            canWin = true;
        } else {
            originalState = new boolean[WIDTH][WIDTH];
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    originalState[i][j] = MathUtils.randomBoolean();
                }
            }
            if (isWinningState()) {
                makeRandomLevel(false);
                return;
            }
        }
        reset();
    }

    protected void resetBoard() {
        setTileValues(originalState);
        updateSize();
    }

    protected void setTileValues(boolean[][] boardState) {
        boardGroup.setTileValues(boardState);
    }

    public void updateSize() {
        float worldWidth = getWidth();
        float tilePadding = worldWidth * TILE_PADDING_RATIO;
        float boardSize = worldWidth - tilePadding;
        boardGroup.updateSize(0, 0, boardSize, tilePadding);

    }

    @Override
    public void draw() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        super.draw();
        renderer.end();
    }

    void updateActiveTiles(int row, int column) {
        clearActiveTiles();
        for (int i = 0; i < WIDTH; i++) {
            tiles[i][column].active = true;
        }
        for (int j = 0; j < WIDTH; j++) {
            tiles[row][j].active = true;
        }
    }

    public void clearActiveTiles() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[i][j].active = false;
            }
        }
    }
}
