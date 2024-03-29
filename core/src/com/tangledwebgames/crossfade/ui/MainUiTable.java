package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.game.Levels;

class MainUiTable extends Table {

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

    MainUiTable(Skin skin, UiReceiver receiver, Drawable background) {
        super();
        this.highlightStyle = skin.get("highlightStyle", Label.LabelStyle.class);
        this.deemphasisStyle = skin.get("deemphasisStyle", Label.LabelStyle.class);
        //background(background);
        setBounds(
                0,
                MainScreen.WORLD_WIDTH,
                MainScreen.WORLD_WIDTH,
                MainScreen.WORLD_HEIGHT - MainScreen.WORLD_WIDTH
        );
        //This listener handles events whenever the is paused, preventing any UI
        //elements in this table from receiving events.
        addCaptureListener(event -> {
            if (!receiver.isMainUiTableEnabled()) {
                event.stop();
            }
            return false;
        });

        final Label titleLabel = new Label(UiText.TITLE, skin, "titleStyle");
        final Label timeLabel = new Label(UiText.TIME, skin);
        final Label levelLabel = new Label(UiText.LEVEL, skin);
        final Label movesLabel = new Label(UiText.MOVES, skin);
        final Label bestMovesLabel = new Label(UiText.BEST, skin);
        timeNum = new Label("0", skin, "highlightStyle");
        levelNum = new Label("1", skin, "highlightStyle");
        movesNum = new Label("0", skin, "highlightStyle");
        bestMovesNum = new Label("0", skin, "highlightStyle");
        prevButton = new TextButton(UiText.PREVIOUS, skin);
        resetButton = new TextButton(UiText.RESET, skin);
        nextButton = new TextButton(UiText.NEXT, skin);
        menuButton = new TextButton(UiText.MENU, skin);
        levelSelectButton = new TextButton(UiText.LEVEL_SELECT, skin);
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receiver.onPreviousButtonClicked();
            }
        });
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receiver.onResetButtonClicked();
            }
        });
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receiver.onNextButtonClicked();
            }
        });
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receiver.onMenuButtonClicked();
            }
        });
        levelSelectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receiver.onLevelSelectButtonClicked();
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
        nextButton.setText(UiText.NEXT);
        if (level == Levels.getRandomizedLevelIndex()) {
            levelNum.setText(UiText.RANDOM);
            nextButton.setText(UiText.RANDOM_BUTTON);
        } else if (level == Levels.getSandboxLevelIndex()) {
            levelNum.setText(UiText.SANDBOX);
        } else if (level == Levels.getTrollLevelIndex()) {
            levelNum.setText(UiText.UNKNOWN_LEVEL);
        }
        resetRecordDisplay(level);
    }

    void resetRecordDisplay(int level) {
        int recordMoves = CrossFadeGame.game.userManager.getRecordMoves(level);
        if (level > Levels.getHighestLevelIndex() || recordMoves <= 0) {
            bestMovesNum.setText("-");
            bestMovesNum.setStyle(deemphasisStyle);
        } else {
            bestMovesNum.setText(recordMoves + "");
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
