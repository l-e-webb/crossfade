package com.tangledwebgames.crossfade;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

import java.util.Arrays;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements AuthManager {

	// C R O S S = 3 18 15 19 19
	private static final int RC_SIGN_IN = 318151919;

	private SignInListener listener;
	private List<AuthUI.IdpConfig> providers;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		providers = Arrays.asList(
				new AuthUI.IdpConfig.EmailBuilder().build(),
				new AuthUI.IdpConfig.PhoneBuilder().build(),
				new AuthUI.IdpConfig.GoogleBuilder().build()
		);
		CrossFadeGame game = new CrossFadeGame();
		game.purchaseManager = new PurchaseManagerGoogleBilling(this);
		game.authManager = this;
		initialize(game, config);
	}

	@Override
	public void silentSignIn(SignInListener listener) {
		this.listener = listener;
		if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			listener.onSuccess();
			return;
		}
		AuthUI.getInstance().silentSignIn(this, providers)
				.addOnCompleteListener(this, task -> {
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
		launchSignInActivity(providers);
	}

	private void launchSignInActivity(List<AuthUI.IdpConfig> providers) {
		Intent intent = AuthUI.getInstance()
				.createSignInIntentBuilder()
				.setAvailableProviders(providers)
				.enableAnonymousUsersAutoUpgrade()
				.setIsSmartLockEnabled(true, false)
				.build();
		startActivityForResult(intent, RC_SIGN_IN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != RC_SIGN_IN) return;

		IdpResponse response = IdpResponse.fromResultIntent(data);

		if (resultCode == RESULT_OK) {
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
		AuthUI.getInstance().signOut(this);
	}
}
