package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tangledwebgames.crossfade.auth.SignInListener;
import com.tangledwebgames.crossfade.data.AssetLoader;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.ui.LoadingUiController;
import com.tangledwebgames.crossfade.ui.UiText;

public class LoadingScreen extends AbstractScreen implements SignInListener {

    private boolean loaded;
    private LoadingUiController uiController;

    @Override
    public void show() {
        AssetLoader.instance.loadEssential(); // Waits until complete.
        UiText.init();
        AssetLoader.instance.loadRemainder(); // Asynchronous.
        loaded = false;
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        uiController = new LoadingUiController(viewport, this);
        uiController.showLoading();
        Gdx.input.setInputProcessor(uiController);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (AssetLoader.instance.isFinished() && !loaded) {
            onLoadComplete();
        } else {
            AssetLoader.instance.update();
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
        if (getGame().authManager != null) {
            getGame().authManager.silentSignIn(this);
        } else {
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
        goToMainScreen();
    }

    @Override
    public void onError(SignInError error) {
        switch (error) {
            case CANCEL:
            case SILENT_SIGN_IN_FAILURE:
                uiController.showLoginPrompt();
                break;
            case NETWORK_ERROR:
                uiController.showLoginPrompt(); // TODO: Make specific
                break;
            case UNKNOWN:
                uiController.showLoginPrompt(); // TODO: Make specific
                break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        uiController.dispose();
    }
}
