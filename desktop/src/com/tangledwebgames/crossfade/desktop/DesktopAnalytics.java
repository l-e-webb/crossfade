package com.tangledwebgames.crossfade.desktop;

import com.tangledwebgames.crossfade.analytics.CrossFadeAnalytics;

class DesktopAnalytics implements CrossFadeAnalytics {

    @Override
    public void appStart() { }

    @Override
    public void login() { }

    @Override
    public void signUp() { }

    @Override
    public void signOut() { }

    @Override
    public void levelStart(int level) { }

    @Override
    public void levelComplete(int level, int time, int moves, boolean isRecord, boolean isFirstTime) { }

    @Override
    public void levelSkipped(int level, int time, int moves) { }

    @Override
    public void purchaseFullVersion() { }

    @Override
    public void restoreFullVersion() { }

    @Override
    public void hitMaxFreeLevel() { }
}
