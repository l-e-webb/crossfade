package com.udacity.gamedev.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends ScreenAdapter {

    private BoardRenderer boardRenderer;
    private float time;
    private static final float WORLD_WIDTH = 400f;
    private static final float WORLD_HEIGHT = 640f;
    private ShapeRenderer renderer;
    private FitViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private static final Color TEXT_COLOR = new Color(0,1,0,1);
    private static final float TEXT_OFFSET = 30f;

    @Override
    public void show() {
        time = 0;
        renderer = new ShapeRenderer();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        boardRenderer = new BoardRenderer(WORLD_WIDTH, WORLD_HEIGHT, viewport);
        Gdx.input.setInputProcessor(boardRenderer);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void render(float delta) {
        time += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        boardRenderer.render(renderer);
        renderText();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
    }

    private void renderText() {
        batch.begin();
        font.setColor(TEXT_COLOR);
        font.draw(batch, "CrossFade", 0 + TEXT_OFFSET, 640 - TEXT_OFFSET);
        batch.end();
    }
}
