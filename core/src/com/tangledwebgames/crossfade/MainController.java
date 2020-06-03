package com.tangledwebgames.crossfade;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.TimeUtils;
import com.tangledwebgames.crossfade.data.GameDataLoader;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.game.GameController;
import com.tangledwebgames.crossfade.game.GameState;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.game.SavedGameState;
import com.tangledwebgames.crossfade.game.WinListener;
import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.PauseState;
import com.tangledwebgames.crossfade.ui.UiController;

public class MainController extends ScreenAdapter implements WinListener {

    private static final long RESTORE_GAME_STATE_CUTOFF = 85000000L; // About 1 day

    static MainController instance;

    private boolean isPaused = true;
    private GameController gameController;
    private UiController uiController;

    static void init(GameController gameController, UiController uiController) {
        instance = new MainController(gameController, uiController);
    }

    private MainController(
            GameController gameController,
            UiController uiController
    ) {
        this.gameController = gameController;
        this.uiController = uiController;
        gameController.setWinListener(this);
        uiController.init(gameController, new UiEventHandler());
    }

    @Override
    public void show() {
        SavedGameState savedGame = GameDataLoader.loadSavedGameState();
        if (savedGame == null ||
                !SettingsManager.isFullVersion() && savedGame.getLevel() > Levels.MAX_FREE_LEVEL) {
            goToLevel(1);
        } else if (TimeUtils.timeSinceMillis(savedGame.getTimeStamp()) < RESTORE_GAME_STATE_CUTOFF
                        && savedGame.getMoves() != 0) {
            loadGameState(savedGame);
        } else {
            goToLevel(savedGame.getLevel());
        }
    }

    @Override
    public void resume() {
        SoundManager.playMusic();
    }

    @Override
    public void pause() {
        showMainMenu();
        com.tangledwebgames.crossfade.data.SettingsManager.flush();
        GameDataLoader.saveRecords();
        GameDataLoader.saveGameState(gameController.getSavedGameState());
        SoundManager.stopMusic();
    }

    boolean isPaused() {
        return isPaused;
    }

    boolean inGame() {
        return !isPaused();
    }

    void pauseGame() {
        if (isPaused()) return;
        isPaused = true;
        gameController.setActive(false);
    }

    void unpauseGame() {
        if (inGame()) return;
        isPaused = false;
        gameController.setActive(true);
        uiController.initPause(PauseState.NOT_PAUSED);
    }

    void togglePause() {
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
        if (!SettingsManager.isFullVersion() &&
                level > Levels.MAX_FREE_LEVEL) {
            showPurchaseDialog(true);
            return;
        } else if (level != gameController.getLevel()) {
            gameController.goToLevel(level);
            uiController.newLevel();
        }
        unpauseGame();
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

    void loadGameState(GameState gameState) {
        gameController.setToGameState(gameState);
        uiController.newLevel();
        unpauseGame();
    }

}
