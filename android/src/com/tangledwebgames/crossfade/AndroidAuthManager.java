package com.tangledwebgames.crossfade;

import android.app.Activity;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

class AndroidAuthManager implements AuthManager, FirebaseAuth.AuthStateListener {

    // C R O S S = 3 18 15 19 19
    static final int RC_SIGN_IN = 318151919;
    private static final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    private Activity activity;
    private SignInListener listener;
    private List<AuthChangeListener> changeListeners;

    AndroidAuthManager(Activity activity) {
        this.activity = activity;
        changeListeners = new ArrayList<>();
    }

    @Override
    public void silentSignIn(SignInListener listener) {
        this.listener = listener;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            listener.onSuccess();
            return;
        }
        AuthUI.getInstance().silentSignIn(activity, providers)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onError(SignInListener.SignInError.SILENT_SIGN_IN_FAILURE);
                    }
                });
    }

    @Override
    public void signIn(SignInListener listener) {
        this.listener = listener;
        launchSignInActivity();
    }

    private void launchSignInActivity() {
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .enableAnonymousUsersAutoUpgrade()
                .setIsSmartLockEnabled(true, false)
                .build();
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RC_SIGN_IN) return;

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == Activity.RESULT_OK) {
            listener.onSuccess();
        } else {
            if (response == null) {
                listener.onError(SignInListener.SignInError.CANCEL);
                return;
            }
            int errorCode = ErrorCodes.UNKNOWN_ERROR;
            try {
                errorCode = response.getError().getErrorCode();
            } catch (Exception ignored) {}
            if (errorCode == ErrorCodes.NO_NETWORK) {
                listener.onError(SignInListener.SignInError.NETWORK_ERROR);
            } else {
                listener.onError(SignInListener.SignInError.UNKNOWN);
            }
        }
    }

    @Override
    public void signOut() {
        AuthUI.getInstance().signOut(activity);
    }

    @Override
    public boolean isSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && !user.isAnonymous();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        for (AuthChangeListener listener : changeListeners) {
            if (user == null) {
                listener.onSignOut();
            } else if (user.isAnonymous()) {
                listener.onAnonymousSignIn();
            } else {
                listener.onSignIn();
            }
        }
    }

    @Override
    public void addChangeListener(AuthChangeListener listener) {
        if (!changeListeners.contains(listener)) changeListeners.add(listener);
    }

    @Override
    public void removeChangeListener(AuthChangeListener listener) {
        changeListeners.remove(listener);
    }

    @Override
    public boolean isAuthAvailable() {
        return true;
    }
}
