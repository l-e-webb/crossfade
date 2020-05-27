package com.tangledwebgames.crossfade;

import com.badlogic.gdx.ScreenAdapter;
import com.tangledwebgames.crossfade.game.GameController;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.game.WinListener;
import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.PauseState;
import com.tangledwebgames.crossfade.ui.UiController;

public class MainController extends ScreenAdapter implements WinListener {

    static MainController instance;

    private boolean isPaused = true;

    protected GameController gameController;
    protected UiController uiController;

    public void init(
            GameController gameController,
            UiController uiController
    ) {
        MainController.instance = this;
        this.gameController = gameController;
        this.uiController = uiController;
        gameController.setWinListener(this);
        uiController.init(gameController, new UiEventHandler());
        goToLevel(1);
        unpauseGame();
    }

    boolean isPaused() {
        return isPaused;
    }

    boolean inGame() {
        return !isPaused();
    }

    protected void pauseGame() {
        if (isPaused()) return;
        isPaused = true;
        gameController.setActive(false);
    }

    protected void unpauseGame() {
        if (inGame()) return;
        isPaused = false;
        gameController.setActive(true);
        uiController.initPause(PauseState.NOT_PAUSED);
    }

    protected void togglePause() {
        if (isPaused()) {
            unpauseGame();
        } else {
            pauseGame();
        }
    }

    void showMainMenu() {
        pauseGame();
        uiController.initPause(PauseState.MENU);
    }

    void showWinMenu() {
        pauseGame();
        gameController.clearActiveTiles();
        uiController.initPause(PauseState.WIN);
    }

    void showLevelSelectMenu() {
        pauseGame();
        uiController.initPause(PauseState.LEVEL_SELECT);
    }

    void showPurchaseDialog(boolean fromAttemptToAccessUnavailableContent) {
        pauseGame();
        uiController.showPurchaseDialog(fromAttemptToAccessUnavailableContent);
    }

    void showPurchaseFailedDialog() {
        pauseGame();
        uiController.initPause(PauseState.PURCHASE_FAILED);
    }

    void showPurchaseNoRestoreDialog() {
        pauseGame();
        uiController.initPause(PauseState.PURCHASE_NO_RESTORE);
    }

    void showPurchaseSuccessDialog() {
        pauseGame();
        uiController.initPause(PauseState.PURCHASE_SUCCESS);
    }

    public void onWin() {
        int level = gameController.getLevel();

        //Sandbox level cannot win
        if (level == Levels.getSandboxLevelIndex()) return;

        int moves = gameController.getMoves();
        if (level <= Levels.getHighestLevelIndex() &&
                (Levels.records[level] == 0 ||
                        moves < Levels.records[level])) {
            Levels.records[level] = moves;
            uiController.newRecordWinText();
        }
        SoundManager.winSound();
        showWinMenu();
    }

    void goToLevel(int level) {
        if (gameController.getLevel() == level) {
            unpauseGame();
        } else if (!SettingsManager.isFullVersion() &&
                level > Levels.MAX_FREE_LEVEL) {
            showPurchaseDialog(true);
        } else {
            gameController.goToLevel(level);
            uiController.newLevel();
            unpauseGame();
        }
    }

    void goToNextLevel() {
        goToLevel(gameController.getLevel() + 1);
    }

    void goToPreviousLevel() {
        goToLevel(gameController.getLevel() - 1);
    }

    void resetLevel() {
        gameController.reset();
        uiController.newLevel();
        unpauseGame();
    }

    @Override
    public void pause() {
        super.pause();
        showMainMenu();
        SettingsManager.flush();
        Levels.saveRecords();
        SoundManager.stopMusic();
    }

    @Override
    public void resume() {
        SoundManager.playMusic();
    }

    @Override
    public void hide() {
        SettingsManager.flush();
        SoundManager.stopMusic();
    }

    @Override
    public void dispose() {
        uiController.dispose();
        gameController.dispose();
        Assets.instance.dispose();
    }


}
