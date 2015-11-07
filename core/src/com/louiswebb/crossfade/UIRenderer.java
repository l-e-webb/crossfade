package com.louiswebb.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by louiswebb on 10/15/15.
 */
public class UIRenderer {

    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont uiFont;
    private static final Color TEXT_COLOR = new Color(0, 1, 0, 1);
    private static final float TEXT_OFFSET = 20f;

    private static final String TITLE = "CrossFade";
    private static final String MOVES = "Moves";
    private static final String TIME = "Time";
    private static final String LEVEL = "Level";
    private static final String NEXT = "Next";
    private static final String PREVIOUS = "Previous";
    private static final String WIN_MSG = "You Win!";

    public UIRenderer(Viewport viewport) {
        this.viewport = viewport;
        batch = new SpriteBatch();
        titleFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        titleFont.getData().setScale(0.55f);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        uiFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        uiFont.getData().setScale(0.1f);
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    public void render(float time, int moves) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        titleFont.setColor(TEXT_COLOR);
        titleFont.draw(batch, TITLE, 0 + TEXT_OFFSET, MainScreen.WORLD_HEIGHT - TEXT_OFFSET);
        batch.end();
    }

}