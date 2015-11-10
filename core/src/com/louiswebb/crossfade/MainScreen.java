package com.louiswebb.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainScreen extends ScreenAdapter implements InputProcessor {

    public BoardRenderer boardRenderer;
    public UIRenderer uiRenderer;
    public float time;
    public int level;
    public boolean paused;
    public boolean win;
    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 768f;
    private ShapeRenderer renderer;
    public FitViewport viewport;

    @Override
    public void show() {
        time = 0f;
        renderer = new ShapeRenderer();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        boardRenderer = new BoardRenderer(viewport);
        uiRenderer = new UIRenderer(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(uiRenderer.mainStage);
        Gdx.input.setInputProcessor(multiplexer);
        goToLevel(1);
    }


    @Override
    public void render(float delta) {
        if (!paused) time += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        boardRenderer.render(renderer);
        uiRenderer.render(renderer);
    }

    public void goToLevel(int level) {
        this.level = (level > 0 && level < Levels.LEVELS.length) ? level : this.level;
        boardRenderer.newLevel(this.level);
        uiRenderer.newLevel();
        time = 0f;
    }

    public void pause() {
        if (!win) {
            paused = !paused;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        if (win && keycode == Input.Keys.ENTER) {
            goToLevel(level + 1);
            paused = false;
            win = false;
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
        if (!paused && boardRenderer.handleTouch(screenX, screenY)) {
            paused = true;
            win = true;
            return true;
        }
        else if (paused) {
            if (win) {
                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    goToLevel(level + 1);
                    paused = false;
                    win = false;
                } else {
                    return false;
                }
            } else {
                pause();
            }
            return true;
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
}
