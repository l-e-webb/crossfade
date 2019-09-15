package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.sound.SoundManager;

class MainUITable extends Table {

    private Label timeNum;
    private Label levelNum;
    private Label movesNum;
    private Label bestMovesNum;
    private Label.LabelStyle highlightStyle;
    private Label.LabelStyle deemphasisStyle;

    private TextButton prevButton;
    private TextButton resetButton;
    private TextButton nextButton;
    private TextButton menuButton;
    private TextButton levelSelectButton;
    
    MainUITable(Skin skin, Drawable background) {
        super();
        this.highlightStyle = skin.get("highlightStyle", Label.LabelStyle.class);
        this.deemphasisStyle = skin.get("deemphasisStyle", Label.LabelStyle.class);
        //background(background);
        setBounds(0, MainScreen.WORLD_WIDTH, MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT - MainScreen.WORLD_WIDTH);
        //This listener handles events whenever the game state is not PLAY, preventing any UI
        //elements in this table from receiving events.
        addCaptureListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (!MainScreen.instance.inGame()) {
                    event.stop();
                }
                return false;
            }
        });

        final Label titleLabel = new Label(UIText.TITLE, skin, "titleStyle");
        final Label timeLabel = new Label(UIText.TIME, skin);
        final Label levelLabel = new Label(UIText.LEVEL, skin);
        final Label movesLabel = new Label(UIText.MOVES, skin);
        final Label bestMovesLabel = new Label(UIText.BEST, skin);
        timeNum = new Label("0", skin, "highlightStyle");
        levelNum = new Label("1", skin, "highlightStyle");
        movesNum = new Label("0", skin, "highlightStyle");
        bestMovesNum = new Label("0", skin, "highlightStyle");
        prevButton = new TextButton(UIText.PREVIOUS, skin);
        resetButton = new TextButton(UIText.RESET, skin);
        nextButton = new TextButton(UIText.NEXT, skin);
        menuButton = new TextButton(UIText.MENU, skin);
        levelSelectButton = new TextButton(UIText.LEVEL_SELECT, skin);
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.onPrevButtonClick();
                SoundManager.buttonSound();
            }
        });
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.onResetButtonClick();
                SoundManager.buttonSound();
            }
        });
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.onNextButtonClick();
                SoundManager.buttonSound();
            }
        });
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.pauseGame();
                SoundManager.buttonSound();
            }
        });
        levelSelectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.levelSelect();
                SoundManager.buttonSound();
            }
        });

        //Uncomment to see wireframe.
        //setDebug(true);
        defaults().expandX().center();

        //Title
        add(titleLabel).colspan(3);//.padBottom(PADDING_UNDER_TITLE);
        row();

        //Third row: menu, reset, and level select buttons.
        add(menuButton).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT)
                .fillX().pad(Dimensions.PADDING_SMALL).uniform();
        add(resetButton).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT)
                .fillX().pad(Dimensions.PADDING_SMALL).uniform();
        add(levelSelectButton).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT)
                .fillX().pad(Dimensions.PADDING_SMALL).uniform();
        row();

        //Second row: previous button, level, and next button.
        add(prevButton).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT)
                .fillX().pad(Dimensions.PADDING_SMALL).uniform();
        VerticalGroup levelGroup = new VerticalGroup();
        levelGroup.addActor(levelLabel);
        levelGroup.addActor(levelNum);
        add(levelGroup).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT).uniform();
        add(nextButton).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT)
                .fillX().pad(Dimensions.PADDING_SMALL).uniform();
        row();

        //First row: time, moves, and best moves.
        VerticalGroup timeGroup = new VerticalGroup();
        timeGroup.addActor(timeLabel);
        timeGroup.addActor(timeNum);
        VerticalGroup movesGroup = new VerticalGroup();
        movesGroup.addActor(movesLabel);
        movesGroup.addActor(movesNum);
        VerticalGroup bestMovesGroup = new VerticalGroup();
        bestMovesGroup.addActor(bestMovesLabel);
        bestMovesGroup.addActor(bestMovesNum);
        add(timeGroup).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT).uniform();
        add(movesGroup).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT).uniform();
        add(bestMovesGroup).minHeight(0).maxHeight(Dimensions.MAIN_UI_ROW_HEIGHT).uniform();
        row();
        
    }
    
    void setTimeAndMoves(int time, int moves) {
        timeNum.setText("" + time);
        movesNum.setText("" + moves);
    }

    void updateForNewLevel(int level) {
        levelNum.setText("" + level);
        nextButton.setText(UIText.NEXT);
        if (level == Levels.getRandomizedLevelIndex()) {
            levelNum.setText(UIText.RANDOM);
            nextButton.setText(UIText.RANDOM_BUTTON);
        } else if (level == Levels.getSandboxLevelIndex()) {
            levelNum.setText(UIText.SANDBOX);
        } else if (level == Levels.getTrollLevelIndex()) {
            levelNum.setText(UIText.UNKNOWN_LEVEL);
        }
        if (level > Levels.getHighestLevelIndex() || Levels.records[level] == 0) {
            bestMovesNum.setText("-");
            bestMovesNum.setStyle(deemphasisStyle);
        } else {
            bestMovesNum.setText(Levels.records[level] + "");
            bestMovesNum.setStyle(highlightStyle);
        }
    }

    void disableButtons() {
        setButtonsEnabled(false);
    }

    void enableButtons() {
        setButtonsEnabled(true);
    }

    private void setButtonsEnabled(boolean enabled) {
        prevButton.setDisabled(!enabled);
        resetButton.setDisabled(!enabled);
        nextButton.setDisabled(!enabled);
        menuButton.setDisabled(!enabled);
        levelSelectButton.setDisabled(!enabled);
    }
}
