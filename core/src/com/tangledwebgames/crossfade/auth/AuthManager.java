package com.tangledwebgames.crossfade.auth;

public interface AuthManager {

    String ANONYMOUS_USER_ID = "anonymous";
    String DESKTOP_USER_ID = "desktop";

    void silentSignIn(SignInListener signInListener);
    void signIn(SignInListener signInListener);
    void signOut();

    String getUserId();
    boolean isAuthAvailable();
    boolean isSignedIn();

    void addChangeListener(AuthChangeListener listener);
    void removeChangeListener(AuthChangeListener listener);

}
