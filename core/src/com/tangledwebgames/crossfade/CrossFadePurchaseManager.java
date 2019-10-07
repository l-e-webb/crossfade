package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.tangledwebgames.crossfade.ui.PauseState;
import com.tangledwebgames.crossfade.ui.UiRenderer;

public class CrossFadePurchaseManager {

    static final String LOG_TAG = CrossFadePurchaseManager.class.getSimpleName();
    static final String FULL_VERSION_SKU = "full_version";

    static PurchaseManager pm;
    static boolean fullVersion;
    static UiRenderer uiRenderer;

    public static void setPurchaseManager(PurchaseManager pm) {
        CrossFadePurchaseManager.pm = pm;
        init();
    }

    public static void setUiRenderer(UiRenderer uiRenderer) {
        CrossFadePurchaseManager.uiRenderer = uiRenderer;
    }

    public static boolean isFullVersion() {
        if (CrossFadeGame.APP_TYPE != Application.ApplicationType.Android) {
            return true;
        }
        return fullVersion;
    }

    public static void buyFullVersion() {
        pm.purchase(FULL_VERSION_SKU);
    }

    public static String getLocalPrice() {
        Information info = pm.getInformation(FULL_VERSION_SKU);
        if (info == null) return "";
        return info.getLocalPricing();
    }

    public static String getLocalDescription() {
        Information info = pm.getInformation(FULL_VERSION_SKU);
        if (info == null) return "";
        return info.getLocalDescription().replace("\n", "");
    }

    public static void restore() {
        pm.purchaseRestore();
    }

    static void init() {
        PurchaseManagerConfig pmc = new PurchaseManagerConfig();
        pmc.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(FULL_VERSION_SKU));
        pm.install(new CrossFadePurchaseObserver(), pmc ,true);
        fullVersion = PreferenceWrapper.prefs.getBoolean(PreferenceWrapper.FULL_VERSION_KEY, false);
    }

    static class CrossFadePurchaseObserver implements PurchaseObserver {
        @Override
        public void handleInstall() {
            Gdx.app.log(LOG_TAG, "Purchase manager installed");
        }

        @Override
        public void handleInstallError(Throwable e) {

        }

        @Override
        public void handleRestore(Transaction[] transactions) {
            if (transactions != null && transactions.length > 0) {
                for (Transaction t : transactions) {
                    handlePurchase(t);
                }
            }
            if (!fullVersion) {
                uiRenderer.initPause(PauseState.PURCHASE_NO_RESTORE);
            }
        }

        @Override
        public void handleRestoreError(Throwable e) {
            uiRenderer.initPause(PauseState.PURCHASE_FAILED);
        }

        @Override
        public void handlePurchase(Transaction transaction) {
            if (transaction.isPurchased() &&
                    transaction.getIdentifier().equals(FULL_VERSION_SKU)) {
                fullVersion = true;
                uiRenderer.onPurchase();
            }
        }

        @Override
        public void handlePurchaseError(Throwable e) {
            uiRenderer.initPause(PauseState.PURCHASE_FAILED);
        }

        @Override
        public void handlePurchaseCanceled() {
            MainScreen.instance.unpauseGame();
        }
    }
}
