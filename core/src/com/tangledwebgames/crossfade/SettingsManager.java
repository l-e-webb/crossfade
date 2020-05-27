package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.tangledwebgames.crossfade.sound.SoundManager;

public class SettingsManager {

    private static Preferences prefs;

    private static final String MUSIC_ON_KEY = "music_on";
    private static final String MUSIC_VOLUME_KEY = "music_volume";
    private static final String SFX_ON_KEY = "sfx_on";
    private static final String SFX_VOLUME_KEY = "sfx_volume";
    private static final String ANIMATE_TILES_KEY = "animate_tiles";
    private static final String HIGHLIGHT_TILES_KEY = "highlight_tiles";
    private static final String FULL_VERSION_KEY = "full_version";

    private static final String PREFERENCES_NAME = "crossfade_preferences";

    private static boolean isMusicOn;
    private static float musicVolume;
    private static boolean isSfxOn;
    private static float sfxVolume;
    private static boolean animateTiles;
    private static boolean highlightTiles;
    private static boolean isFullVersion;

    static void init() {
        prefs = Gdx.app.getPreferences(PREFERENCES_NAME);
        isMusicOn = prefs.getBoolean(
                MUSIC_ON_KEY,
                CrossFadeGame.APP_TYPE != Application.ApplicationType.WebGL
        );
        musicVolume = prefs.getFloat(MUSIC_VOLUME_KEY, 1);
        isSfxOn = prefs.getBoolean(SFX_ON_KEY, true);
        sfxVolume = prefs.getFloat(SFX_VOLUME_KEY, 1);
        animateTiles = prefs.getBoolean(ANIMATE_TILES_KEY, true);
        highlightTiles = prefs.getBoolean(HIGHLIGHT_TILES_KEY, true);
        isFullVersion = prefs.getBoolean(FULL_VERSION_KEY, false);
        SoundManager.updateFromSettings();
    }

    static void flush() {
        prefs.putBoolean(MUSIC_ON_KEY, isMusicOn);
        prefs.putFloat(MUSIC_VOLUME_KEY, musicVolume);
        prefs.putBoolean(SFX_ON_KEY, isSfxOn);
        prefs.putFloat(SFX_VOLUME_KEY, sfxVolume);
        prefs.putBoolean(ANIMATE_TILES_KEY, animateTiles);
        prefs.putBoolean(HIGHLIGHT_TILES_KEY, highlightTiles);
        prefs.putBoolean(FULL_VERSION_KEY, isFullVersion);
        prefs.flush();
    }

    public static boolean isMusicOn() {
        return isMusicOn;
    }

    static void setIsMusicOn(boolean isMusicOn) {
        SettingsManager.isMusicOn = isMusicOn;
        SoundManager.updateFromSettings();
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    static void setMusicVolume(float musicVolume) {
        SettingsManager.musicVolume = musicVolume;
        SoundManager.updateFromSettings();
    }

    public static boolean isSfxOn() {
        return isSfxOn;
    }

    static void setIsSfxOn(boolean isSfxOn) {
        SettingsManager.isSfxOn = isSfxOn;
        SoundManager.updateFromSettings();
    }

    public static float getSfxVolume() {
        return sfxVolume;
    }

    static void setSfxVolume(float sfxVolume) {
        SettingsManager.sfxVolume = sfxVolume;
        SoundManager.updateFromSettings();
    }

    public static boolean isAnimateTiles() {
        return animateTiles;
    }

    static void setAnimateTiles(boolean animateTiles) {
        SettingsManager.animateTiles = animateTiles;
        MainController.instance.gameController.setAnimateTiles(animateTiles);
    }

    public static boolean isHighlightTiles() {
        return highlightTiles;
    }

    static void setHighlightTiles(boolean highlightTiles) {
        SettingsManager.highlightTiles = highlightTiles;
        MainController.instance.gameController.setHighlightTiles(highlightTiles);
    }

    public static boolean isFullVersion() {
        if (CrossFadeGame.APP_TYPE != Application.ApplicationType.Android) {
            return true;
        }
        return isFullVersion;
    }

    static void setIsFullVersion(boolean isFullVersion) {
        SettingsManager.isFullVersion = isFullVersion;
    }

}
