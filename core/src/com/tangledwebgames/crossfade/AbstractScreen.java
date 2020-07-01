package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AbstractScreen extends ScreenAdapter {

    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 768f;

    protected Viewport viewport;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    protected CrossFadeGame getGame() {
        return CrossFadeGame.game;
    }
}
