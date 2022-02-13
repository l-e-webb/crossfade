package com.tangledwebgames.crossfade.auth;

import com.badlogic.gdx.Gdx;

/**
 * Trivial AuthManager to be used in situations when authentication is not used.
 */
public class AuthManagerEmpty extends AuthManager {

    private static final String LOG_TAG = AuthManagerEmpty.class.getSimpleName();

    private final String userId;

    public AuthManagerEmpty(String userId) {
        this.userId = userId;
    }

    public AuthManagerEmpty() {
        this(ANONYMOUS_USER_ID);
    }

    @Override
    public void silentSignIn(SignInListener signInListener) {
        Gdx.app.error(LOG_TAG, "Attempting silent sign in when authentication is not available.");
        signInListener.onError(SignInListener.SignInError.UNKNOWN);
    }

    @Override
    public void signIn(SignInListener signInListener) {
        Gdx.app.error(LOG_TAG, "Attempting sign in when authentication is not available.");
        signInListener.onError(SignInListener.SignInError.UNKNOWN);
    }

    @Override
    public void signOut() {
        Gdx.app.error(LOG_TAG, "Attempting sign out when authentication is not available.");
        // Listeners still notified to ensure records refresh.
        notifySignOut();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public boolean isAuthAvailable() {
        return false;
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
