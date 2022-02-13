package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.tangledwebgames.crossfade.data.userdata.UserDataChangeListener;

import javax.annotation.Nonnull;

public class CrossFadePurchaseManager {

    interface PurchaseEventListener {
        void onFullVersionRestored();
        void onRestoreError();
        void onRestoreFailure();
        void onSuccessfulPurchase();
        void onPurchaseError();
        void onPurchaseCanceled();
    }

    private static final String LOG_TAG = CrossFadePurchaseManager.class.getSimpleName();
    private static final String FULL_VERSION_SKU = "full_version";

    private static PurchaseManager pm;
    private static boolean isPurchaseObserverInstalled = false;

    private static final PurchaseEventListener emptyEventListener = new PurchaseEventListener() {
        @Override
        public void onFullVersionRestored() {
        }

        @Override
        public void onRestoreError() {
        }

        @Override
        public void onRestoreFailure() {
        }

        @Override
        public void onSuccessfulPurchase() {
        }

        @Override
        public void onPurchaseError() {
        }

        @Override
        public void onPurchaseCanceled() {
        }
    };

    private static PurchaseEventListener purchaseEventListener = emptyEventListener;

    private static final PurchaseObserver purchaseObserver = new PurchaseObserver() {
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
            Gdx.app.log(LOG_TAG, "Attempting to restore from prior transactions.");
            boolean success = false;
            if (transactions != null && transactions.length > 0) {
                for (Transaction t : transactions) {
                    Gdx.app.log(LOG_TAG, t.getUserId() + "");
                    if (isPurchaseTransaction(t)) {
                        success = true;
                        break;
                    }
                }
            }
            if (success) {
                Gdx.app.log(LOG_TAG, "Full version restoration successful.");
                CrossFadeGame.game.userManager.saveHasFullVersion(true);
                purchaseEventListener.onFullVersionRestored();
            } else {
                Gdx.app.log(LOG_TAG, "Full version restoration failure.");
                purchaseEventListener.onRestoreFailure();
            }
        }

        @Override
        public void handleRestoreError(Throwable e) {
            Gdx.app.error(LOG_TAG, "Full version restoration error.", e);
            purchaseEventListener.onRestoreError();
        }

        @Override
        public void handlePurchase(Transaction transaction) {
            Gdx.app.log(LOG_TAG, "Handling purchase transaction.");
            if (isPurchaseTransaction(transaction)) {
                Gdx.app.log(LOG_TAG, "Full version purchase successful.");
                CrossFadeGame.game.userManager.saveHasFullVersion(true);
                purchaseEventListener.onSuccessfulPurchase();
            }
        }

        @Override
        public void handlePurchaseError(Throwable e) {
            Gdx.app.error(LOG_TAG, "Full version purchase error.", e);
            purchaseEventListener.onPurchaseError();
        }

        @Override
        public void handlePurchaseCanceled() {
            Gdx.app.log(LOG_TAG, "Purchase attempt canceled.");
            purchaseEventListener.onPurchaseCanceled();
        }
    };

    private final static UserDataChangeListener userDataChangeListener = new UserDataChangeListener() {
        @Override
        public void onFullVersionChange() {
            if (!CrossFadeGame.game.userManager.hasFullVersion()) {
                restoreSilently();
            }
        }

        @Override
        public void onRecordChange() {
        }
    };

    static void setPurchaseManager(@Nonnull PurchaseManager pm) {
        CrossFadePurchaseManager.pm = pm;
        init();
    }

    private static void init() {
        Gdx.app.log(LOG_TAG, "Initiating purchase manager.");
        PurchaseManagerConfig pmc = new PurchaseManagerConfig();
        pmc.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(FULL_VERSION_SKU));
        pm.install(purchaseObserver, pmc, true);
        CrossFadeGame.game.userManager.addUserDataChangeListener(userDataChangeListener);
    }

    static void buyFullVersion(PurchaseEventListener listener) {
        Gdx.app.log(LOG_TAG, "Initiating full version purchase.");
        CrossFadePurchaseManager.purchaseEventListener = listener;
        pm.purchase(FULL_VERSION_SKU);
    }

    static void restore(PurchaseEventListener listener) {
        Gdx.app.log(LOG_TAG, "Initiating full version restoration.");
        CrossFadePurchaseManager.purchaseEventListener = listener;
        pm.purchaseRestore();
    }

    static void restoreSilently() {
        restore(emptyEventListener);
    }

    public static boolean isPurchaseAvailable() {
        return isPurchaseObserverInstalled &&
                !CrossFadeGame.game.userManager.hasFullVersion() &&
                getLocalPrice() != null;
    }

    public static String getLocalPrice() {
        if (pm == null) return null;
        Information info = pm.getInformation(FULL_VERSION_SKU);
        if (info == null) return null;
        return info.getLocalPricing();
    }

    public static String getLocalDescription() {
        if (pm == null) return null;
        Information info = pm.getInformation(FULL_VERSION_SKU);
        if (info == null) return null;
        String description = info.getLocalDescription();
        if (description == null) return null;
        return description.replace("\n", "");
    }

    private static boolean isPurchaseTransaction(Transaction transaction) {
        return transaction.isPurchased() &&
                transaction.getIdentifier().equals(FULL_VERSION_SKU);
    }
}
