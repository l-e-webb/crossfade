package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;

public class Assets implements Disposable, AssetErrorListener {

    public static final Assets instance = new Assets();

    private static final String LOG_TAG = Assets.class.getSimpleName();
    private static final String TITLE_FONT_PATH = "titleFont.fnt";
    private static final String UI_FONT_PATH = "font.fnt";
    private static final String SMALL_FONT_PATH = "smallFont.fnt";
    private static final String TILE_SPRITE_PATH = "tile.png";
    private static final String TILE_SMALL_SPRITE_PATH = "tileSmall.png";
    private static final String GREY_BOX = "grey_box.png";
    private static final String WHITE_BOX = "white_box.png";
    private static final String SLIDER_BACKGROUND_PATH = "slider_background.png";
    private static final String SLIDER_KNOB_PATH = "slider_knob.png";
    private static final String BACKGROUND_MUSIC_PATH = "test_bgm.mp3";
    private static final String MOVE_SOUND_PATH = "move_sound.wav";
    private static final String BUTTON_SOUND_PATH = "move_sound.wav";
    private static final String WIN_SOUND_PATH = "win_sound.mp3";
    private static final String BASE_GAME_TEXT_PATH = "strings/GameText";

    public BitmapFont titleFont;
    public BitmapFont uiFont;
    public BitmapFont smallFont;
    public Texture tile;
    public Texture tileSmall;
    public Texture greyBox;
    public Texture whiteBox;
    public TextureRegion sliderBackground;
    public Sprite sliderKnob;
    public Music backgroundMusic;
    public Sound moveSound;
    public Sound buttonSound;
    public Sound winSound;
    public I18NBundle gameText;
    private AssetManager assetManager;



    private Assets() {
    }

    void loadAll() {
        assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(TITLE_FONT_PATH, BitmapFont.class);
        assetManager.load(UI_FONT_PATH, BitmapFont.class);
        assetManager.load(SMALL_FONT_PATH, BitmapFont.class);
        assetManager.load(TILE_SPRITE_PATH, Texture.class);
        assetManager.load(TILE_SMALL_SPRITE_PATH, Texture.class);
        assetManager.load(GREY_BOX, Texture.class);
        assetManager.load(WHITE_BOX, Texture.class);
        assetManager.load(SLIDER_BACKGROUND_PATH, Texture.class);
        assetManager.load(SLIDER_KNOB_PATH, Texture.class);
        assetManager.load(BACKGROUND_MUSIC_PATH, Music.class);
        assetManager.load(MOVE_SOUND_PATH, Sound.class);
        assetManager.load(BUTTON_SOUND_PATH, Sound.class);
        assetManager.load(WIN_SOUND_PATH, Sound.class);
        I18NBundleLoader.I18NBundleParameter i18nParam = new I18NBundleLoader.I18NBundleParameter(CrossFadeGame.LOCALE);
        assetManager.load(BASE_GAME_TEXT_PATH, I18NBundle.class, i18nParam);

        assetManager.finishLoading();

        titleFont = assetManager.get(TITLE_FONT_PATH);
        uiFont = assetManager.get(UI_FONT_PATH);
        smallFont = assetManager.get(SMALL_FONT_PATH);
        tile = assetManager.get(TILE_SPRITE_PATH, Texture.class);
        tileSmall = assetManager.get(TILE_SMALL_SPRITE_PATH, Texture.class);
        greyBox = assetManager.get(GREY_BOX, Texture.class);
        whiteBox = assetManager.get(WHITE_BOX, Texture.class);
        sliderBackground = new TextureRegion(assetManager.get(SLIDER_BACKGROUND_PATH, Texture.class));
        sliderKnob = new Sprite(assetManager.get(SLIDER_KNOB_PATH, Texture.class));
        backgroundMusic = assetManager.get(BACKGROUND_MUSIC_PATH);
        moveSound = assetManager.get(MOVE_SOUND_PATH);
        buttonSound = assetManager.get(BUTTON_SOUND_PATH);
        winSound = assetManager.get(WIN_SOUND_PATH);
        gameText = assetManager.get(BASE_GAME_TEXT_PATH);
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
