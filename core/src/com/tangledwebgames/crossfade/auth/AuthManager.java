package com.tangledwebgames.crossfade.auth;

import java.util.HashSet;
import java.util.Set;

public abstract class AuthManager {

    public static String ANONYMOUS_USER_ID = "anonymous";
    public static String DESKTOP_USER_ID = "desktop";
    public static String HTML_USER_ID = "html";

    private final Set<AuthChangeListener> listeners = new HashSet<>();

    public abstract void silentSignIn(SignInListener signInListener);
    public abstract void signIn(SignInListener signInListener);
    public abstract void signOut();

    public abstract String getUserId();
    public abstract boolean isAuthAvailable();
    public abstract boolean isSignedIn();


    public void signInAnonymous() {
        notifyAnonymousSignIn();
    }

    public void addChangeListener(AuthChangeListener listener) {
        listeners.add(listener);
    }
    public void removeChangeListener(AuthChangeListener listener) {
        listeners.remove(listener);
    }

    protected void notifySignIn() {
        for (AuthChangeListener listener : listeners) {
            listener.onSignIn();
        }
    }

    protected void notifySignOut() {
        for (AuthChangeListener listener : listeners) {
            listener.onSignOut();
        }
    }

    protected void notifyAnonymousSignIn() {
        for (AuthChangeListener listener : listeners) {
            listener.onAnonymousSignIn();
        }
    }
}
