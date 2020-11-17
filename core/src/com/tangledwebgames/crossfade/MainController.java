package com.tangledwebgames.crossfade;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.TimeUtils;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.SignInListener;
import com.tangledwebgames.crossfade.data.LevelLoader;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.game.GameController;
import com.tangledwebgames.crossfade.game.GameState;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.game.SavedGameState;
import com.tangledwebgames.crossfade.game.WinListener;
import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.PauseState;
import com.tangledwebgames.crossfade.ui.UiController;

public class MainController extends ScreenAdapter implements WinListener, AuthChangeListener {

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
        CrossFadeGame.game.authManager.addChangeListener(this);
        loadSavedGameState();
    }

    void loadSavedGameState() {
        SavedGameState savedGame = CrossFadeGame.game.dataManager.loadSavedGameState();
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
        saveGameState();
        SoundManager.stopMusic();
    }

    @Override
    public void dispose() {
        CrossFadeGame.game.authManager.removeChangeListener(this);
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
        boolean isRecord = false;
        boolean isFirstTime = false;
        if (level <= Levels.getHighestLevelIndex() &&
                (Levels.records[level] == 0 ||
                        moves < Levels.records[level])) {
            isRecord = true;
            if (Levels.records[level] == 0) {
                isFirstTime = true;
            }
            Levels.records[level] = moves;
            uiController.newRecordWinText();
        }

        CrossFadeGame.game.analytics.levelComplete(
                level,
                gameController.getTime(),
                moves,
                isRecord,
                isFirstTime
        );
        SoundManager.winSound();
        showWinMenu();
    }

    void goToLevel(int level) {
        if (!SettingsManager.isFullVersion() &&
                level > Levels.MAX_FREE_LEVEL) {
            showPurchaseDialog(true);
            CrossFadeGame.game.analytics.hitMaxFreeLevel();
            return;
        } else if (level != gameController.getLevel()) {
            gameController.goToLevel(level);
            uiController.newLevel();
            CrossFadeGame.game.analytics.levelStart(level);
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
        CrossFadeGame.game.analytics.levelStart(gameController.getLevel());
        unpauseGame();
    }

    void loadGameState(GameState gameState) {
        gameController.setToGameState(gameState);
        uiController.newLevel();
        unpauseGame();
    }

    void saveGameState() {
        SettingsManager.flush();
        CrossFadeGame.game.dataManager.saveRecords();
        CrossFadeGame.game.dataManager.saveGameState(gameController.getSavedGameState());
    }

    GameState getGameState() {
        return gameController;
    }

    void signOut() {
        CrossFadeGame.game.authManager.signOut();
        unpauseGame();
    }

    void signIn() {
        CrossFadeGame.game.authManager.silentSignIn(new SignInListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(SignInError error) {
                if (error == SignInError.SILENT_SIGN_IN_FAILURE) {
                    CrossFadeGame.game.authManager.signIn(this);
                }
            }
        });
    }

    @Override
    public void onSignIn() {
        uiController.resetTablesOnAuthChange();
        CrossFadeGame.game.analytics.login();
    }

    @Override
    public void onSignOut() {
        uiController.resetTablesOnAuthChange();
        CrossFadeGame.game.analytics.signOut();
    }

    @Override
    public void onAnonymousSignIn() {
        uiController.resetTablesOnAuthChange();
    }
}
