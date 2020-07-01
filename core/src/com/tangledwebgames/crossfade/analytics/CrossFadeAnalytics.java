package com.tangledwebgames.crossfade.analytics;

import java.util.Map;

public interface CrossFadeAnalytics {

    void appStart();
    void login();
    void signUp();
    void signOut();
    void levelStart(int level);
    void levelComplete(int level, int time, int moves, boolean isRecord, boolean isFirstTime);
    void levelSkipped(int level, int time, int moves);
    void purchaseFullVersion();
    void restoreFullVersion();
    void hitMaxFreeLevel();
}
