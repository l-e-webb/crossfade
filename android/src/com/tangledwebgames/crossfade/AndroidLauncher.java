package com.tangledwebgames.crossfade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.tangledwebgames.crossfade.android.BuildConfig;
import com.tangledwebgames.crossfade.data.AndroidUserManager;

public class AndroidLauncher extends AndroidApplication {

	private static final String LOG_TAG = AndroidLauncher.class.getSimpleName();
	private static final String FIREBASE_AUTH_ENABLED_KEY = "FIREBASE_AUTH_ENABLED";

	private AndroidFirebaseAuthManager authManager = null;
	private CrossFadeGame game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game = new CrossFadeGame();
		game.purchaseManager = new PurchaseManagerGoogleBilling(this);
		game.analytics = new AndroidAnalytics(FirebaseAnalytics.getInstance(this));
		game.debug = BuildConfig.DEBUG;
		initialize(game, config);
		initializeFirebaseRemoteConfig();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		authManager.onActivityResult(requestCode, resultCode, data);
	}

	private void initializeFirebaseRemoteConfig() {
		Log.i(LOG_TAG, "Initializing firebase remote config.");
		FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
				.setMinimumFetchIntervalInSeconds(60 * 10)
				.build();
		FirebaseRemoteConfig.getInstance().setConfigSettingsAsync(settings)
		.addOnCompleteListener(this, task -> {
			if (task.isSuccessful()) {
				onFirebaseRemoteConfigSettingsComplete();
			} else {
				onFirebaseRemoveConfigFailure();
			}
		});
	}

	private void onFirebaseRemoteConfigSettingsComplete() {
		Log.i(LOG_TAG, "Firebase remote config settings loaded. Fetching remote config.");
		FirebaseRemoteConfig.getInstance().fetchAndActivate()
				.addOnCompleteListener(this, task -> {
					if (task.isSuccessful()) {
						onFirebaseRemoteConfigFetchComplete();
					} else {
						onFirebaseRemoveConfigFailure();
					}
				});
	}

	private void onFirebaseRemoteConfigFetchComplete() {
		Log.i(LOG_TAG, "Firebase remote config fetched successfully.");
		if (FirebaseRemoteConfig.getInstance().getBoolean(FIREBASE_AUTH_ENABLED_KEY)) {
			Log.i(LOG_TAG, "Initializing firebase auth and user manager.");
			authManager = new AndroidFirebaseAuthManager(this);
			game.authManager = authManager;
			FirebaseAuth.getInstance().addAuthStateListener(authManager);
			game.userManager = new AndroidUserManager();
		}
		onConfigComplete();
	}

	private void onFirebaseRemoveConfigFailure() {
		Log.i(LOG_TAG, "Firebase remote config failed.");
		onConfigComplete();
	}

	private void onConfigComplete() {
		Log.i(LOG_TAG, "Android config complete.");
		game.onConfigComplete();
	}
}
