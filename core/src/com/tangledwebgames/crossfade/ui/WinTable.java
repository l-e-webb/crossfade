package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.sound.SoundManager;

class WinTable extends Table {

    private Label winMessage;
    private Label winTime;
    private Label winMoves;
    private Label winLevel;
    
    WinTable(Skin skin, Drawable background) {
        super();
        //Uncomment to see wireframe.
        //setDebug(true);
        background(background);
        pad(Dimensions.PADDING_LARGE);
        winMessage = new Label(UiText.WIN_MSG, skin);
        winLevel = new Label("", skin);
        winTime = new Label("", skin);
        winMoves = new Label("", skin);
        final TextButton winContinueButton = new TextButton(UiText.CONTINUE, skin);
        winContinueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.unpauseGame();
                MainScreen.instance.goToNextLevel();
                SoundManager.buttonSound();
            }
        });
        add(winMessage).spaceBottom(Dimensions.PADDING_LARGE);
        row();
        add(winLevel);
        row();
        add(winTime);
        row();
        add(winMoves).spaceBottom(Dimensions.PADDING_LARGE);
        row();
        add(winContinueButton).height(Dimensions.PAUSE_BUTTON_HEIGHT).minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH);
    }

    void setWinTimeAndMoves(int time, int moves) {
        winTime.setText(UiText.TIME + ": " + time);
        winMoves.setText(UiText.MOVES + ": " + moves);
    }

    void updateForNewLevel(int level) {
        winMessage.setText(UiText.WIN_MSG);
        winLevel.setText(UiText.LEVEL + ": " + level);
        if (level == Levels.getRandomizedLevelIndex()) {
            winLevel.setText(UiText.LEVEL + ": " + UiText.RANDOM);
        } else if (level == Levels.getTrollLevelIndex()) {
            winLevel.setText(UiText.LEVEL + ": " + UiText.UNKNOWN_LEVEL);
        }
    }

    void newRecordWinText() {
        winMessage.setText(UiText.NEW_RECORD);
    }
}
