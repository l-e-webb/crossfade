package com.tangledwebgames.crossfade.auth;

public interface AuthManager {

    void silentSignIn(SignInListener signInListener);
    void signIn(SignInListener signInListener);
    void signOut();

}
