package com.tangledwebgames.crossfade.analytics;

import com.badlogic.gdx.Gdx;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;

public class AuthChangeLogger implements AuthChangeListener {

    private static final String LOG_TAG = AuthChangeLogger.class.getSimpleName();

    @Override
    public void onSignIn() {
        CrossFadeGame.game.analytics.login();
        Gdx.app.log(LOG_TAG, "User logged in.");
    }

    @Override
    public void onSignOut() {
        CrossFadeGame.game.analytics.logOut();
        Gdx.app.log(LOG_TAG, "User logged out.");
    }

    @Override
    public void onAnonymousSignIn() {
        Gdx.app.log(LOG_TAG, "User signed in anonymously.");
    }
}
