package com.tangledwebgames.crossfade.client;

import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

class HtmlAuthManager extends AuthManager {

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
    public String getUserId() {
        return HTML_USER_ID;
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
