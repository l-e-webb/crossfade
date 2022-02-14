package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.game.GameState;

/**
 * Manages all UI elements through a Stage.
 */
public class UiController extends UiStage {

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
        initStyle();
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
            case PURCHASE_UNAVAILABLE:
            case LOG_IN_FAILED:
            case LOG_IN_SUCCESS:
            case LOG_OUT_SUCCESS:
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
            case PURCHASE_UNAVAILABLE:
                showPurchaseUnavailable();
                break;
            case LOG_IN_FAILED:
                showLoginFailedDialog(false);
                break;
            case LOG_IN_SUCCESS:
                showLoginSuccess();
                break;
            case LOG_OUT_SUCCESS:
                showLogoutSuccess();
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
        resetTablesOnAuthChange();
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

    private void showPurchaseUnavailable() {
        setDialog(
                UiText.GENERIC_ERROR_HEADER,
                UiText.PURCHASE_UNAVAILABLE_ERROR_BODY,
                UiText.OK,
                "",
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onPurchaseUnavailableConfirm();
                    }
                },
                new ClickListener()
        );
    }

    private void showLoginSuccess() {
        setDialog(
                UiText.LOGIN_PROMPT_SUCCESS,
                UiText.OK,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onLoginSuccessConfirm();
                    }
                }
        );
    }

    public void showLoginFailedDialog(boolean networkError) {
        if (networkError) {
            showLoginFailed(UiText.LOGIN_PROMPT_NETWORK_ERROR_BODY);
        } else {
            showLoginFailed(UiText.LOGIN_PROMPT_UNKNOWN_ERROR_BODY);
        }
    }

    private void showLoginFailed(String errorBody) {
        setDialog(
                UiText.GENERIC_ERROR_HEADER,
                errorBody,
                UiText.LOGIN_PROMPT_TRY_AGAIN,
                UiText.LOGIN_PROMPT_CANCEL,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onLoginTryAgain();
                    }
                },
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onLoginCancel();
                    }
                }
        );
    }

    private void showLogoutSuccess() {
        setDialog(
                UiText.LOGOUT_SUCCESS,
                UiText.OK,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        receiver.onLogoutConfirm();
                    }
                }
        );
    }

    private void initUI() {
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
        dialog.setText(labelText, "", buttonText, "");
        dialog.setConfirmButtonListener(buttonListener);
    }

    private void setDialog(
            String headerText,
            String labelText,
            String confirmButtonText,
            String cancelButtonText,
            ClickListener confirmButtonListener,
            ClickListener cancelButtonListener
    ) {
        dialog.setText(headerText, labelText, confirmButtonText, cancelButtonText);
        dialog.setConfirmButtonListener(confirmButtonListener);
        dialog.setCancelButtonListener(cancelButtonListener);
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

    public void resetTablesOnAuthChange() {
        pauseMenu.initContents(receiver);
        levelSelectMenu.createLevelContents();
        initPause(pauseState);
    }

    public void resetRecordDisplay() {
        mainUiTable.resetRecordDisplay(gameState.getLevel());
        if (pauseState == PauseState.LEVEL_SELECT) {
            levelSelectMenu.updateAll();
        }
    }

}