package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.game.BoardGroup;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.sound.SoundManager;

import static com.tangledwebgames.crossfade.game.Levels.getRandomizedLevelIndex;

public class LevelSelectTable extends Table {

    static float itemPrefWidth;

    Drawable itemBackground;
    Drawable itemBackgroundHighlight;
    Skin skin;
    VerticalGroup levelSelectGroup;
    boolean initialValidation = false;

    LevelSelectTable(Skin skin, Drawable background, Drawable scrollBoxBackground, Drawable itemBackground, Drawable itemBackgroundHighlight) {
        super();
        //Uncomment to see wireframe
        //setDebug(true);
        itemPrefWidth = MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO - 2 * Dimensions.PADDING_LARGE;
        background(background);
        pad(Dimensions.PADDING_LARGE);
        this.skin = skin;
        this.itemBackground = itemBackground;
        this.itemBackgroundHighlight = itemBackgroundHighlight;
        levelSelectGroup = new VerticalGroup();
        levelSelectGroup.grow().wrap(false).left().columnLeft().space(Dimensions.LEVEL_SELECT_ITEM_PADDING);
        for (int i = 1; i <= getRandomizedLevelIndex(); i++) {
            levelSelectGroup.addActor(new LevelSelectListItem(i));
        }

        ScrollPane levelSelectPane = new ScrollPane(levelSelectGroup, skin);
        levelSelectPane.setFadeScrollBars(false);
        levelSelectPane.setScrollingDisabled(true, false);
        levelSelectPane.setOverscroll(false, false);
        Container<ScrollPane> levelSelectPaneContainer = new Container<>(levelSelectPane);
        levelSelectPaneContainer.fill();
        levelSelectPaneContainer.background(scrollBoxBackground);

        Label levelSelectHeading = new Label(UIText.LEVEL_SELECT_HEADING, skin);
        TextButton continueButton = new TextButton(UIText.CONTINUE, skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                MainScreen.instance.unpauseGame();
            }
        });
        add(levelSelectHeading).center().spaceBottom(Dimensions.PADDING_MEDIUM);
        row();
        add(levelSelectPaneContainer).grow().spaceBottom(Dimensions.PADDING_LARGE);
        row();
        add(continueButton).center().height(Dimensions.PAUSE_BUTTON_HEIGHT).minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible() && !initialValidation) {
            updateAll();
        }
        super.draw(batch, parentAlpha);
    }

    void updateAll() {
        for (Actor child : levelSelectGroup.getChildren()) {
            try {
                LevelSelectListItem item = (LevelSelectListItem) child;
                item.updateBoardGroupPosition();
                item.updateRecordLabelText();
            } catch (ClassCastException e) {}
        }
    }

    private class LevelSelectListItem extends Table {

        Label recordNumLabel;
        BoardGroup boardGroup;
        int level;

        LevelSelectListItem(int level) {
            super();
            //Uncomment to see wireframe.
            //setDebug(true);
            background(itemBackground);
            setTouchable(Touchable.enabled);
            this.level = level;
            Label levelLabel = new Label(UIText.LEVEL + ":", skin);
            String levelNumLabelText = "";
            if (level <= Levels.getHighestLevelIndex()) {
                levelNumLabelText += level;
            } else if (level == Levels.getSandboxLevelIndex()) {
                levelNumLabelText += UIText.SANDBOX;
            } else if (level == Levels.getTrollLevelIndex()) {
                levelNumLabelText += UIText.UNKNOWN_LEVEL;
            } else if (level == getRandomizedLevelIndex()) {
                levelNumLabelText += UIText.RANDOM;
            }
            Label levelNumLabel = new Label(levelNumLabelText, skin, "highlightStyle");
            Label recordLabel = new Label(UIText.BEST + ":", skin);
            recordNumLabel = new Label("", skin);

            pad(Dimensions.PADDING_SMALL);
            //padRight(Dimensions.PADDING_MEDIUM);
            defaults().left().pad(Dimensions.PADDING_SMALL).expandY();
            add(levelLabel);
            add(levelNumLabel);
            row();
            add(recordLabel);
            add(recordNumLabel);
            left();

            boardGroup = new BoardGroup(Board.WIDTH);
            boardGroup.setTileValues(Levels.getLevel(level));
            addActor(boardGroup);

            addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    background(itemBackgroundHighlight);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    background(itemBackground);
                    super.touchUp(event, x, y, pointer, button);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    if (pointer != -1) {
                        background(itemBackground);
                    }
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (MainScreen.instance.getLevel() != level) {
                        MainScreen.instance.goToLevel(level);
                        SoundManager.buttonSound();
                    }
                }
            });
        }

        void updateBoardGroupPosition() {
            float height = getHeight() - getPadTop() - getPadBottom();
            float x = getWidth() - height - getPadRight();
            boardGroup.updateSize(x, getPadBottom(), height);
            if (!initialValidation && height > getPrefHeight() / 2) {
                initialValidation = true;
            }
        }

        void updateRecordLabelText() {
            String recordLabelText;
            if (level <= Levels.getHighestLevelIndex() && Levels.records[level] > 0) {
                recordLabelText = "" + Levels.records[level];
                recordNumLabel.setStyle(skin.get("highlightStyle", Label.LabelStyle.class));
            } else {
                recordLabelText = "-";
                recordNumLabel.setStyle(skin.get("deemphasisStyle", Label.LabelStyle.class));
            }
            recordNumLabel.setText(recordLabelText);
        }

        @Override
        public float getPrefWidth() {
            return itemPrefWidth;
        }

        @Override
        public float getMaxHeight() {
            return Dimensions.LEVEL_SELECT_ITEM_HEIGHT;
        }

        @Override
        public float getPrefHeight() {
            return Dimensions.LEVEL_SELECT_ITEM_HEIGHT;
        }
    }
}
