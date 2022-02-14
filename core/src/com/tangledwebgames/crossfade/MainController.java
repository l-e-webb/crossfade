package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;
import com.tangledwebgames.crossfade.analytics.CrossFadeAnalytics;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.data.savedgame.SavedGameManager;
import com.tangledwebgames.crossfade.data.savedgame.SavedGameState;
import com.tangledwebgames.crossfade.data.userdata.UserDataChangeListener;
import com.tangledwebgames.crossfade.data.userdata.UserManager;
import com.tangledwebgames.crossfade.game.GameController;
import com.tangledwebgames.crossfade.game.GameState;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.game.WinListener;
import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.PauseState;
import com.tangledwebgames.crossfade.ui.UiController;

public class MainController extends ScreenAdapter implements
        WinListener,
        AuthChangeListener,
        UserDataChangeListener,
        CrossFadePurchaseManager.PurchaseEventListener
{

    private static final long RESTORE_GAME_STATE_CUTOFF = 85000000L; // About 1 day

    static MainController instance;

    private boolean isPaused = true;
    private final GameController gameController;
    private final UiController uiController;

    private final CrossFadeAnalytics analytics;
    private final AuthManager authManager;
    private final UserManager userManager;

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
        userManager = CrossFadeGame.game.userManager;
        authManager.addChangeListener(this);
        gameController.setWinListener(this);
        userManager.addUserDataChangeListener(this);
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
                !userManager.hasFullVersion() && savedGame.getLevel() > Levels.MAX_FREE_LEVEL) {
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
        userManager.removeUserDataChangeListener(this);
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

    void pauseGame(PauseState pauseState) {
        pauseGame();
        uiController.initPause(pauseState);
    }

    void unpauseGame() {
        if (inGame()) return;
        isPaused = false;
        gameController.setActive(true);
        uiController.initPause(PauseState.NOT_PAUSED);
        if (getGameState().isWinningState()) {
            gameController.reset();
            uiController.newLevel();
        }
    }

    void togglePause() {
        if (isPaused()) {
            unpauseGame();
        } else {
            pauseGame();
        }
    }

    void showMainMenu() {
        pauseGame(PauseState.MENU);
    }

    void showWinMenu() {
        gameController.clearActiveTiles();
        pauseGame(PauseState.WIN);
    }

    void showLevelSelectMenu() {
        pauseGame(PauseState.LEVEL_SELECT);
    }

    void showPurchaseDialog(boolean fromAttemptToAccessUnavailableContent) {
        pauseGame();
        uiController.showPurchaseDialog(fromAttemptToAccessUnavailableContent);
    }

    void showLoginFailureDialog(boolean fromNetworkError) {
        pauseGame();
        uiController.showLoginFailedDialog(fromNetworkError);
    }

    public void onWin() {
        int level = gameController.getLevel();

        //Sandbox level cannot win
        if (level == Levels.getSandboxLevelIndex()) return;

        int moves = gameController.getMoves();
        boolean isRecord = false;
        boolean isFirstTime = false;
        if (level <= Levels.getHighestLevelIndex()) {
            isRecord = userManager.isRecord(level, moves);
            isFirstTime = !userManager.hasBeatenLevel(level);
            if (isRecord) {
                uiController.newRecordWinText();
                userManager.saveRecord(level, moves);
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
        if (isLockedLevel(level)) {
            hitMaxFreeLevel();
            return;
        } else if (level != gameController.getLevel()) {
            final int l = level;
            gameController.goToLevel(level);
            uiController.newLevel();

            // Only track level start events if the user stays on the level for at least one second.
            // This prevents spammed events if the user rapidly advances by clicking "next" or "previous."
            uiController.addAction(
                    Actions.sequence(
                            Actions.delay(1f),
                            Actions.run(() -> {
                                if (getGameState().getLevel() == l) {
                                    analytics.levelStart(l);
                                }
                            })
                    )
            );
        }
        unpauseGame();
    }

    void goToNextLevel() {
        goToLevel(gameController.getLevel() + 1);
    }

    void goToPreviousLevel() {
        goToLevel(gameController.getLevel() - 1);
    }

    private void hitMaxFreeLevel() {
        analytics.hitMaxFreeLevel();
        showPurchaseDialog(true);
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
        pauseGame(PauseState.LOG_OUT_SUCCESS);
    }

    void signIn() {
        authManager.silentSignIn(new SignInListener() {
            @Override
            public void onSuccess() {
                pauseGame(PauseState.LOG_IN_SUCCESS);
            }

            @Override
            public void onError(SignInError error) {
                switch (error) {
                    case SILENT_SIGN_IN_FAILURE:
                        authManager.signIn(this);
                        break;
                    case CANCEL:
                        unpauseGame();
                        break;
                    case UNKNOWN:
                        showLoginFailureDialog(false);
                        break;
                    case NETWORK_ERROR:
                        showLoginFailureDialog(true);
                        break;
                }
            }
        });
    }

    @Override
    public void onSignIn() {
        uiController.resetTablesOnAuthChange();
    }

    @Override
    public void onSignOut() {
        uiController.resetTablesOnAuthChange();
    }

    @Override
    public void onAnonymousSignIn() {
        uiController.resetTablesOnAuthChange();
    }

    @Override
    public void onRecordChange() {
        uiController.resetRecordDisplay();
    }

    @Override
    public void onFullVersionChange() {
        if (isLockedLevel(getGameState().getLevel())) {
            goToLevel(1);
        }
        uiController.resetTablesOnAuthChange();
    }

    @Override
    public void onFullVersionRestored() {
        pauseGame(PauseState.PURCHASE_SUCCESS);
    }

    @Override
    public void onRestoreError() {
        pauseGame(PauseState.PURCHASE_FAILED);
    }

    @Override
    public void onRestoreFailure() {
        pauseGame(PauseState.PURCHASE_NO_RESTORE);
    }

    @Override
    public void onRestoreUnavailable() {
        pauseGame(PauseState.PURCHASE_UNAVAILABLE);
    }

    @Override
    public void onSuccessfulPurchase() {
        pauseGame(PauseState.PURCHASE_SUCCESS);
    }

    @Override
    public void onPurchaseError() {
        pauseGame(PauseState.PURCHASE_FAILED);
    }

    @Override
    public void onPurchaseUnavailable() {
        pauseGame(PauseState.PURCHASE_UNAVAILABLE);
    }

    @Override
    public void onPurchaseCanceled() {
        unpauseGame();
    }

    private boolean isLockedLevel(int level) {
        if (CrossFadeGame.APP_TYPE == Application.ApplicationType.Android) {
            return !userManager.hasFullVersion() && level > Levels.MAX_FREE_LEVEL;
        }
        return false;
    }
}
