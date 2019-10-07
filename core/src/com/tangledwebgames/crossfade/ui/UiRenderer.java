package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.tangledwebgames.crossfade.Assets;
import com.tangledwebgames.crossfade.MainScreen;

/**
 * Manages all UI elements through a Stage.
 */
public class UiRenderer extends Stage {

    private MainScreen screen;
    private MainUiTable mainUiTable;
    private MenuTable menuTable;
    private WinTable winTable;
    private LevelSelectTable levelSelectTable;
    private PurchaseTable purchaseTable;
    private CrossFadeDialog dialog;

    private int time;
    private int moves;

    PauseState pauseState;

    public UiRenderer(MainScreen screen) {
        super(screen.getViewport());
        this.screen = screen;
        time = -1;
        moves = -1;
        pauseState = PauseState.NOT_PAUSED;
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
            winTable.setWinTimeAndMoves(time, moves);
        }
    }

    public PauseState getPauseState() {
        return pauseState;
    }

    private void setVisibleTables() {
        winTable.setVisible(false);
        menuTable.setVisible(false);
        levelSelectTable.setVisible(false);
        purchaseTable.setVisible(false);
        dialog.setVisible(false);
        switch (getPauseState()) {
            case NOT_PAUSED: default:
                break;
            case WIN:
                winTable.setVisible(true);
                break;
            case MENU:
                menuTable.setVisible(true);
                break;
            case LEVEL_SELECT:
                levelSelectTable.setVisible(true);
                break;
            case PURCHASE:
                purchaseTable.setVisible(true);
                break;
            case PURCHASE_FAILED:
            case PURCHASE_SUCCESS:
            case PURCHASE_NO_RESTORE:
                dialog.setVisible(true);
                break;
        }
    }

    public void initPause(PauseState state) {
        pauseState = state;
        switch (state) {
            case MENU:
                showMenu();
                break;
            case LEVEL_SELECT:
                showLevelSelect();
                break;
            case PURCHASE:
                showPurchaseDialog(false);
            case NOT_PAUSED:
                initUnpause();
                break;
            case PURCHASE_SUCCESS:
                showPurchaseSuccess();
                break;
            case PURCHASE_FAILED:
                showPurchaseFailed();
                break;
            case PURCHASE_NO_RESTORE:
                showNoRestore();
                break;
            case WIN: default:
                break;
        }
        setVisibleTables();
    }

    private void showMenu() {
        menuTable.initPause();
        mainUiTable.disableButtons();
    }

    private void showLevelSelect() {
        levelSelectTable.updateAll();
        mainUiTable.disableButtons();
    }

    private void initUnpause() {
        mainUiTable.enableButtons();
    }

    public void showPurchaseDialog(boolean fromAttemptToAccessUnavailableContent) {
        pauseState = PauseState.PURCHASE;
        purchaseTable.update(fromAttemptToAccessUnavailableContent);
        purchaseTable.enableAll();
        updateTablePositions();
        setVisibleTables();
    }

    public void onPurchase() {
        resetTablesOnPurchase();
        initPause(PauseState.PURCHASE_SUCCESS);
    }

    void showPurchaseSuccess() {
        dialog.setLabelText(UiText.FULL_VERSION_UNLOCKED);
    }

    void showPurchaseFailed() {
        dialog.setLabelText(UiText.PURCHASE_ERROR);
    }

    void showNoRestore() {
        dialog.setLabelText(UiText.NO_RESTORE);
    }

    public void initUI() {

        //Font inits.
        BitmapFont titleFont = Assets.instance.titleFont;
        titleFont.getData().setScale(Dimensions.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont uiFont = Assets.instance.uiFont;
        uiFont.getData().setScale(Dimensions.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont smallFont = Assets.instance.smallFont;
        smallFont.getData().setScale(Dimensions.SMALL_TEXT_SCALE);
        smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Style inits.
        Label.LabelStyle labelStyle = new Label.LabelStyle(uiFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle smallStyle = new Label.LabelStyle(smallFont, Dimensions.PRIMARY_COLOR);
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
        skin.add("smallStyle", smallStyle);
        skin.add("highlightStyle", highlightStyle);
        skin.add("deemphasisStyle", deemphasisStyle);
        skin.add("default", labelStyle);
        skin.add("default", buttonStyle);
        skin.add("default", checkBoxStyle);
        skin.add("default-horizontal", sliderStyle);
        skin.add("default", scrollPaneStyle);

        //Main UI
        mainUiTable = new MainUiTable(skin, whiteBox.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Pause Screen
        menuTable = new MenuTable(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Win Screen
        winTable = new WinTable(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Level select
        levelSelectTable = new LevelSelectTable(skin,
                tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR),
                box9Patch.tint(Dimensions.UI_BACKGROUND_COLOR_DOUBLED),
                box9Patch.tint(Dimensions.SLIDER_COLOR),
                box9Patch.tint(Dimensions.ACTIVE_BUTTON_COLOR)
        );

        //Purchase table
        purchaseTable = new PurchaseTable(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Dialog
        dialog = new CrossFadeDialog(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));
        dialog.setButtonText(UiText.OK);
        dialog.setButtonListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.unpauseGame();
            }
        });

        addActor(mainUiTable);
        addActor(menuTable);
        addActor(winTable);
        addActor(levelSelectTable);
        addActor(purchaseTable);
        addActor(dialog);

        updateTablePositions();
        setVisibleTables();
    }

    public void newLevel() {
        moves = -1;
        time = -1;
        int level = screen.getLevel();
        mainUiTable.updateForNewLevel(level);
        winTable.updateForNewLevel(level);
    }

    public void newRecordWinText() {
        winTable.newRecordWinText();
    }

    public void updateTablePositions() {
        float x = screen.getViewport().getWorldWidth() / 2;
        float y = screen.getViewport().getWorldHeight() / 2;
        menuTable.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, menuTable.getPrefHeight());
        menuTable.setPosition(x, y, Align.center);
        winTable.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, winTable.getPrefHeight());
        winTable.setPosition(x, y, Align.center);
        levelSelectTable.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, MainScreen.WORLD_HEIGHT * Dimensions.LEVEL_SELECT_HEIGHT_RATIO);
        levelSelectTable.setPosition(x, y, Align.center);
        purchaseTable.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, MainScreen.WORLD_HEIGHT * Dimensions.PURCHASE_DIALOG_HEIGHT_RATIO);
        purchaseTable.setPosition(x, y, Align.center);
        dialog.setSize(MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO, MainScreen.WORLD_HEIGHT * Dimensions.GENERIC_DIALOG_HEIGHT_RATIO);
        dialog.setPosition(x, y, Align.center);
    }

    void resetTablesOnPurchase() {
        menuTable.initContents();
        levelSelectTable.createLevelContents();
    }

}