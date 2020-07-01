package com.tangledwebgames.crossfade.client;

import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

class HtmlAuthManager implements AuthManager {

    @Override
    public boolean isAuthAvailable() {
        return false;
    }

    @Override
    public void silentSignIn(SignInListener signInListener) { }

    @Override
    public void signIn(SignInListener signInListener) { }

    @Override
    public void signOut() { }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void addChangeListener(AuthChangeListener listener) { }

    @Override
    public void removeChangeListener(AuthChangeListener listener) { }
}
