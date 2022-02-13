package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.pay.PurchaseManager;
import com.tangledwebgames.crossfade.analytics.AuthChangeLogger;
import com.tangledwebgames.crossfade.analytics.CrossFadeAnalytics;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.data.userdata.GdxUserManager;
import com.tangledwebgames.crossfade.data.userdata.UserManager;

import java.util.Locale;

public class CrossFadeGame extends Game {

    public static CrossFadeGame game;

    public static Application.ApplicationType APP_TYPE;
    public static Locale LOCALE;
    public static final String VERSION = "1.4.0";

    public boolean debug = false;
    public PurchaseManager purchaseManager;
    public AuthManager authManager;
    public CrossFadeAnalytics analytics;
    public UserManager userManager;

    @Override
    public void create() {
        APP_TYPE = Gdx.app.getType();
        LOCALE = Locale.getDefault();
        game = this;
        if (debug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
        if (userManager == null) {
            userManager = new GdxUserManager();
        }
        userManager.initialize();
        analytics.appStart();
        authManager.addChangeListener(new AuthChangeLogger());
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
