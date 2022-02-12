package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tangledwebgames.crossfade.auth.SignInListener;
import com.tangledwebgames.crossfade.data.AssetLoader;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.ui.LoadingUiController;
import com.tangledwebgames.crossfade.ui.UiText;

public class LoadingScreen extends AbstractScreen implements SignInListener {

    private static final String LOG_TAG = LoadingScreen.class.getSimpleName();

    private boolean loaded;
    private LoadingUiController uiController;
    private boolean goToMainScreenNextFrame;

    @Override
    public void show() {
        Gdx.app.log(LOG_TAG, "Loading essential assets loading completed.");
        AssetLoader.instance.loadEssential(); // Waits until complete.
        Gdx.app.log(LOG_TAG, "Essential assets loaded.");
        UiText.init();
        Gdx.app.log(LOG_TAG, "Loading additional assets.");
        AssetLoader.instance.loadRemainder(); // Asynchronous.
        loaded = false;
        goToMainScreenNextFrame = false;
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        uiController = new LoadingUiController(viewport, this);
        uiController.showLoading();
        Gdx.input.setInputProcessor(uiController);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (AssetLoader.instance.isFinished() && !loaded) {
            Gdx.app.log(LOG_TAG, "Asset loading completed.");
            onLoadComplete();
        } else {
            AssetLoader.instance.update();
        }

        if (goToMainScreenNextFrame) {
            goToMainScreen();
            return;
        }

        uiController.act(delta);
        uiController.draw();
    }

    private void onLoadComplete() {
        AssetLoader.instance.onLoadComplete();
        SettingsManager.init();
        Levels.init();
        uiController.initFull();
        loaded = true;
        if (getGame().authManager.isAuthAvailable()) {
            Gdx.app.log(LOG_TAG, "Beginning auth flow.");
            getGame().authManager.silentSignIn(this);
        } else {
            Gdx.app.log(LOG_TAG, "Skipping auth flow.");
            getGame().recordManager.refreshRecords();
            goToMainScreen();
        }
    }

    private void goToMainScreen() {
        getGame().setScreen(new MainScreen());
    }

    public void onLoginButtonClicked() {
        getGame().authManager.signIn(this);
    }

    public void onNoLoginButtonClicked() {
        goToMainScreen();
    }

    @Override
    public void onSuccess() {
        getGame().analytics.login();
        uiController.addAction(Actions.run(
                () -> { goToMainScreen(); }
        ));
    }

    @Override
    public void onError(SignInError error) {
        switch (error) {
            case CANCEL:
            case SILENT_SIGN_IN_FAILURE:
                uiController.showLoginPrompt();
                break;
            case NETWORK_ERROR:
                uiController.showErrorDialog(true);
                break;
            case UNKNOWN:
                uiController.showErrorDialog(false);
                break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        uiController.dispose();
    }
}
