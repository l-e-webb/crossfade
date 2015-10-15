package com.udacity.gamedev.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by louiswebb on 10/12/15.
 */
public class BoardRenderer extends InputAdapter {

    private Board board;
    private static Levels LEVELS;
    public int moves;

    Viewport viewport;
    private static float TILE_WIDTH;
    private static float OFFSET;
    private static float OFFSET_FACTOR = 0.05f;
    private static Color TILE_ON_COLOR = new Color(0,1,0,1);
    private static Color TILE_OFF_COLOR = new Color(0.1f, 0.1f, 0.1f, 1f);

    public BoardRenderer(float worldWidth, float worldHeight, Viewport viewport) {
        moves = 0;
        LEVELS = new Levels();
        board = new Board(LEVELS.LEVELS[0]);
        this.viewport = viewport;
        TILE_WIDTH = worldWidth / board.WIDTH;
        OFFSET = TILE_WIDTH * OFFSET_FACTOR;
    }

    public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < board.WIDTH; i++) {
            for (int j = 0; j < board.WIDTH; j++) {
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
        float leftEdge = TILE_WIDTH * row;
        float topEdge = TILE_WIDTH * (column);
        renderer.rect(leftEdge + OFFSET, topEdge - OFFSET, TILE_WIDTH - 2 * OFFSET, TILE_WIDTH - 2 * OFFSET);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int[] rowAndCol = getTileFromClick(screenX, screenY);
        if (rowAndCol[0] >= 0 && rowAndCol[0] < board.WIDTH && rowAndCol[1] >= 0 && rowAndCol[1] < board.WIDTH) {
            return makeMove(rowAndCol[0], rowAndCol[1]);
        }
        return false;
    }

    public int[] getTileFromClick(int screenX, int screenY) {
        Vector2 worldClick = viewport.unproject(new Vector2(screenX, screenY));
        Gdx.app.log("CLICK_TEST", "Click at " + worldClick.x + ", " + worldClick.y);
        int row = (int) (worldClick.x / TILE_WIDTH);
        int col = (int) (worldClick.y / TILE_WIDTH);
        return new int[]{row, col};
    }

    public boolean makeMove(int row, int column) {
        moves++;
        return board.selectTile(row, column);
    }
}
