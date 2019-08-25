package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.sound.SoundManager;

public class MainScreen extends ScreenAdapter implements InputProcessor {

    public static MainScreen instance;

    ExtendViewport viewport;
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
        instance = this;
        time = 0f;
        renderer = new ShapeRenderer();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        board = new Board(viewport, renderer);
        uiRenderer = new UIRenderer();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(uiRenderer.getStage());
        multiplexer.addProcessor(board);
        Gdx.input.setInputProcessor(multiplexer);
        SoundManager.setMusic(CrossFadeGame.APP_TYPE != Application.ApplicationType.WebGL);
        goToLevel(1);
    }

    @Override
    public void render(float delta) {
        if (state == State.PLAY) time += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        board.draw();
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

    void goToNextLevel() {
        goToLevel(this.level + 1);
    }

    void reset() {
        board.reset();
        time = 0f;
        state = State.PLAY;
        uiRenderer.newLevel();
    }

    void onLeftButtonClick() {
        if (state == State.PAUSE) return;
        goToLevel(this.level - 1);
    }

    void onCenterButtonClick() {
        if (state == State.PAUSE) return;
        reset();
    }

    void onRightButtonClick() {
        if (state == State.PAUSE) return;
        if (this.level == Levels.getRandomizedLevelIndex()) {
            goToLevel(this.level);
        } else {
            goToLevel(this.level + 1);
        }
    }

    @Override
    public void pause() {
        pauseGame();
        SoundManager.stopMusic();
    }

    @Override
    public void resume() {
        SoundManager.playMusic();
    }

    public void pauseGame() {
        if (state == State.PLAY) {
            state = State.PAUSE;
            uiRenderer.initPause();
        }
    }


    public void unpauseGame() {
        if (state == State.PAUSE) {
            state = State.PLAY;
            uiRenderer.initUnpause();
        }
    }

    public void win() {
        state = State.WIN;
        SoundManager.winSound();
    }

    public boolean inGame() {
        return state == State.PLAY;
    }

    public void togglePause() {
        switch (state) {
            case PLAY:
                pauseGame();
                break;
            case PAUSE:
                unpauseGame();
                break;
            default:
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiRenderer.updateTablePositions();
        board.updateSize();
    }

    @Override
    public void hide() {
        SoundManager.stopMusic();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        board.dispose();
        SoundManager.disposeOfInstance();
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            togglePause();
            return true;
        } else if (keycode == Input.Keys.MENU) {
            pauseGame();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Uncomment to make tapping the screen automatically continue after win.
//        if (state == State.WIN) {
//            goToNextLevel();
//            return true;
//        }
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
