package com.louiswebb.crossfade;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by louiswebb on 10/15/15.
 */
public class UIRenderer {

    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private static final Color TEXT_COLOR = new Color(0,1,0,1);
    private static final float TEXT_OFFSET = 30f;

    public UIRenderer(Viewport viewport) {
        this.viewport = viewport;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void render() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.setColor(TEXT_COLOR);
        font.draw(batch, "CrossFade", 0 + TEXT_OFFSET, 640 - TEXT_OFFSET);
        batch.end();
    }

    private static class TextArrays {

        public static final boolean[][] C = {
                {true, true, true, true},
                {true, false, false, false},
                {true, false, false, false},
                {true, true, true, true}
        };

        public static final boolean[][] R = {
                {true, true, true, false},
                {true, false, false, true},
                {true, true, true, false},
                {true, false, false, true}
        };

        public static final boolean[][] O = {
                {true, true, true, true},
                {true, false, false, true},
                {true, false, false, true},
                {true, true, true, true}
        };

        public static final boolean[][] S = {
                {true, true, true, true},
                {false, true, false, true},
                {false, false, true, false},
                {true, true, true, true}
        };
    }
}
