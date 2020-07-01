package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.pay.PurchaseManager;
import com.tangledwebgames.crossfade.analytics.CrossFadeAnalytics;
import com.tangledwebgames.crossfade.auth.AuthManager;

import java.util.Locale;

public class CrossFadeGame extends Game {

    public static CrossFadeGame game;

    public static Application.ApplicationType APP_TYPE;
    public static Locale LOCALE;
    public PurchaseManager purchaseManager;
    public AuthManager authManager;
    public CrossFadeAnalytics analytics;

    @Override
    public void create() {
        APP_TYPE = Gdx.app.getType();
        LOCALE = Locale.getDefault();
        game = this;
        analytics.appStart();
        if (APP_TYPE == Application.ApplicationType.Android) {
            Gdx.input.setCatchKey(Input.Keys.MENU, true);
            CrossFadePurchaseManager.setPurchaseManager(purchaseManager);
        }
        this.setScreen(new LoadingScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (purchaseManager != null) purchaseManager.dispose();
    }
}
