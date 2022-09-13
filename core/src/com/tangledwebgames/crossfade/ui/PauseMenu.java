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
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.CrossFadePurchaseManager;
import com.tangledwebgames.crossfade.data.SettingsManager;

class PauseMenu extends Table {

    private CheckBox sfxOn;
    private CheckBox musicOn;
    private Slider sfxVolumeSlider;
    private Slider musicVolumeSlider;
    private CheckBox animateTiles;
    private CheckBox highlightTiles;
    private CheckBox sharingUsageData;
    private Skin skin;
    private boolean listenForChanges = true;

    PauseMenu(Skin skin, UiReceiver receiver, Drawable background) {
        super();
        //Uncomment to see wireframe.
        //setDebug(true);
        background(background);
        pad(Dimensions.PADDING_LARGE);
        this.skin = skin;
        initContents(receiver);
    }

    void initPause() {
        listenForChanges = false;
        sfxOn.setChecked(SettingsManager.isSfxOn());
        musicOn.setChecked(SettingsManager.isMusicOn());
        sfxVolumeSlider.setValue(SettingsManager.getSfxVolume());
        musicVolumeSlider.setValue(SettingsManager.getMusicVolume());
        animateTiles.setChecked(SettingsManager.isAnimateTiles());
        highlightTiles.setChecked(SettingsManager.isHighlightTiles());
        sharingUsageData.setChecked(SettingsManager.isSharingUsageData());
        listenForChanges = true;
    }

    void initContents(UiReceiver receiver) {
        clear();
        final Label pauseLabel = new Label(UiText.PAUSED, skin);
        sfxOn = new CheckBox(UiText.SFX, skin);
        sfxOn.getImage().setScaling(Scaling.fit);
        sfxOn.getImageCell()
                .maxSize(Dimensions.CHECKBOX_SIZE)
                .spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        sfxOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!listenForChanges) return;
                receiver.onSfxCheckboxChanged(sfxOn.isChecked());
            }
        });
        final Label sfxLevelLabel = new Label(UiText.SFX_LEVEL, skin);
        sfxVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!listenForChanges) return;
                receiver.onSfxVolumeSliderChanged(sfxVolumeSlider.getValue());
            }
        });
        musicOn = new CheckBox(UiText.MUSIC, skin);
        musicOn.getImage().setScaling(Scaling.fit);
        musicOn.getImageCell()
                .maxSize(Dimensions.CHECKBOX_SIZE)
                .spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        musicOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!listenForChanges) return;
                receiver.onMusicCheckboxChanged(musicOn.isChecked());
            }
        });
        final Label musicLevelLabel = new Label(UiText.MUSIC_LEVEL, skin);
        musicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!listenForChanges) return;
                receiver.onMusicVolumeSliderChanged(musicVolumeSlider.getValue());
            }
        });
        animateTiles = new CheckBox(UiText.ANIMATE_TILES, skin);
        animateTiles.getImage().setScaling(Scaling.fit);
        animateTiles.getImageCell()
                .maxSize(Dimensions.CHECKBOX_SIZE)
                .spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        animateTiles.getLabelCell().growX();
        animateTiles.getLabel().setWrap(true);
        animateTiles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!listenForChanges) return;
                receiver.onAnimateTilesCheckboxChanged(animateTiles.isChecked());
            }
        });
        highlightTiles = new CheckBox(UiText.HIGHLIGHT_TILES, skin);
        highlightTiles.getImage().setScaling(Scaling.fit);
        highlightTiles.getImageCell()
                .maxSize(Dimensions.CHECKBOX_SIZE)
                .spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        highlightTiles.getLabelCell().growX();
        highlightTiles.getLabel().setWrap(true);
        highlightTiles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!listenForChanges) return;
                receiver.onHighlightTilesCheckboxChanged(highlightTiles.isChecked());
            }
        });
        sharingUsageData = new CheckBox(UiText.DATA_PRIVACY_SETTINGS_ITEM, skin);
        sharingUsageData.getImage().setScaling(Scaling.fit);
        sharingUsageData.getImageCell()
                .maxSize(Dimensions.CHECKBOX_SIZE)
                .spaceRight(Dimensions.CHECKBOX_RIGHT_PADDING);
        sharingUsageData.getLabelCell().growX();
        sharingUsageData.getLabel().setWrap(true);
        sharingUsageData.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!listenForChanges) return;
                receiver.onShareUsageDataCheckboxChanged(sharingUsageData.isChecked());
            }
        });
        final Label privacyPolicyLink = new Label(UiText.DATA_PRIVACY_POLICY, skin, "linkStyle");
        privacyPolicyLink.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!listenForChanges) return;
                receiver.onPauseMenuPrivacyPolicyClicked();
            }
        });
        final Button pauseContinueButton = new TextButton(UiText.CONTINUE, skin);
        pauseContinueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receiver.onPauseMenuContinueButtonClicked();
            }
        });

        row();
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
        add(animateTiles).growX().colspan(2).spaceTop(Dimensions.PADDING_LARGE);
        row();
        add(highlightTiles).growX().colspan(2).spaceTop(Dimensions.PADDING_LARGE);
        row();
        add(sharingUsageData).growX().colspan(2).spaceTop(Dimensions.PADDING_LARGE);
        row();
        add(privacyPolicyLink).center().colspan(2).spaceTop(Dimensions.PADDING_MEDIUM);
        row();
        if (CrossFadePurchaseManager.isPurchaseAvailable()) {
            final Button buyButton = new TextButton(UiText.BUY, skin);
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    receiver.onPauseMenuBuyButtonClicked();
                }
            });
            add(buyButton).center().colspan(2)
                    .spaceTop(Dimensions.PADDING_LARGE)
                    .height(Dimensions.PAUSE_BUTTON_HEIGHT)
                    .minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH);
            row();
        }
        if (CrossFadeGame.game.authManager.isAuthAvailable()) {
            Button authButton;
            if (CrossFadeGame.game.authManager.isSignedIn()) {
                authButton = new TextButton(UiText.SIGN_OUT, skin);
                authButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        receiver.onSignOutButtonClicked();
                    }
                });
            } else {
                authButton = new TextButton(UiText.SIGN_IN, skin);
                authButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        receiver.onSignInButtonClicked();
                    }
                });
            }
            add(authButton).center().colspan(2)
                    .spaceTop(Dimensions.PADDING_LARGE)
                    .height(Dimensions.PAUSE_BUTTON_HEIGHT)
                    .minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH);
            row();
        }
        add(pauseContinueButton).center().colspan(2)
                .spaceTop(Dimensions.PADDING_LARGE)
                .height(Dimensions.PAUSE_BUTTON_HEIGHT)
                .minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH);
    }

}
