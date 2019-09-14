package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;

public class BoardGroup extends Group {

    private static final String LOG_TAG = BoardGroup.class.getSimpleName();

    int boardWidth;
    Tile[][] tiles;

    public BoardGroup(int boardWidth) {
        super();
        this.boardWidth = boardWidth;
        tiles = new Tile[boardWidth][boardWidth];
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Tile tile = new Tile(i, j);
                tiles[i][j] = tile;
                addActor(tile);
                tile.boardGroup = this;
            }
        }
    }

    public void setTileValues(boolean[][] values) {
        if (values.length != boardWidth || values[0].length != boardWidth) {
            Gdx.app.log(LOG_TAG, "Attempting to initialize board with malformed value array.");
        }
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardWidth; j++) {
                tiles[i][j].endFlip();
                tiles[i][j].value = values[i][j];
            }
        }
    }

    public void updateSize(float x, float y, float boardSize, float tilePadding) {
        setPosition(x + tilePadding, y + tilePadding);
        setSize(boardSize, boardSize);
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardWidth; j++) {
                tiles[i][j].updateSize(tilePadding);
            }
        }
    }

    public void updateSize(float x, float y, float boardSize) {
        float tilePadding = boardSize * Board.TILE_PADDING_RATIO;
        float actualSize = boardSize- tilePadding;
        updateSize(x, y, actualSize, tilePadding);
    }
}
