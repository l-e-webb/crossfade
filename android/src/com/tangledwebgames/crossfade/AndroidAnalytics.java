package com.tangledwebgames.crossfade;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tangledwebgames.crossfade.analytics.CrossFadeAnalytics;

class AndroidAnalytics implements CrossFadeAnalytics {

    // Events
    private static final String APP_START = FirebaseAnalytics.Event.APP_OPEN;
    private static final String LOG_IN = FirebaseAnalytics.Event.LOGIN;
    private static final String SIGN_UP = FirebaseAnalytics.Event.SIGN_UP;
    private static final String SIGN_OUT = "sign_out";
    private static final String LEVEL_START = FirebaseAnalytics.Event.LEVEL_START;
    private static final String LEVEL_COMPLETE = "level_complete";
    private static final String LEVEL_SKIPPED = "level_skipped";
    private static final String PURCHASE_FULL_VERSION = FirebaseAnalytics.Event.PURCHASE;
    private static final String RESTORE_FULL_VERSION = "restore_full_version";
    private static final String HIT_MAX_FREE_LEVEL = "hit_max_free_level";

    // Params
    private static final String METHOD = FirebaseAnalytics.Param.METHOD;
    private static final String LEVEL = FirebaseAnalytics.Param.LEVEL;
    private static final String TIME = "level_time";
    private static final String MOVES = "level_moves";
    private static final String IS_RECORD = "is_record";
    private static final String IS_FIRST_TIME = "is_first_time";

    private final FirebaseAnalytics firebaseAnalytics;

    AndroidAnalytics(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void appStart() {
        firebaseAnalytics.logEvent(APP_START, null);
    }

    @Override
    public void login() {
        firebaseAnalytics.logEvent(LOG_IN, null);
    }

    @Override
    public void signUp() {
        firebaseAnalytics.logEvent(SIGN_UP, null);
    }

    @Override
    public void signOut() {
        firebaseAnalytics.logEvent(SIGN_OUT, null);
    }

    @Override
    public void levelStart(int level) {
        Bundle levelStartBundle = new Bundle();
        levelStartBundle.putLong(LEVEL, level);
        firebaseAnalytics.logEvent(LEVEL_START, levelStartBundle);
    }

    @Override
    public void levelComplete(int level, int time, int moves, boolean isRecord, boolean isFirstTime) {
        Bundle levelCompleteBundle = new Bundle();
        levelCompleteBundle.putLong(LEVEL, level);
        levelCompleteBundle.putLong(TIME, time);
        levelCompleteBundle.putLong(MOVES, moves);
        levelCompleteBundle.putBoolean(IS_RECORD, isRecord);
        levelCompleteBundle.putBoolean(IS_FIRST_TIME, isFirstTime);
        firebaseAnalytics.logEvent(LEVEL_COMPLETE, levelCompleteBundle);
    }

    @Override
    public void levelSkipped(int level, int time, int moves) {
        Bundle levelSkippedBundle = new Bundle();
        levelSkippedBundle.putLong(LEVEL, level);
        levelSkippedBundle.putLong(TIME, time);
        levelSkippedBundle.putLong(MOVES, moves);
        firebaseAnalytics.logEvent(LEVEL_SKIPPED, levelSkippedBundle);

    }

    @Override
    public void purchaseFullVersion() {
        firebaseAnalytics.logEvent(PURCHASE_FULL_VERSION, null);
    }

    @Override
    public void restoreFullVersion() {
        firebaseAnalytics.logEvent(RESTORE_FULL_VERSION, null);
    }

    @Override
    public void hitMaxFreeLevel() {
        firebaseAnalytics.logEvent(HIT_MAX_FREE_LEVEL, null);
    }
}
