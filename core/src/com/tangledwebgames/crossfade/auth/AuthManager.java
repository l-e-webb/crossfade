package com.tangledwebgames.crossfade.auth;

public interface AuthManager {

    void signIn(SignInListener signInListener);
    void signOut();

}
