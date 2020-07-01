package com.tangledwebgames.crossfade.auth;

public interface AuthChangeListener {

    void onSignIn();
    void onSignOut();
    void onAnonymousSignIn();

}
