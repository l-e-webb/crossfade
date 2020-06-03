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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.data.AssetLoader;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.game.GameState;

/**
 * Manages all UI elements through a Stage.
 */
public class UiController extends Stage {

    private MainUiTable mainUiTable;
    private PauseMenu pauseMenu;
    private WinDialog winDialog;
    private LevelSelectMenu levelSelectMenu;
    private PurchaseMenu purchaseMenu;
    private CrossFadeDialog dialog;

    private GameState gameState;
    private PauseState pauseState;
    private UiReceiver receiver;

    private int time;
    private int moves;

    public UiController(Viewport viewport) {
        super(viewport);

    }

    public void init(GameState gameState, UiReceiver receiver) {
        this.gameState = gameState;
        this.receiver = receiver;
        pauseState = PauseState.NOT_PAUSED;
        time = 0;
        moves = 0;
        initUI();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        //Update time and move UI elements
        int newTime = gameState.getTime();
        int newMoves = gameState.getMoves();
        if (time < newTime || moves < newMoves) {
            time = newTime;
            moves = newMoves;
            mainUiTable.setTimeAndMoves(time, moves);
            winDialog.setWinTimeAndMoves(time, moves);
        }
    }

    private void setVisibleTables() {
        winDialog.setVisible(false);
        pauseMenu.setVisible(false);
        levelSelectMenu.setVisible(false);
        purchaseMenu.setVisible(false);
        dialog.setVisible(false);
        switch (pauseState) {
            case NOT_PAUSED:
            default:
                break;
            case WIN:
                winDialog.setVisible(true);
                break;
            case MENU:
                pauseMenu.setVisible(true);
                break;
            case LEVEL_SELECT:
                levelSelectMenu.setVisible(true);
                break;
            case PURCHASE:
                purchaseMenu.setVisible(true);
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
            case WIN:
            default:
                break;
        }
        setVisibleTables();
    }

    private void showMenu() {
        pauseMenu.initPause();
        mainUiTable.disableButtons();
    }

    private void showLevelSelect() {
        levelSelectMenu.updateAll();
        mainUiTable.disableButtons();
    }

    private void initUnpause() {
        mainUiTable.enableButtons();
    }

    public void showPurchaseDialog(boolean fromAttemptToAccessUnavailableContent) {
        pauseState = PauseState.PURCHASE;
        purchaseMenu.update(fromAttemptToAccessUnavailableContent);
        purchaseMenu.enableAll();
        updateTablePositions();
        setVisibleTables();
    }

    private void showPurchaseSuccess() {
        resetTablesOnPurchase();
        setDialog(
                UiText.FULL_VERSION_UNLOCKED,
                UiText.OK,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onPurchaseSuccessConfirm();
                    }
                }
        );
    }

    private void showPurchaseFailed() {
        setDialog(
                UiText.PURCHASE_ERROR,
                UiText.OK,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onPurchaseFailedConfirm();
                    }
                }
        );
    }

    private void showNoRestore() {
        setDialog(
                UiText.NO_RESTORE,
                UiText.OK,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onPurchaseNoRestoreConfirm();
                    }
                }
        );
    }

    private void initUI() {

        //Font inits.
        BitmapFont titleFont = AssetLoader.instance.titleFont;
        titleFont.getData().setScale(Dimensions.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );
        BitmapFont uiFont = AssetLoader.instance.uiFont;
        uiFont.getData().setScale(Dimensions.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );
        BitmapFont smallFont = AssetLoader.instance.smallFont;
        smallFont.getData().setScale(Dimensions.SMALL_TEXT_SCALE);
        smallFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );

        //Style inits.
        Label.LabelStyle labelStyle = new Label.LabelStyle(uiFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle smallStyle = new Label.LabelStyle(smallFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle highlightStyle = new Label.LabelStyle(
                uiFont,
                Dimensions.ACTIVE_BUTTON_COLOR
        );
        Label.LabelStyle deemphasisStyle = new Label.LabelStyle(uiFont, Dimensions.DARK_COLOR);
        NinePatchDrawable tile9Patch = new NinePatchDrawable(
                new NinePatch(AssetLoader.instance.tileSmall, 13, 13, 13, 13));
        NinePatchDrawable box9Patch = new NinePatchDrawable(
                new NinePatch(AssetLoader.instance.greyBox, 3, 3, 3, 3));
        TextureRegionDrawable whiteBox = new TextureRegionDrawable(AssetLoader.instance.whiteBox);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = tile9Patch.tint(Dimensions.BUTTON_COLOR);
        buttonStyle.down = tile9Patch.tint(Dimensions.ACTIVE_BUTTON_COLOR);
        buttonStyle.fontColor = Dimensions.DARK_TEXT_COLOR;
        buttonStyle.downFontColor = Dimensions.DARK_TEXT_COLOR;
        buttonStyle.font = uiFont;
        TextureRegionDrawable checkbox = new TextureRegionDrawable(
                AssetLoader.instance.tileSmall
        );
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(
                checkbox.tint(Dimensions.OFF_CHECKBOX_COLOR),
                checkbox.tint(Dimensions.PRIMARY_COLOR),
                uiFont,
                Dimensions.PRIMARY_COLOR
        );
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(
                AssetLoader.instance.sliderBackground
        );
        Sprite sliderKnobSprite = new Sprite(AssetLoader.instance.sliderKnob);
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
        mainUiTable = new MainUiTable(
                skin,
                receiver,
                whiteBox.tint(Dimensions.UI_BACKGROUND_COLOR)
        );

        //Pause Screen
        pauseMenu = new PauseMenu(skin, receiver, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Win Screen
        winDialog = new WinDialog(skin, receiver, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        //Level select
        levelSelectMenu = new LevelSelectMenu(
                skin,
                receiver,
                tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR),
                box9Patch.tint(Dimensions.UI_BACKGROUND_COLOR_DOUBLED),
                box9Patch.tint(Dimensions.SLIDER_COLOR),
                box9Patch.tint(Dimensions.ACTIVE_BUTTON_COLOR)
        );

        //Purchase table
        purchaseMenu = new PurchaseMenu(
                skin,
                receiver,
                tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR)
        );

        //Dialog
        dialog = new CrossFadeDialog(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));

        addActor(mainUiTable);
        addActor(pauseMenu);
        addActor(winDialog);
        addActor(levelSelectMenu);
        addActor(purchaseMenu);
        addActor(dialog);

        updateTablePositions();
        setVisibleTables();
    }

    private void setDialog(String labelText, String buttonText, ClickListener buttonListener) {
        dialog.setLabelText(labelText);
        dialog.setButtonText(buttonText);
        dialog.setButtonListener(buttonListener);
    }

    public void newLevel() {
        moves = -1;
        time = -1;
        int level = gameState.getLevel();
        mainUiTable.updateForNewLevel(level);
        winDialog.updateForNewLevel(level);
    }

    public void newRecordWinText() {
        winDialog.newRecordWinText();
    }

    public void updateTablePositions() {
        float x = getViewport().getWorldWidth() / 2;
        float y = getViewport().getWorldHeight() / 2;
        pauseMenu.setSize(
                MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO,
                pauseMenu.getPrefHeight()
        );
        pauseMenu.setPosition(x, y, Align.center);
        winDialog.setSize(
                MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO,
                winDialog.getPrefHeight()
        );
        winDialog.setPosition(x, y, Align.center);
        levelSelectMenu.setSize(
                MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO,
                MainScreen.WORLD_HEIGHT * Dimensions.LEVEL_SELECT_HEIGHT_RATIO
        );
        levelSelectMenu.setPosition(x, y, Align.center);
        purchaseMenu.setSize(
                MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO,
                MainScreen.WORLD_HEIGHT * Dimensions.PURCHASE_DIALOG_HEIGHT_RATIO
        );
        purchaseMenu.setPosition(x, y, Align.center);
        dialog.setSize(
                MainScreen.WORLD_WIDTH * Dimensions.PAUSE_TABLE_WIDTH_RATIO,
                MainScreen.WORLD_HEIGHT * Dimensions.GENERIC_DIALOG_HEIGHT_RATIO
        );
        dialog.setPosition(x, y, Align.center);
    }

    void resetTablesOnPurchase() {
        pauseMenu.initContents(receiver);
        levelSelectMenu.createLevelContents();
    }

}