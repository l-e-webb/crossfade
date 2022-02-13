package com.tangledwebgames.crossfade;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tangledwebgames.crossfade.android.R;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

import java.util.Arrays;
import java.util.List;

class AndroidAuthManager extends AuthManager implements FirebaseAuth.AuthStateListener {

    private static final String LOG_TAG = AndroidAuthManager.class.getSimpleName();

    // C R O S S = 3 18 15 19 19
    static final int RC_SIGN_IN = 318151919;
    private static final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    private final Activity activity;

    private SignInListener listener;

    AndroidAuthManager(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void silentSignIn(SignInListener listener) {
        this.listener = listener;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Gdx.app.log(LOG_TAG, "User already logged in.");
            if (user.isAnonymous()) {
                notifyAnonymousSignIn();
            } else {
                notifySignIn();
            }
            listener.onSuccess();
            return;
        }

        Gdx.app.log(LOG_TAG, "Attempting silent sign-in.");
        AuthUI.getInstance().silentSignIn(activity, providers)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Gdx.app.log(LOG_TAG, "Silent sign-in successful.");
                        listener.onSuccess();
                    } else {
                        Gdx.app.log(LOG_TAG, "Silent sign-in failed.");
                        listener.onError(SignInListener.SignInError.SILENT_SIGN_IN_FAILURE);
                    }
                });
    }

    @Override
    public void signIn(SignInListener listener) {
        this.listener = listener;
        Gdx.app.log(LOG_TAG, "Attempting firebase UI sign-in.");
        launchSignInActivity();
    }

    private void launchSignInActivity() {
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .enableAnonymousUsersAutoUpgrade()
                .setIsSmartLockEnabled(true, false)
                .setTheme(R.style.GdxTheme)
                .build();
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RC_SIGN_IN) return;

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == Activity.RESULT_OK) {
            Gdx.app.log(LOG_TAG, "Sign in activity completed with success.");
            listener.onSuccess();
        } else {
            if (response == null) {
                Gdx.app.log(LOG_TAG, "Sign in activity completed with no response.");
                listener.onError(SignInListener.SignInError.CANCEL);
                return;
            }
            int errorCode = ErrorCodes.UNKNOWN_ERROR;
            try {
                errorCode = response.getError().getErrorCode();
            } catch (Exception ignored) {}
            if (errorCode == ErrorCodes.NO_NETWORK) {
                Gdx.app.log(LOG_TAG, "Sign in activity completed connectivity error.");
                listener.onError(SignInListener.SignInError.NETWORK_ERROR);
            } else {
                Gdx.app.log(LOG_TAG, "Sign in activity completed with unknown error.");
                listener.onError(SignInListener.SignInError.UNKNOWN);
            }
        }
    }

    @Override
    public void signOut() {
        Gdx.app.log(LOG_TAG, "Beginning sign-out.");
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
        if (user != null) {
            if (user.isAnonymous()) {
                notifyAnonymousSignIn();
            } else {
                notifySignIn();
            }
        } else {
            notifySignOut();
        }
    }

    @Override
    public boolean isAuthAvailable() {
        return true;
    }

    @Override
    public String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null && !user.isAnonymous()) ? user.getUid() : ANONYMOUS_USER_ID;
    }
}
