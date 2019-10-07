package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.PauseState;
import com.tangledwebgames.crossfade.ui.UiRenderer;

public class MainScreen extends ScreenAdapter {

    public static MainScreen instance;

    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 768f;

    Viewport viewport;
    Board board;
    UiRenderer uiRenderer;
    ShapeRenderer renderer;

    float time;
    int level;
    State state;


    @Override
    public void show() {
        instance = this;
        state = State.START;
        time = 0f;
        renderer = new ShapeRenderer();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        board = new Board(getViewport(), getRenderer());
        uiRenderer = new UiRenderer(this);
        CrossFadePurchaseManager.setUiRenderer(uiRenderer);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputHandler());
        multiplexer.addProcessor(uiRenderer);
        multiplexer.addProcessor(getBoard());
        Gdx.input.setInputProcessor(multiplexer);
        goToLevel(1);
    }

    @Override
    public void render(float delta) {
        if (getState() == State.PLAY) time = getTime() + delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getViewport().apply();
        getRenderer().setProjectionMatrix(getViewport().getCamera().combined);
        board.act(delta);
        uiRenderer.act(delta);
        getBoard().draw();
        uiRenderer.draw();
    }

    public void goToLevel(int level) {
        if (!CrossFadePurchaseManager.isFullVersion() &&
                level > Levels.MAX_FREE_LEVEL) {
            purchaseDialog(true);
            return;
        }
        if (level >= Levels.getRandomizedLevelIndex()) {
            //Randomized level
            level = Levels.getRandomizedLevelIndex();
            getBoard().makeRandomLevel(true);
        } else if (level == Levels.getSandboxLevelIndex()) {
            getBoard().initializeLevel(Levels.sandboxLevelPlaceholder);
        } else if (level == Levels.getTrollLevelIndex()) {
            //Troll level
            getBoard().initializeLevel(Levels.trollLevel);
        } else if (level <= Levels.getHighestLevelIndex() && level > 0) {
            getBoard().initializeLevel(Levels.levels[level]);
        } else {
            reset();
            return;
        }
        this.level = level;
        uiRenderer.newLevel();
        time = 0f;
        state = State.PLAY;
    }

    public void goToNextLevel() {
        goToLevel(this.getLevel() + 1);
    }

    public void reset() {
        getBoard().reset();
        time = 0f;
        state = State.PLAY;
        uiRenderer.newLevel();
    }

    public void onPrevButtonClick() {
        if (!inGame()) return;
        goToLevel(this.getLevel() - 1);
    }

    public void onResetButtonClick() {
        if (!inGame()) return;
        reset();
    }

    public void onNextButtonClick() {
        if (!inGame()) return;
        if (getLevel() == Levels.getRandomizedLevelIndex()) {
            goToLevel(getLevel());
        } else {
            goToLevel(getLevel() + 1);
        }
    }

    @Override
    public void pause() {
        pauseMenu();
        PreferenceWrapper.flush();
        Levels.saveRecords();
        SoundManager.stopMusic();
    }

    @Override
    public void resume() {
        SoundManager.playMusic();
    }

    public boolean inGame() {
        return getState() == State.PLAY;
    }

    public void pauseGame() {
        state = State.PAUSE;
    }

    public void pauseMenu() {
        if (getState() == State.PLAY) {
            board.clearActiveTiles();
            pauseGame();
            uiRenderer.initPause(PauseState.MENU);
        }
    }

    public void win() {
        //Sandbox level cannot win
        if (level == Levels.getSandboxLevelIndex()) return;

        state = State.PAUSE;
        if (level <= Levels.getHighestLevelIndex() &&
                (Levels.records[level] == 0 ||
                board.getMoves() < Levels.records[level])) {
            Levels.records[level] = board.getMoves();
            uiRenderer.newRecordWinText();
        }
        SoundManager.winSound();
        uiRenderer.initPause(PauseState.WIN);
    }

    public void levelSelect() {
        pauseGame();
        uiRenderer.initPause(PauseState.LEVEL_SELECT);
    }

    public void purchaseDialog(boolean fromAttemptToAccessUnavailableContent) {
        pauseGame();
        uiRenderer.showPurchaseDialog(fromAttemptToAccessUnavailableContent);
    }

    public void unpauseGame() {
        if (inGame()) return;
        uiRenderer.initPause(PauseState.NOT_PAUSED);
        state = State.PLAY;
    }

    public void togglePause() {
        switch (getState()) {
            case PLAY:
                pauseMenu();
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
        getViewport().update(width, height, true);
        uiRenderer.updateTablePositions();
        getBoard().updateSize();
    }

    @Override
    public void hide() {
        PreferenceWrapper.flush();
        SoundManager.stopMusic();
    }

    @Override
    public void dispose() {
        getRenderer().dispose();
        getBoard().dispose();
        Assets.instance.dispose();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public float getTime() {
        return time;
    }

    public int getLevel() {
        return level;
    }

    public State getState() {
        return state;
    }

    public ShapeRenderer getRenderer() {
        return renderer;
    }

    public Board getBoard() {
        return board;
    }

    class InputHandler extends GestureDetector {

        GestureHandler gestureHandler;

        InputHandler() {
            this(new GestureHandler());
        }

        private InputHandler(GestureHandler gestureHandler) {
            super(gestureHandler);
            this.gestureHandler = gestureHandler;
        }

        @Override
        public boolean touchDown(int x, int y, int pointer, int button) {
            //Uncomment to make tapping the screen automatically continue after win.
//          if (state == State.WIN) {
//              goToNextLevel();
//              return true;
//          }
            //Uncomment to pause by swiping down
//            gestureHandler.touchStartX = x;
//            gestureHandler.touchStartY = y;
            return false;
        }

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ESCAPE) {
                togglePause();
                return true;
            } else if (keycode == Input.Keys.MENU) {
                pauseMenu();
                return true;
            }
            return false;
        }
    }

    class GestureHandler extends GestureDetector.GestureAdapter {

        static final float PAN_REGISTER_RATIO = 0.2f;
        static final float SCREEN_TOP_MARGIN_RATIO = 0.15f;
        float screenTopMargin;
        float panRegisterLength;
        float touchStartX;
        float touchStartY;

        GestureHandler() {
            super();
            screenTopMargin = getViewport().getScreenHeight() * SCREEN_TOP_MARGIN_RATIO;
            panRegisterLength = (float)Math.pow(getViewport().getScreenHeight() * PAN_REGISTER_RATIO, 2);
        }

        //Uncomment to pause by swiping down
//        @Override
//        public boolean panStop(float x, float y, int pointer, int button) {
//            if (touchStartY < screenTopMargin){
//                float deltaX = Math.abs(x - touchStartX);
//                float deltaY = Math.abs(y - touchStartY);
//                float distance2 = deltaX * deltaX + deltaY * deltaY;
//                if (distance2 > panRegisterLength) {
//                    pauseMenu();
//                    return true;
//                }
//            }
//            return super.panStop(x, y, pointer, button);
//        }
    }

    public enum State {
        START,
        PLAY,
        PAUSE,
    }
}
