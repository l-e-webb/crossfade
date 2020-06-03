package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.tangledwebgames.crossfade.data.SettingsManager;

import javax.annotation.Nonnull;

public class CrossFadePurchaseManager {

    private static final String LOG_TAG = CrossFadePurchaseManager.class.getSimpleName();
    private static final String FULL_VERSION_SKU = "full_version";

    private static PurchaseManager pm;
    private static boolean isPurchaseObserverInstalled = false;

    static void setPurchaseManager(@Nonnull PurchaseManager pm) {
        CrossFadePurchaseManager.pm = pm;
        init();
    }

    static void buyFullVersion() {
        pm.purchase(FULL_VERSION_SKU);
    }

    public static boolean isPurchaseAvailable() {
        return isPurchaseObserverInstalled && !com.tangledwebgames.crossfade.data.SettingsManager.isFullVersion();
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

    static void restore() {
        pm.purchaseRestore();
    }

    private static void init() {
        PurchaseManagerConfig pmc = new PurchaseManagerConfig();
        pmc.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(FULL_VERSION_SKU));
        pm.install(new CrossFadePurchaseObserver(), pmc, true);
    }

    private static class CrossFadePurchaseObserver implements PurchaseObserver {

        @Override
        public void handleInstall() {
            Gdx.app.log(LOG_TAG, "Purchase manager installed");
            isPurchaseObserverInstalled = true;
        }

        @Override
        public void handleInstallError(Throwable e) {
            Gdx.app.error(LOG_TAG, e.getMessage());
            isPurchaseObserverInstalled = false;
        }

        @Override
        public void handleRestore(Transaction[] transactions) {
            if (transactions != null && transactions.length > 0) {
                for (Transaction t : transactions) {
                    handlePurchase(t);
                }
            }
            if (!com.tangledwebgames.crossfade.data.SettingsManager.isFullVersion()) {
                MainController.instance.showPurchaseNoRestoreDialog();
            }
        }

        @Override
        public void handleRestoreError(Throwable e) {
            MainController.instance.showPurchaseFailedDialog();
        }

        @Override
        public void handlePurchase(Transaction transaction) {
            if (transaction.isPurchased() &&
                    transaction.getIdentifier().equals(FULL_VERSION_SKU)) {
                com.tangledwebgames.crossfade.data.SettingsManager.setIsFullVersion(true);
                SettingsManager.flush();
                MainController.instance.showPurchaseSuccessDialog();
            }
        }

        @Override
        public void handlePurchaseError(Throwable e) {
            MainController.instance.showPurchaseFailedDialog();
        }

        @Override
        public void handlePurchaseCanceled() {
            MainController.instance.unpauseGame();
        }
    }
}
