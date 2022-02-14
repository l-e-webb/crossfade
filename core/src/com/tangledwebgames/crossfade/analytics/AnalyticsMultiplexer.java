package com.tangledwebgames.crossfade.analytics;

import java.util.HashSet;
import java.util.Set;

public class AnalyticsMultiplexer implements CrossFadeAnalytics {

    private final Set<CrossFadeAnalytics> analyticsSet = new HashSet<>();

    public void add(CrossFadeAnalytics analytics) {
        analyticsSet.add(analytics);
    }

    public void remove(CrossFadeAnalytics analytics) {
        analyticsSet.remove(analytics);
    }

    @Override
    public void appStart() {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.appStart();
        }
    }

    @Override
    public void login() {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.login();
        }
    }

    @Override
    public void signUp() {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.signUp();
        }
    }

    @Override
    public void logOut() {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.logOut();
        }
    }

    @Override
    public void levelStart(int level) {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.levelStart(level);
        }
    }

    @Override
    public void levelComplete(int level, int time, int moves, boolean isRecord, boolean isFirstTime) {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.levelComplete(level, time, moves, isRecord, isFirstTime);
        }
    }

    @Override
    public void levelSkipped(int level, int time, int moves) {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.levelSkipped(level, time, moves);
        }
    }

    @Override
    public void purchaseFullVersion() {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.purchaseFullVersion();
        }
    }

    @Override
    public void restoreFullVersion() {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.restoreFullVersion();
        }
    }

    @Override
    public void hitMaxFreeLevel() {
        for (CrossFadeAnalytics a : analyticsSet) {
            a.hitMaxFreeLevel();
        }
    }
}
