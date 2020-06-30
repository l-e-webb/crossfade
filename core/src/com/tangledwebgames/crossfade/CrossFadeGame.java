package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.pay.PurchaseManager;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.data.AssetLoader;
import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.ui.UiText;

import java.util.Locale;

public class CrossFadeGame extends Game {

    public static Application.ApplicationType APP_TYPE;
    public static Locale LOCALE;
    public PurchaseManager purchaseManager;
    public AuthManager authManager;

    @Override
    public void create() {
        APP_TYPE = Gdx.app.getType();
        LOCALE = Locale.getDefault();
        if (APP_TYPE == Application.ApplicationType.Android) {
            Gdx.input.setCatchKey(Input.Keys.MENU, true);
        }
        AssetLoader.instance.loadAll();
        SettingsManager.init();
        if (APP_TYPE == Application.ApplicationType.Android) {
            CrossFadePurchaseManager.setPurchaseManager(purchaseManager);
        }
        Levels.init();
        UiText.init();
        this.setScreen(new MainScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (purchaseManager != null) purchaseManager.dispose();
    }
}
