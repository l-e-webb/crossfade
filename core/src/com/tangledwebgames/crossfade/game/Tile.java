package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tangledwebgames.crossfade.sound.SoundManager;

class Tile extends Actor {
    boolean value;
    boolean active;
    int row;
    int column;
    Board board;

    Tile(int row, int column) {
        this(row, column, false);
    }

    Tile(int row, int column, boolean value) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.active = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        ShapeRenderer renderer = board.renderer;
        Color color;
        if (value) {
            color = active ? Board.TILE_ON_ACTIVE_COLOR : Board.TILE_ON_COLOR;
        } else {
            color = active ? Board.TILE_OFF_ACTIVE_COLOR : Board.TILE_OFF_COLOR;
        }
        renderer.setColor(color);
        Vector2 screenPos = localToStageCoordinates(new Vector2(0, 0));
        renderer.rect(screenPos.x, screenPos.y, getWidth(), getHeight());
    }

    void init(Board board) {
        this.board = board;
        final Board b = board;
        clearListeners();
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                b.updateActiveTiles(row, column);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                b.clearActiveTiles();
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                b.updateActiveTiles(row, column);
                Gdx.app.log("Tile", "activating tiles on enter");
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                b.clearActiveTiles();
                Gdx.app.log("Tile", "clearing active tiles on exit");
                super.exit(event, x, y, pointer, toActor);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                b.selectTile(row, column);
                SoundManager.moveSound();
                super.clicked(event, x, y);
            }
        });
    }

    void toggle() {
        value = !value;
    }

    void updateSize(float tilePadding) {
        setPosition(getXPosition(), getYPosition());
        float tileWidth = getParent().getWidth() / Board.WIDTH;
        setSize(tileWidth - tilePadding, tileWidth - tilePadding);
    }

    float getXPosition() {
        float tileWidth = getParent().getWidth() / Board.WIDTH;
        return tileWidth * column;
    }

    float getYPosition() {
        float tileHeight = getParent().getHeight() / Board.WIDTH;
        return tileHeight * (Board.WIDTH - row - 1);
    }




}