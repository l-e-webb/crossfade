package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tangledwebgames.crossfade.Assets;
import com.tangledwebgames.crossfade.MainScreen;

/**
 * Manages all UI elements through a Stage.
 */
public class UIRenderer extends Stage {

    private MainScreen screen;
    private MainUITable mainUiTable;
    private PauseUITable pauseUiTable;
    private WinUiTable winUiTable;
    private LevelSelectTable levelSelectTable;

    private int time;
    private int moves;

    public UIRenderer(MainScreen screen) {
        super(screen.getViewport());
        this.screen = screen;
        time = -1;
        moves = -1;
        initUI();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        //Update time and move UI elements
        int newTime = (int) screen.getTime();
        int newMoves = screen.getBoard().getMoves();
        if (time < newTime || moves < newMoves) {
            time = newTime;
            moves = newMoves;
            mainUiTable.setTimeAndMoves(time, moves);
            winUiTable.setWinTimeAndMoves(time, moves);
        }
        //Update table visibility
        setVisibleTables();
    }

    private void setVisibleTables() {
        switch (screen.getState()) {
            case WIN:
                winUiTable.setVisible(true);
                pauseUiTable.setVisible(false);
                levelSelectTable.setVisible(false);
                break;
            case PAUSE:
                winUiTable.setVisible(false);
                pauseUiTable.setVisible(true);
                levelSelectTable.setVisible(false);
                break;
            case PLAY: default:
                winUiTable.setVisible(false);
                pauseUiTable.setVisible(false);
                levelSelectTable.setVisible(false);
                break;
            case LEVEL_SELECT:
                winUiTable.setVisible(false);
                pauseUiTable.setVisible(false);
                levelSelectTable.setVisible(true);
                break;
        }
    }

    public void initPause() {
        pauseUiTable.initPause();
        mainUiTable.disableButtons();
    }

    public void initLevelSelect() {
        levelSelectTable.updateAll();
        mainUiTable.disableButtons();
    }

    public void initUnpause() {
        mainUiTable.enableButtons();
    }

    public void initUI() {

        //Font inits.
        BitmapFont titleFont = Assets.instance.titleFont;
        titleFont.getData().setScale(Dimensions.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont uiFont = Assets.instance.uiFont;
        uiFont.getData().setScale(Dimensions.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Style inits.
        Label.LabelStyle labelStyle = new Label.LabelStyle(uiFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle highlightStyle = new Label.LabelStyle(uiFont, Dimensions.ACTIVE_BUTTON_COLOR);
        Label.LabelStyle deemphasisStyle = new Label.LabelStyle(uiFont, Dimensions.DARK_COLOR);
        NinePatchDrawable tile9Patch = new NinePatchDrawable(
                new NinePatch(Assets.instance.tileSmall, 13, 13 , 13, 13));
        NinePatchDrawable box9Patch = new NinePatchDrawable(
                new NinePatch(Assets.instance.greyBox, 3, 3, 3, 3));
        TextureRegionDrawable whiteBox = new TextureRegionDrawable(Assets.instance.whiteBox);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tile9Patch.tint(Dimensions.BUTTON_COLOR);
        buttonStyle.down = tile9Patch.tint(Dimensions.ACTIVE_BUTTON_COLOR);
        buttonStyle.fontColor = Dimensions.DARK_TEXT_COLOR;
        buttonStyle.downFontColor = Dimensions.DARK_TEXT_COLOR;
        buttonStyle.font = uiFont;
        TextureRegionDrawable checkbox = new TextureRegionDrawable(
                Assets.instance.tileSmall
        );
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(
                checkbox.tint(Dimensions.OFF_CHECKBOX_COLOR),
                checkbox.tint(Dimensions.PRIMARY_COLOR),
                uiFont,
                Dimensions.PRIMARY_COLOR
        );
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(
                Assets.instance.sliderBackground
        );
        Sprite sliderKnobSprite = new Sprite(Assets.instance.sliderKnob);
        sliderKnobSprite.setSize(Dimensions.SLIDER_KNOB_WIDTH, Dimensions.SLIDER_KNOB_HEIGHT);
        SpriteDrawable sliderKnob = new SpriteDrawable(sliderKnobSprite);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(
                sliderBackground.tint(Dimensions.SLIDER_COLOR),
                sliderKnob.tint(Dimensions.PRIMARY_COLOR)
        );
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = box9Patch.tint(Dimensions.SLIDER_COLOR);



        //Skin init.
        Skin skin = new Skin();
        skin.add("titleStyle", titleStyle);
        skin.add("highlightStyle", highlightStyle);
        skin.add("deemphasisStyle", deemphasisStyle);
        skin.add("default", labelStyle);
        skin.add("default", buttonStyle);
        skin.add("default", checkBoxStyle);
        skin.add("default-horizontal", sliderStyle);
        skin.add("default", scrollPaneStyle);
        //Main UI
        mainUiTable = new MainUITable(skin, whiteBox.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Pause Screen
        pauseUiTable = new PauseUITable(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Win Screen
        winUiTable = new WinUiTable(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Level select
        levelSelectTable = new LevelSelectTable(skin,
                tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR),
                box9Patch.tint(Dimensions.UI_BACKGROUND_COLOR_DOUBLED),
                box9Patch.tint(Dimensions.SLIDER_COLOR),
                box9Patch.tint(Dimensions.ACTIVE_BUTTON_COLOR)
        );

        addActor(mainUiTable);
        addActor(pauseUiTable);
        addActor(winUiTable);
        addActor(levelSelectTable);
    }

    public void newLevel() {
        moves = -1;
        time = -1;
        int level = screen.getLevel();
        mainUiTable.updateForNewLevel(level);
        winUiTable.updateForNewLevel(level);
    }

    public void newRecordWinText() {
        winUiTable.newRecordWinText();
    }

    public void updateTablePositions() {
        float x = screen.getViewport().getWorldWidth() / 2;
        float y = screen.getViewport().getWorldHeight() / 2;
        pauseUiTable.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, pauseUiTable.getPrefHeight());
        pauseUiTable.setPosition(x, y, Align.center);
        winUiTable.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, winUiTable.getPrefHeight());
        winUiTable.setPosition(x, y, Align.center);
        levelSelectTable.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, MainScreen.WORLD_HEIGHT * Dimensions.LEVEL_SELECT_HEIGHT_RATIO);
        levelSelectTable.setPosition(x, y, Align.center);
    }

}