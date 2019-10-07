package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.tangledwebgames.crossfade.CrossFadePurchaseManager;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.sound.SoundManager;

class MenuTable extends Table {

    private CheckBox sfxOn;
    private CheckBox musicOn;
    private Slider sfxVolumeSlider;
    private Slider musicVolumeSlider;
    private CheckBox animateTiles;
    private CheckBox highlightTiles;
    private Skin skin;
    
    MenuTable(Skin skin, Drawable background) {
        super();
        //Uncomment to see wireframe.
        //setDebug(true);
        background(background);
        pad(Dimensions.PADDING_LARGE);
        this.skin = skin;
        initContents();
    }

    void initPause() {
        sfxOn.setChecked(SoundManager.isSfxOn());
        musicOn.setChecked(SoundManager.isMusicOn());
        sfxVolumeSlider.setValue(SoundManager.getSfxVolume());
        musicVolumeSlider.setValue(SoundManager.getMusicVolume());
        animateTiles.setChecked(MainScreen.instance.getBoard().animateTiles);
        highlightTiles.setChecked(MainScreen.instance.getBoard().highlightTiles);
    }

    void initContents() {
        clear();
        final Label pauseLabel = new Label(UiText.PAUSED, skin);
        sfxOn = new CheckBox(UiText.SFX, skin);
        sfxOn.getImage().setScaling(Scaling.fit);
        sfxOn.getImageCell().maxSize(Dimensions.CHECKBOX_SIZE).spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        sfxOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setSfx(sfxOn.isChecked());
                if (MainScreen.instance.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        final Label sfxLevelLabel = new Label(UiText.SFX_LEVEL, skin);
        sfxVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setSfxVolume(sfxVolumeSlider.getValue());
            }
        });
        musicOn = new CheckBox(UiText.MUSIC, skin);
        musicOn.getImage().setScaling(Scaling.fit);
        musicOn.getImageCell().maxSize(Dimensions.CHECKBOX_SIZE).spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        musicOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setMusic(musicOn.isChecked());
                if (MainScreen.instance.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        final Label musicLevelLabel = new Label(UiText.MUSIC_LEVEL, skin);
        musicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setMusicVolume(musicVolumeSlider.getValue());
            }
        });
        animateTiles = new CheckBox(UiText.ANIMATE_TILES, skin);
        animateTiles.getImage().setScaling(Scaling.fit);
        animateTiles.getImageCell().maxSize(Dimensions.CHECKBOX_SIZE).spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        animateTiles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainScreen.instance.getBoard().animateTiles = animateTiles.isChecked();
                if (MainScreen.instance.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        highlightTiles = new CheckBox(UiText.HIGHLIGHT_TILES, skin);
        highlightTiles.getImage().setScaling(Scaling.fit);
        highlightTiles.getImageCell().maxSize(Dimensions.CHECKBOX_SIZE).spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        highlightTiles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainScreen.instance.getBoard().highlightTiles = highlightTiles.isChecked();
                if (MainScreen.instance.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        final Button pauseContinueButton = new TextButton(UiText.CONTINUE, skin);
        pauseContinueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.unpauseGame();
                SoundManager.buttonSound();
            }
        });
        add(pauseLabel).center().colspan(2);
        row();
        add(sfxOn).left().colspan(2).spaceTop(Dimensions.PADDING_LARGE);
        row();
        add(sfxLevelLabel).left();
        add(sfxVolumeSlider).growX().padLeft(Dimensions.SLIDER_PADDING_LEFT);
        row();
        add(musicOn).left().colspan(2).spaceTop(Dimensions.PADDING_LARGE);
        row();
        add(musicLevelLabel).left();
        add(musicVolumeSlider).growX().padLeft(Dimensions.SLIDER_PADDING_LEFT);
        row();
        add(animateTiles).left().colspan(2).spaceTop(Dimensions.PADDING_LARGE);
        row();
        add(highlightTiles).left().colspan(2).spaceTop(Dimensions.PADDING_LARGE);
        row();
        if (!CrossFadePurchaseManager.isFullVersion()) {
            final Button buyButton = new TextButton(UiText.BUY, skin);
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    MainScreen.instance.purchaseDialog(false);
                }
            });
            add(buyButton).center().colspan(2).spaceTop(Dimensions.PADDING_LARGE).height(Dimensions.PAUSE_BUTTON_HEIGHT).minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH);
            row();
        }
        add(pauseContinueButton).center().colspan(2).spaceTop(Dimensions.PADDING_LARGE).height(Dimensions.PAUSE_BUTTON_HEIGHT).minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH);
    }

}
