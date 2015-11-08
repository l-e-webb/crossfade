package com.louiswebb.crossfade;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by louiswebb on 10/12/15.
 */
public class BoardRenderer {

    private Board board;

    Viewport viewport;
    private static float TILE_WIDTH;
    private static float OFFSET;
    private static float OFFSET_FACTOR = 0.04f;
    private static final Color TILE_ON_COLOR = new Color(0,1,0,1);
    private static final Color TILE_OFF_COLOR = new Color(0.1f, 0.1f, 0.1f, 1f);

    public BoardRenderer(Viewport viewport) {
        board = new Board(Levels.LEVELS[0]);
        this.viewport = viewport;
        TILE_WIDTH = MainScreen.WORLD_WIDTH / Board.WIDTH;
        OFFSET = TILE_WIDTH * OFFSET_FACTOR;
    }

    public void newLevel(int level) {
        board = new Board(Levels.LEVELS[level]);
        board.moves = 0;
    }

    public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < Board.WIDTH; i++) {
            for (int j = 0; j < Board.WIDTH; j++) {
                renderTile(i, j, renderer);
            }
        }
        renderer.end();
    }

    private void renderTile(int row, int column, ShapeRenderer renderer) {
        if (board.getTileValue(row, column)) {
            renderer.setColor(TILE_ON_COLOR);
        } else {
            renderer.setColor(TILE_OFF_COLOR);
        }
        float leftEdge = TILE_WIDTH * column;
        float botEdge = TILE_WIDTH * row;
        renderer.rect(leftEdge + OFFSET, botEdge + OFFSET, TILE_WIDTH - 2 * OFFSET, TILE_WIDTH - 2 * OFFSET);
    }


    public boolean handleTouch(int screenX, int screenY) {
        int[] rowAndCol = getTileFromClick(screenX, screenY);
        if (rowAndCol[0] >= 0 && rowAndCol[0] < Board.WIDTH && rowAndCol[1] >= 0 && rowAndCol[1] < board.WIDTH) {
            return makeMove(rowAndCol[0], rowAndCol[1]);
        }
        return false;
    }

    public int[] getTileFromClick(int screenX, int screenY) {
        Vector2 worldClick = viewport.unproject(new Vector2(screenX, screenY));
        int col = (int) (worldClick.x / TILE_WIDTH);
        int row = (int) (worldClick.y / TILE_WIDTH);
        return new int[]{row, col};
    }

    public boolean makeMove(int row, int column) {
        return board.selectTile(row, column);
    }

    public int getMoves() {
        return board.moves;
    }
}
