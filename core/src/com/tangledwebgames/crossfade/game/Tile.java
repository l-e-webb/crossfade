package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tangledwebgames.crossfade.Assets;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.sound.SoundManager;

class Tile extends Actor {

    static boolean useSprites = true;

    boolean value;
    boolean active;
    int row;
    int column;
    Board board;

    TileState state;
    FlipDirection flipDirection;
    float flipWaitTimer;
    float flipTimer;

    Tile(int row, int column) {
        this(row, column, false);
    }

    Tile(int row, int column, boolean value) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.active = false;
        this.state = TileState.IDLE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Rectangle rect = getRenderRect();
        if (useSprites) {
            batch.setColor(getColor());
            batch.draw(Assets.instance.tile, rect.x, rect.y, rect.width, rect.height);
        } else {
            ShapeRenderer renderer = board.renderer;
            renderer.setColor(getColor());
            Vector2 screenPos = localToStageCoordinates(new Vector2(0, 0));
            renderer.rect(screenPos.x, screenPos.y, getWidth(), getHeight());
        }
    }

    @Override
    public void act(float delta) {
        switch (state) {
            case IDLE: default:
                break;
            case WAITING_TO_FLIP:
                flipWaitTimer -= delta;
                if (flipWaitTimer < 0) {
                    state = TileState.FLIPPING;
                }
                break;
            case FLIPPING:
                flipTimer += delta;
                if (flipTimer > Board.FLIP_DURATION) {
                    state = TileState.IDLE;
                }
                break;
        }
    }

    void flip(FlipDirection direction, float delay) {
        flipDirection = direction;
        if (delay == 0) {
            state = TileState.FLIPPING;
        } else {
            flipWaitTimer = delay;
            state = TileState.WAITING_TO_FLIP;
        }
        flipTimer = 0;
    }

    void flip(FlipDirection direction) {
        flip(direction, 0);
    }

    void init(Board board) {
        this.board = board;
        final Board b = board;
        clearListeners();
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!MainScreen.instance.inGame()) {
                    return false;
                }
                b.updateActiveTiles(row, column);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!MainScreen.instance.inGame()) {
                    return;
                }
                b.clearActiveTiles();
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!MainScreen.instance.inGame() || pointer == -1) {
                    return;
                }
                b.updateActiveTiles(row, column);
                Gdx.app.log("Tile", "activating tiles on enter");
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (!MainScreen.instance.inGame() || pointer == -1) {
                    return;
                }
                b.clearActiveTiles();
                Gdx.app.log("Tile", "clearing active tiles on exit");
                super.exit(event, x, y, pointer, toActor);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!MainScreen.instance.inGame()) {
                    return;
                }
                SoundManager.moveSound();
                if (b.selectTile(row, column)) {
                    MainScreen.instance.win();
                }
                super.clicked(event, x, y);
            }
        });
    }

    void toggle() {
        value = !value;
    }

    void updateSize(float tilePadding) {
        setPosition(getXAnchor(), getYAnchor());
        float tileWidth = getParent().getWidth() / Board.WIDTH;
        setSize(tileWidth - tilePadding, tileWidth - tilePadding);
    }

    float getXAnchor() {
        float tileWidth = getParent().getWidth() / Board.WIDTH;
        return tileWidth * column;
    }

    float getYAnchor() {
        float tileHeight = getParent().getHeight() / Board.WIDTH;
        return tileHeight * (Board.WIDTH - row - 1);
    }

    Rectangle getRenderRect() {
        if (state != TileState.FLIPPING) {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
        Rectangle rect = new Rectangle();
        float widthRatio = 1 - MathUtils.sin(flipTimer * MathUtils.PI / Board.FLIP_DURATION);
        if (flipDirection == FlipDirection.HORIZONTAL) {
            rect.width = getWidth() * widthRatio;
            rect.x = getX() + getWidth() / 2 - rect.width / 2;
            rect.y = getY();
            rect.height = getHeight();
        } else {
            rect.height = getHeight() * widthRatio;
            rect.y = getY() + getHeight() / 2 - rect.height / 2;
            rect.x = getX();
            rect.width = getWidth();
        }
        return rect;
    }

    @Override
    public Color getColor() {
        boolean displayValue;
        switch (state) {
            case IDLE: default:
                displayValue = value;
                break;
            case WAITING_TO_FLIP:
                displayValue = !value;
                break;
            case FLIPPING:
                if (flipTimer < Board.HALF_FLIP_DURATION) {
                    displayValue = !value;
                } else {
                    displayValue = value;
                }
                break;
        }
        if (board.highlightTiles) {
            if (displayValue) {
                return active ? Board.TILE_ON_ACTIVE_COLOR : Board.TILE_ON_COLOR;
            } else {
                return active ? Board.TILE_OFF_ACTIVE_COLOR : Board.TILE_OFF_COLOR;
            }
        } else {
            return displayValue ? Board.TILE_ON_COLOR : Board.TILE_OFF_COLOR;
        }
    }

    enum TileState {
        IDLE,
        WAITING_TO_FLIP,
        FLIPPING
    }

    enum FlipDirection {
        HORIZONTAL,
        VERTICAL
    }


}
