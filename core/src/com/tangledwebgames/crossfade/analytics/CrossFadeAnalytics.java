package com.tangledwebgames.crossfade.analytics;

public interface CrossFadeAnalytics {

    void appStart();
    void login();
    void logOut();
    void signUp();
    void levelStart(int level);
    void levelComplete(int level, int time, int moves, boolean isRecord, boolean isFirstTime);
    void levelSkipped(int level, int time, int moves);
    void purchaseFullVersion();
    void restoreFullVersion();
    void hitMaxFreeLevel();
}
