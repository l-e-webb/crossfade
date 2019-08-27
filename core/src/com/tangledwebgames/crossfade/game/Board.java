package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.PreferenceWrapper;

/**
 * Class representing the game board and its current state.
 */
public class Board extends Stage {

    static boolean debug = false;

    public static final int WIDTH = 5;

    static final float TILE_PADDING_RATIO = 0.005f;
    static final Color TILE_ON_COLOR = new Color(0, 1, 0, 1);
    static final Color TILE_ON_ACTIVE_COLOR = new Color(1f, 0.55f, 0, 1);
    static final Color TILE_OFF_COLOR = new Color(0.25f, 0.25f, 0.25f, 1);
    static final Color TILE_OFF_ACTIVE_COLOR = new Color(0.275f, 0.16f, 0.1f, 1);

    public boolean animateTiles;
    public boolean highlightTiles;
    Group boardGroup;
    ShapeRenderer renderer;
    private Tile[][] tiles;
    private boolean[][] originalState;
    private int moves;

    public Board(Viewport viewport, ShapeRenderer renderer) {
        super(viewport);
        this.renderer = renderer;
        boardGroup = new Group();
        addActor(boardGroup);
        tiles = new Tile[WIDTH][WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Tile tile = new Tile(i, j);
                tiles[i][j] = tile;
                boardGroup.addActor(tile);
                tile.init(this);
            }
        }
        originalState = new boolean[WIDTH][WIDTH];
        reset();
        animateTiles = PreferenceWrapper.prefs.getBoolean(PreferenceWrapper.ANIMATE_TILES_KEY, false);
        highlightTiles = PreferenceWrapper.prefs.getBoolean(PreferenceWrapper.HIGHLIGHT_TILES_KEY, true);
        setDebugAll(debug);
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
        return tiles[row][column].value;
    }

    private void flipTile(int row, int column) {
        if (row < WIDTH && column < WIDTH) {
            tiles[row][column].toggle();
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
        clearActiveTiles();
        boolean winningState = isWinningState();
        if (isWinningState()) {
            MainScreen.instance.win();
        }
        return winningState;
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
            //Reset board
            originalState = new boolean[WIDTH][WIDTH];
            reset();
            //Create random solvable board by making a random series of moves from a blank board.
            int randomMoves = MathUtils.random(3, WIDTH * WIDTH * 2 / 3);
            for (int i = 0; i < randomMoves; i++) {
                selectTile(MathUtils.random(WIDTH - 1), MathUtils.random(WIDTH - 1));
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
        moves = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[i][j].value = originalState[i][j];
            }
        }
        updateSize();
    }

    public void updateSize() {
        float worldWidth = getWidth();
        float tilePadding = worldWidth * TILE_PADDING_RATIO;
        float boardSize = worldWidth - tilePadding;
        boardGroup.setPosition(
                tilePadding,
                tilePadding
        );
        boardGroup.setSize(boardSize, boardSize);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[i][j].updateSize(tilePadding);
            }
        }
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

    void clearActiveTiles() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[i][j].active = false;
            }
        }
    }
}
