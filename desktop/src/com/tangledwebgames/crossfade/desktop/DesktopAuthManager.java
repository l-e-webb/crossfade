package com.tangledwebgames.crossfade.desktop;

import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

class DesktopAuthManager implements AuthManager {

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
    public void addChangeListener(AuthChangeListener listener) { }

    @Override
    public void removeChangeListener(AuthChangeListener listener) { }

    @Override
    public String getUserId() {
        return DESKTOP_USER_ID;
    }
}
