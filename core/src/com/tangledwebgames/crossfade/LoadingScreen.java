package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
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

import java.util.Collections;
import java.util.List;

public class LoadingScreen extends AbstractScreen {

    private static final String LOG_TAG = LoadingScreen.class.getSimpleName();
    private static final List<Application.ApplicationType> DATA_REPORTING_APP_TYPES = Collections
            .singletonList(Application.ApplicationType.Android);

    private boolean loginStarted;
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
        Gdx.app.log(LOG_TAG, "Loading essential assets .");
        AssetLoader.instance.loadEssential(); // Waits until complete.
        Gdx.app.log(LOG_TAG, "Essential assets loaded.");
        UiText.init();
        Gdx.app.log(LOG_TAG, "Loading additional assets.");
        AssetLoader.instance.loadRemainder(); // Asynchronous.
        loaded = false;
        loginStarted = false;
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, 0);
        uiController = new LoadingUiController(viewport, this);
        uiController.showLoading();
        Gdx.input.setInputProcessor(uiController);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!loaded &&
                AssetLoader.instance.isFinished() &&
                !CrossFadePurchaseManager.isInitializing()
        ) {
            onLoadComplete();
        } else {
            AssetLoader.instance.update();
        }

        if (shouldShowDataSharingDialog()) {
            uiController.showDataSharingDialog();
        } else if (shouldBeginLogin()) {
            beginLogIn();
        }

        uiController.act(delta);
        uiController.draw();
    }

    private boolean shouldBeginLogin() {
        return loaded &&
                getGame().configComplete &&
                !loginStarted &&
                (!DATA_REPORTING_APP_TYPES.contains(Gdx.app.getType()) ||
                        SettingsManager.isIsDataSharingDialogShown());
    }

    private boolean shouldShowDataSharingDialog() {
        return loaded &&
                DATA_REPORTING_APP_TYPES.contains(Gdx.app.getType()) &&
                getGame().configComplete &&
                !loginStarted &&
                !SettingsManager.isIsDataSharingDialogShown() &&
                !uiController.isDataSharingDialogVisible();
    }

    private void onLoadComplete() {
        Gdx.app.log(LOG_TAG, "Asset loading completed.");
        AssetLoader.instance.onLoadComplete();
        SettingsManager.init();
        Levels.init();
        uiController.initFull();
        loaded = true;
    }

    private void beginLogIn() {
        getGame().authManager.addChangeListener(authChangeListener);
        if (getGame().authManager.isAuthAvailable()) {
            Gdx.app.log(LOG_TAG, "Beginning auth flow.");
            getGame().authManager.silentSignIn(signInListener);
        } else {
            Gdx.app.log(LOG_TAG, "Skipping auth flow.");
            getGame().authManager.signInAnonymous();
        }
        loginStarted = true;
    }

    private void goToMainScreen() {
        if (!getGame().userManager.hasFullVersion()) {
            CrossFadePurchaseManager.restoreSilently();
        }
        uiController.addAction(Actions.run(
                () -> {
                    getGame().authManager.removeChangeListener(authChangeListener);
                    getGame().setScreen(new MainScreen());
                }
        ));
    }

    public void onLoginButtonClicked() {
        uiController.hideDialog();
        getGame().authManager.signIn(signInListener);
    }

    public void onNoLoginButtonClicked() {
        uiController.hideDialog();
        getGame().authManager.signInAnonymous();
    }

    public void onDataSharingDialogAgree() {
        Gdx.app.log(LOG_TAG, "Starting usage data sharing.");
        SettingsManager.setIsSharingUsageData(true);
        SettingsManager.setIsDataSharingDialogShown(true);
        SettingsManager.flush();
        uiController.hideDialog();
    }

    public void onDataShoringDialogDisagree() {
        Gdx.app.log(LOG_TAG, "No usage data sharing.");
        SettingsManager.setIsSharingUsageData(false);
        SettingsManager.setIsDataSharingDialogShown(true);
        SettingsManager.flush();
        uiController.hideDialog();
    }

    public void onPrivacyPolicyClick() {
        Gdx.net.openURI(CrossFadeGame.PRIVACY_POLICY_URL);
    }

    @Override
    public void dispose() {
        super.dispose();
        uiController.dispose();
    }
}
