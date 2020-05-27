package com.tangledwebgames.crossfade;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		CrossFadeGame game = new CrossFadeGame();
		game.purchaseManager = new PurchaseManagerGoogleBilling(this);
		initialize(game, config);
	}

}
