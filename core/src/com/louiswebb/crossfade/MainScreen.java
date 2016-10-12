package com.louiswebb.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.louiswebb.crossfade.game.Board;
import com.louiswebb.crossfade.game.BoardRenderer;
import com.louiswebb.crossfade.game.Levels;

public class MainScreen extends ScreenAdapter implements InputProcessor {

    ExtendViewport viewport;
    BoardRenderer boardRenderer;
    Board board;
    UIRenderer uiRenderer;

    float time;
    int level;
    State state;

    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 768f;

    private ShapeRenderer renderer;

    @Override
    public void show() {
        time = 0f;
        renderer = new ShapeRenderer();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        board = new Board();
        boardRenderer = new BoardRenderer(viewport, board);
        uiRenderer = new UIRenderer(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(uiRenderer.getStage());
        Gdx.input.setInputProcessor(multiplexer);
        goToLevel(1);
    }

    @Override
    public void render(float delta) {
        if (state == State.PLAY) time += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        boardRenderer.render(renderer);
        uiRenderer.render();
    }

    void goToLevel(int level) {
        if (level >= Levels.getRandomizedLevelIndex()) {
            //Randomized level
            level = Levels.getRandomizedLevelIndex();
            board.makeRandomLevel(true);
        } else if (level == Levels.getTrollLevelIndex()) {
            //Troll level
            board.initializeLevel(Levels.trollLevel);
        } else if (level <= Levels.getHighestLevelIndex() && level > 0) {
            board.initializeLevel(Levels.levels[level]);
        } else {
            reset();
            return;
        }
        this.level = level;
        uiRenderer.newLevel();
        time = 0f;
        state = State.PLAY;
    }

    void reset() {
        board.reset();
        time = 0f;
        state = State.PLAY;
        uiRenderer.newLevel();
    }

    void onLeftButtonClick() {
        goToLevel(this.level - 1);
    }

    void onCenterButtonClick() {
        reset();
    }

    void onRightButtonClick() {
        if (this.level == Levels.getRandomizedLevelIndex()) {
            goToLevel(this.level);
        } else {
            goToLevel(this.level + 1);
        }
    }

    public void pause() {
        switch (state) {
            case PLAY:
                state = State.PAUSE;
                break;
            case PAUSE:
                state = State.PLAY;
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiRenderer.updateTablePositions();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            pause();
            return true;
        }
        if (state == State.WIN && keycode == Input.Keys.ENTER) {
            goToLevel(level + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (state) {
            case PLAY:
                if (boardRenderer.handleTouch(screenX, screenY)) {
                    state = State.WIN;
                    return true;
                }
                return false;
            case PAUSE:
                if (CrossFadeGame.APP_TYPE == Application.ApplicationType.Android) {
                    state = State.PLAY;
                    return true;
                }
                return false;
            case WIN:
                if (CrossFadeGame.APP_TYPE == Application.ApplicationType.Android) {
                    goToLevel(level + 1);
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    enum State {
        PLAY,
        PAUSE,
        WIN
    }
}
