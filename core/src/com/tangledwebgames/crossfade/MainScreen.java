package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.data.AssetLoader;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.game.GameController;
import com.tangledwebgames.crossfade.ui.UiController;

public class MainScreen implements Screen {

    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 768f;

    private Viewport viewport;
    private ShapeRenderer renderer;
    private UiController uiController;
    private GameController gameController;

    @Override
    public void show() {
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        renderer = new ShapeRenderer();
        gameController = new Board(viewport, renderer);
        uiController = new UiController(viewport);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputHandler());
        multiplexer.addProcessor(uiController);
        multiplexer.addProcessor(gameController);
        Gdx.input.setInputProcessor(multiplexer);
        MainController.init(gameController, uiController);
        MainController.instance.show();
    }

    @Override
    public void resume() {
        MainController.instance.resume();
    }

    @Override
    public void pause() {
        MainController.instance.pause();
    }

    @Override
    public void hide() {
        MainController.instance.hide();
    }

    @Override
    public void dispose() {
        MainController.instance.dispose();
        uiController.dispose();
        gameController.dispose();
        AssetLoader.instance.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        gameController.act(delta);
        uiController.act(delta);
        gameController.draw();
        uiController.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiController.updateTablePositions();
        gameController.updateSize();
    }

    static class InputHandler extends InputAdapter {

        private InputHandler() {
        }

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ESCAPE) {
                MainController.instance.togglePause();
                return true;
            } else if (keycode == Input.Keys.MENU) {
                MainController.instance.showMainMenu();
                return true;
            }
            return false;
        }

    }

}
