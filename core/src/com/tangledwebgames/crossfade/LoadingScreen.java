package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.SignInListener;
import com.tangledwebgames.crossfade.data.AssetLoader;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.ui.LoadingUiController;
import com.tangledwebgames.crossfade.ui.UiText;

public class LoadingScreen extends AbstractScreen {

    private static final String LOG_TAG = LoadingScreen.class.getSimpleName();

    private boolean loaded;
    private LoadingUiController uiController;

    private final AuthChangeListener authChangeListener = new AuthChangeListener() {
        @Override
        public void onSignIn() {
            goToMainScreen();
        }

        @Override
        public void onSignOut() {
            // Do nothing
        }

        @Override
        public void onAnonymousSignIn() {
            goToMainScreen();
        }
    };

    private final SignInListener signInListener = new SignInListener() {
        @Override
        public void onSuccess() {
            getGame().analytics.login();
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
    };

    @Override
    public void show() {
        Gdx.app.log(LOG_TAG, "Loading essential assets loading completed.");
        AssetLoader.instance.loadEssential(); // Waits until complete.
        Gdx.app.log(LOG_TAG, "Essential assets loaded.");
        UiText.init();
        Gdx.app.log(LOG_TAG, "Loading additional assets.");
        AssetLoader.instance.loadRemainder(); // Asynchronous.
        loaded = false;
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        uiController = new LoadingUiController(viewport, this);
        uiController.showLoading();
        Gdx.input.setInputProcessor(uiController);
        getGame().authManager.addChangeListener(authChangeListener);
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
            getGame().authManager.silentSignIn(signInListener);
        } else {
            Gdx.app.log(LOG_TAG, "Skipping auth flow.");
            getGame().authManager.signInAnonymous();
        }
    }

    private void goToMainScreen() {
        uiController.addAction(Actions.run(
                () -> {
                    getGame().authManager.removeChangeListener(authChangeListener);
                    getGame().setScreen(new MainScreen());
                }
        ));
    }

    public void onLoginButtonClicked() {
        getGame().authManager.signIn(signInListener);
    }

    public void onNoLoginButtonClicked() {
        getGame().authManager.signInAnonymous();
    }

    @Override
    public void dispose() {
        super.dispose();
        uiController.dispose();
    }
}
