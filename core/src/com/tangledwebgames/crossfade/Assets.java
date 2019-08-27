package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final Assets instance = new Assets();

    private static final String LOG_TAG = Assets.class.getSimpleName();
    private static final String TITLE_FONT_PATH = "titleFont.fnt";
    private static final String UI_FONT_PATH = "font.fnt";
    private static final String TILE_SPRITE_PATH = "tile.png";
    private static final String BUTTON_EMPTY_PATH = "button_empty_9patch.png";
    private static final String BUTTON_FILLED_PATH = "button_filled_9patch.png";
    private static final String BUTTON_TRANSPARENT_PATH = "button_transparent_filled_9patch.png";
    private static final String CHECKBOX_EMPTY_PATH = "checkbox_empty.png";
    private static final String CHECKBOX_FILLED_PATH = "checkbox_filled.png";
    private static final String SLIDER_BACKGROUND_PATH = "slider_background.png";
    private static final String SLIDER_KNOB_PATH = "slider_knob.png";
    private static final String BACKGROUND_MUSIC_PATH = "test_bgm.mp3";
    private static final String MOVE_SOUND_PATH = "move_sound.wav";
    private static final String BUTTON_SOUND_PATH = "move_sound.wav";
    private static final String WIN_SOUND_PATH = "win_sound.mp3";

    public BitmapFont titleFont;
    public BitmapFont uiFont;
    public Texture tile;
    public Texture buttonEmpty;
    public Texture buttonFilled;
    public Texture buttonTransparent;
    public TextureRegion checkboxEmpty;
    public TextureRegion checkboxFilled;
    public TextureRegion sliderBackground;
    public Sprite sliderKnob;
    public String levelData;
    public Music backgroundMusic;
    public Sound moveSound;
    public Sound buttonSound;
    public Sound winSound;
    private AssetManager assetManager;



    private Assets() {
    }

    void loadAll() {
        assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(TITLE_FONT_PATH, BitmapFont.class);
        assetManager.load(UI_FONT_PATH, BitmapFont.class);
        assetManager.load(TILE_SPRITE_PATH, Texture.class);
        assetManager.load(BUTTON_EMPTY_PATH, Texture.class);
        assetManager.load(BUTTON_FILLED_PATH, Texture.class);
        assetManager.load(BUTTON_TRANSPARENT_PATH, Texture.class);
        assetManager.load(CHECKBOX_EMPTY_PATH, Texture.class);
        assetManager.load(CHECKBOX_FILLED_PATH, Texture.class);
        assetManager.load(SLIDER_BACKGROUND_PATH, Texture.class);
        assetManager.load(SLIDER_KNOB_PATH, Texture.class);
        assetManager.load(BACKGROUND_MUSIC_PATH, Music.class);
        assetManager.load(MOVE_SOUND_PATH, Sound.class);
        assetManager.load(BUTTON_SOUND_PATH, Sound.class);
        assetManager.load(WIN_SOUND_PATH, Sound.class);
        assetManager.finishLoading();

        titleFont = assetManager.get(TITLE_FONT_PATH);
        uiFont = assetManager.get(UI_FONT_PATH);
        tile = assetManager.get(TILE_SPRITE_PATH, Texture.class);
        buttonEmpty = assetManager.get(BUTTON_EMPTY_PATH);
        buttonFilled = assetManager.get(BUTTON_FILLED_PATH);
        buttonTransparent = assetManager.get(BUTTON_TRANSPARENT_PATH);
        checkboxEmpty = new TextureRegion(assetManager.get(CHECKBOX_EMPTY_PATH, Texture.class));
        checkboxFilled = new TextureRegion(assetManager.get(CHECKBOX_FILLED_PATH, Texture.class));
        sliderBackground = new TextureRegion(assetManager.get(SLIDER_BACKGROUND_PATH, Texture.class));
        sliderKnob = new Sprite(assetManager.get(SLIDER_KNOB_PATH, Texture.class));
        backgroundMusic = assetManager.get(BACKGROUND_MUSIC_PATH);
        moveSound = assetManager.get(MOVE_SOUND_PATH);
        buttonSound = assetManager.get(BUTTON_SOUND_PATH);
        winSound = assetManager.get(WIN_SOUND_PATH);
        Gdx.app.log(LOG_TAG, (backgroundMusic == null) + "");
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(LOG_TAG, "Error loading asset " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        if (assetManager != null) assetManager.dispose();
    }
}
