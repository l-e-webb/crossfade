package com.tangledwebgames.crossfade.analytics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class AnalyticsLogger implements CrossFadeAnalytics {

    public String logTag = AnalyticsLogger.class.getSimpleName();
    public int logLevel = Application.LOG_INFO;

    @Override
    public void appStart() {
        logEvent("App Start");
    }

    @Override
    public void login() {
        logEvent("Log In");
    }

    @Override
    public void signUp() {
        logEvent("Sign Up");
    }

    @Override
    public void logOut() {
        logEvent("Sign Out");
    }

    @Override
    public void levelStart(int level) {
        logEvent("Start Level " + level);
    }

    @Override
    public void levelComplete(int level, int time, int moves, boolean isRecord, boolean isFirstTime) {
        String event = "Level " + level + " complete (";
        event += (time + "s, ");
        event += (moves + " moves");
        if (isRecord) {
            event += ", new record";
        }
        if (isFirstTime) {
            event += ", first time";
        }
        event += ")";
        logEvent(event);
    }

    @Override
    public void levelSkipped(int level, int time, int moves) {
        String event = "Level " + level + " skipped (" + time + "s, " + moves + " moves)";
        logEvent(event);
    }

    @Override
    public void purchaseFullVersion() {
        logEvent("Full Version Purchase");
    }

    @Override
    public void restoreFullVersion() {
        logEvent("Full Version Restore");
    }

    @Override
    public void hitMaxFreeLevel() {
        logEvent("Hit Max Free Level");
    }

    private void logEvent(String event) {
        String logText = "Event: " + event;
        switch (logLevel) {
            case Application.LOG_DEBUG:
                Gdx.app.debug(logTag, logText);
                break;
            case Application.LOG_ERROR:
                Gdx.app.error(logTag, logText);
                break;
            case Application.LOG_INFO:
                Gdx.app.log(logTag, logText);
        }
    }
}
