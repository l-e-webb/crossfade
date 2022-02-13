package com.tangledwebgames.crossfade.desktop;

import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

/**
 * Empty methods; desktop version does not implement authentication.
 */
class DesktopAuthManager extends AuthManager {

    @Override
    public void silentSignIn(SignInListener signInListener) { }

    @Override
    public void signIn(SignInListener signInListener) { }

    @Override
    public void signOut() { }

    @Override
    public boolean isAuthAvailable() {
        return false;
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public String getUserId() {
        return DESKTOP_USER_ID;
    }
}
