package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.pay.PurchaseManager;
import com.tangledwebgames.crossfade.analytics.AnalyticsLogger;
import com.tangledwebgames.crossfade.analytics.AnalyticsMultiplexer;
import com.tangledwebgames.crossfade.analytics.AuthChangeLogger;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.AuthManagerEmpty;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.data.userdata.GdxUserManager;
import com.tangledwebgames.crossfade.data.userdata.UserManager;

import java.util.Locale;

public class CrossFadeGame extends Game {

    private static final String LOG_TAG = CrossFadeGame.class.getSimpleName();

    public static CrossFadeGame game;

    public static Application.ApplicationType APP_TYPE;
    public static Locale LOCALE;
    public static final String VERSION = "1.4.0";
    public static final String PRIVACY_POLICY_URL = "https://tangledwebgames.com/privacy_policy";


    public final AnalyticsMultiplexer analytics = new AnalyticsMultiplexer();

    public boolean debug = false;
    public boolean configComplete = false;
    public boolean created = false;
    public PurchaseManager purchaseManager;
    public AuthManager authManager;
    public UserManager userManager;

    @Override
    public void create() {
        APP_TYPE = Gdx.app.getType();
        LOCALE = Locale.getDefault();
        game = this;
        created = true;
        if (debug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
        analytics.add(new AnalyticsLogger());
        analytics.appStart();

        // If config is already complete before create() was called.
        if (configComplete) {
            onConfigComplete();
        }

        this.setScreen(new LoadingScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (purchaseManager != null) purchaseManager.dispose();
        SettingsManager.clearDataSharingPermissionListeners();
    }

    public void onConfigComplete() {
        configComplete = true;
        if (!created) return; // Config will be completed on create()

        // Init auth manager
        if (authManager == null) {
            Gdx.app.log(LOG_TAG, "No auth manager set. Using empty one.");
            authManager = new AuthManagerEmpty();
        }
        authManager.addChangeListener(new AuthChangeLogger());

        // Init user manager
        if (userManager == null) {
            Gdx.app.log(LOG_TAG, "No user manager set. Using local storage.");
            userManager = new GdxUserManager();
        }
        userManager.initialize();

        // Init purchase manager
        if (APP_TYPE == Application.ApplicationType.Android) {
            Gdx.input.setCatchKey(Input.Keys.MENU, true);
            Gdx.app.log(LOG_TAG, "Initializing purchase manager.");
            CrossFadePurchaseManager.setPurchaseManager(purchaseManager);
        }

        Gdx.app.log(LOG_TAG, "Game config complete.");
    }
}
