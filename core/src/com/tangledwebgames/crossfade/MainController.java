package com.tangledwebgames.crossfade;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.TimeUtils;
import com.tangledwebgames.crossfade.analytics.CrossFadeAnalytics;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.data.savedgame.SavedGameManager;
import com.tangledwebgames.crossfade.data.userdata.RecordChangeListener;
import com.tangledwebgames.crossfade.data.userdata.UserRecordManager;
import com.tangledwebgames.crossfade.game.GameController;
import com.tangledwebgames.crossfade.game.GameState;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.data.savedgame.SavedGameState;
import com.tangledwebgames.crossfade.game.WinListener;
import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.PauseState;
import com.tangledwebgames.crossfade.ui.UiController;

public class MainController extends ScreenAdapter
        implements WinListener, AuthChangeListener, RecordChangeListener {

    private static final long RESTORE_GAME_STATE_CUTOFF = 85000000L; // About 1 day

    static MainController instance;

    private boolean isPaused = true;
    private final GameController gameController;
    private final UiController uiController;

    private final CrossFadeAnalytics analytics;
    private final AuthManager authManager;
    private final UserRecordManager recordManager;

    static void init(GameController gameController, UiController uiController) {
        instance = new MainController(gameController, uiController);
    }

    private MainController(
            GameController gameController,
            UiController uiController
    ) {
        this.gameController = gameController;
        this.uiController = uiController;
        analytics = CrossFadeGame.game.analytics;
        authManager = CrossFadeGame.game.authManager;
        recordManager = CrossFadeGame.game.recordManager;
        authManager.addChangeListener(this);
        gameController.setWinListener(this);
        recordManager.addRecordChangeListener(this);
        uiController.init(gameController, new UiEventHandler());
    }

    @Override
    public void show() {
        loadSavedGameState();
    }

    void loadSavedGameState() {
        SavedGameManager.loadSavedGameState();
        SavedGameState savedGame = SavedGameManager.getSavedGameState();
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
        authManager.removeChangeListener(this);
        recordManager.removeRecordChangeListener(this);
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
        if (level <= Levels.getHighestLevelIndex()) {
            isRecord = recordManager.isRecord(level, moves);
            isFirstTime = !recordManager.hasBeatenLevel(level);
            if (isRecord) {
                uiController.newRecordWinText();
                recordManager.saveRecord(level, moves);
            }
        }

        analytics.levelComplete(
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
            analytics.hitMaxFreeLevel();
            return;
        } else if (level != gameController.getLevel()) {
            gameController.goToLevel(level);
            uiController.newLevel();
            analytics.levelStart(level);
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
        analytics.levelStart(gameController.getLevel());
        unpauseGame();
    }

    void loadGameState(GameState gameState) {
        gameController.setToGameState(gameState);
        uiController.newLevel();
        unpauseGame();
    }

    void saveGameState() {
        SettingsManager.flush();
        SavedGameManager.saveGameState(gameController.getSavedGameState());
    }

    GameState getGameState() {
        return gameController;
    }

    void signOut() {
        authManager.signOut();
        unpauseGame();
    }

    void signIn() {
        authManager.silentSignIn(new SignInListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(SignInError error) {
                if (error == SignInError.SILENT_SIGN_IN_FAILURE) {
                    authManager.signIn(this);
                }
            }
        });
    }

    @Override
    public void onSignIn() {
        uiController.resetTablesOnAuthChange();
        analytics.login();
    }

    @Override
    public void onSignOut() {
        uiController.resetTablesOnAuthChange();
        analytics.signOut();
    }

    @Override
    public void onAnonymousSignIn() {
        uiController.resetTablesOnAuthChange();
    }

    @Override
    public void onRecordChange() {
        uiController.resetRecordDisplay();
    }
}
