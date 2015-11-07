package com.louiswebb.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends ScreenAdapter implements InputProcessor {

    private BoardRenderer boardRenderer;
    private Stage stage;
    private Table table;
    private UIRenderer uiRenderer;
    private float time;
    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 768f;
    private ShapeRenderer renderer;
    private FitViewport viewport;

    @Override
    public void show() {
        time = 0;
        renderer = new ShapeRenderer();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        boardRenderer = new BoardRenderer(viewport);
        uiRenderer = new UIRenderer(viewport);
        Gdx.input.setInputProcessor(this);
        initUI();

    }

    private void initUI() {
        stage = new Stage();
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        //table.setDebug(true);
        table.top();
        //table.pad(UIText.TEXT_OFFSET, UIText.TEXT_OFFSET, UIText.TEXT_OFFSET + WORLD_WIDTH, UIText.TEXT_OFFSET);
        Skin skin = new Skin();
        BitmapFont big = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        big.getData().setScale(0.6f);
        BitmapFont small = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        small.getData().setScale(0.25f);
        skin.add("titleFont", big, BitmapFont.class);
        skin.add("font", small, BitmapFont.class);
        Label titleText = new Label(UIText.TITLE, skin, "titleFont", UIText.TEXT_COLOR);
        Label timeText = new Label(UIText.TIME, skin, "font", UIText.TEXT_COLOR);
        Label movesText = new Label(UIText.MOVES, skin, "font", UIText.TEXT_COLOR);
        Label timeNum = new Label(((int) time) + "", skin, "font", UIText.TEXT_COLOR);
        Label moveNum = new Label(boardRenderer.getMoves() + "", skin, "font", UIText.TEXT_COLOR);
        table.add(titleText).colspan(2).minWidth(WORLD_WIDTH).expandX();
        table.row();
        table.add(timeText).expandX();
        table.add(movesText).expandX();
        table.row();
        table.add(timeNum).expandX();
        table.add(moveNum).expandX();
    }

    @Override
    public void render(float delta) {
        time += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        boardRenderer.render(renderer);
        //uiRenderer.render(time, boardRenderer.getMoves());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        renderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
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
        boardRenderer.handleTouch(screenX, screenY);
        return true;
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
