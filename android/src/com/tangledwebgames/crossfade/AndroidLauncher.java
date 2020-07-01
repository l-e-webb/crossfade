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
import com.google.firebase.auth.FirebaseUser;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.SignInListener;

import java.util.Arrays;
import java.util.List;

public class AndroidLauncher extends AndroidApplication {

	private AndroidAuthManager authManager;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		CrossFadeGame game = new CrossFadeGame();
		game.purchaseManager = new PurchaseManagerGoogleBilling(this);
		authManager = new AndroidAuthManager(this);
		game.authManager = authManager;
		FirebaseAuth.getInstance().addAuthStateListener(authManager);
		initialize(game, config);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		authManager.onActivityResult(requestCode, resultCode, data);
	}
}
