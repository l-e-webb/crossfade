package com.tangledwebgames.crossfade.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.tangledwebgames.crossfade.sound.SoundManager;

import java.util.HashSet;

public class SettingsManager {

    public static final boolean IS_MUSIC_ON_DEFAULT = false;
    public static final float MUSIC_VOLUME_DEFAULT = 1f;
    public static final boolean IS_SFX_ON_DEFAULT = true;
    public static final float SFX_VOLUME_DEFAULT = 1f;
    public static final boolean ANIMATE_TILES_DEFAULT = true;
    public static final boolean HIGHLIGHT_TILES_DEFAULT = true;
    public static final boolean SHARE_USAGE_DATA_DEFAULT = false;

    private static Preferences prefs;

    private static final String MUSIC_ON_KEY = "music_on";
    private static final String MUSIC_VOLUME_KEY = "music_volume";
    private static final String SFX_ON_KEY = "sfx_on";
    private static final String SFX_VOLUME_KEY = "sfx_volume";
    private static final String ANIMATE_TILES_KEY = "animate_tiles";
    private static final String HIGHLIGHT_TILES_KEY = "highlight_tiles";
    private static final String SHARE_USAGE_DATA_KEY = "share_data";
    private static final String SHARE_DATA_DIALOG_SHOWN_KEY = "share_data_dialog_shown";

    private static final String PREFERENCES_NAME = "crossfade_preferences";

    private static boolean isMusicOn;
    private static float musicVolume;
    private static boolean isSfxOn;
    private static float sfxVolume;
    private static boolean animateTiles;
    private static boolean highlightTiles;
    private static boolean isSharingUsageData;
    private static boolean isDataSharingDialogShown;

    private static final HashSet<DataSharingPermissionListener> dataSharingPermissionListeners = new HashSet<>();

    public static void init() {
        prefs = Gdx.app.getPreferences(PREFERENCES_NAME);
        isMusicOn = prefs.getBoolean(MUSIC_ON_KEY, IS_MUSIC_ON_DEFAULT);
        musicVolume = prefs.getFloat(MUSIC_VOLUME_KEY, MUSIC_VOLUME_DEFAULT);
        isSfxOn = prefs.getBoolean(SFX_ON_KEY, IS_SFX_ON_DEFAULT);
        sfxVolume = prefs.getFloat(SFX_VOLUME_KEY, SFX_VOLUME_DEFAULT);
        animateTiles = prefs.getBoolean(ANIMATE_TILES_KEY, ANIMATE_TILES_DEFAULT);
        highlightTiles = prefs.getBoolean(HIGHLIGHT_TILES_KEY, HIGHLIGHT_TILES_DEFAULT);
        isSharingUsageData = prefs.getBoolean(SHARE_USAGE_DATA_KEY, SHARE_USAGE_DATA_DEFAULT);
        isDataSharingDialogShown = prefs.getBoolean(SHARE_DATA_DIALOG_SHOWN_KEY, false);
        SoundManager.updateFromSettings();
    }

    public static void flush() {
        prefs.putBoolean(MUSIC_ON_KEY, isMusicOn);
        prefs.putFloat(MUSIC_VOLUME_KEY, musicVolume);
        prefs.putBoolean(SFX_ON_KEY, isSfxOn);
        prefs.putFloat(SFX_VOLUME_KEY, sfxVolume);
        prefs.putBoolean(ANIMATE_TILES_KEY, animateTiles);
        prefs.putBoolean(HIGHLIGHT_TILES_KEY, highlightTiles);
        prefs.putBoolean(SHARE_USAGE_DATA_KEY, isSharingUsageData);
        prefs.putBoolean(SHARE_DATA_DIALOG_SHOWN_KEY, isDataSharingDialogShown);
        prefs.flush();
    }

    public static boolean isMusicOn() {
        return isMusicOn;
    }

    public static void setIsMusicOn(boolean isMusicOn) {
        SettingsManager.isMusicOn = isMusicOn;
        SoundManager.updateFromSettings();
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        SettingsManager.musicVolume = musicVolume;
        SoundManager.updateFromSettings();
    }

    public static boolean isSfxOn() {
        return isSfxOn;
    }

    public static void setIsSfxOn(boolean isSfxOn) {
        SettingsManager.isSfxOn = isSfxOn;
        SoundManager.updateFromSettings();
    }

    public static float getSfxVolume() {
        return sfxVolume;
    }

    public static void setSfxVolume(float sfxVolume) {
        SettingsManager.sfxVolume = sfxVolume;
        SoundManager.updateFromSettings();
    }

    public static boolean isAnimateTiles() {
        return animateTiles;
    }

    public static void setAnimateTiles(boolean animateTiles) {
        SettingsManager.animateTiles = animateTiles;
    }

    public static boolean isHighlightTiles() {
        return highlightTiles;
    }

    public static void setHighlightTiles(boolean highlightTiles) {
        SettingsManager.highlightTiles = highlightTiles;
    }

    public static boolean isSharingUsageData() {
        return isSharingUsageData;
    }

    public static void setIsSharingUsageData(boolean isSharingUsageData) {
        SettingsManager.isSharingUsageData = isSharingUsageData;
        notifyDataSharingPermissionListeners(isSharingUsageData);
    }

    public static boolean isIsDataSharingDialogShown() {
        return isDataSharingDialogShown;
    }

    public static void setIsDataSharingDialogShown(boolean isDataSharingDialogShown) {
        SettingsManager.isDataSharingDialogShown = isDataSharingDialogShown;
    }

    public static void addDataSharingPermissionListener(DataSharingPermissionListener listener) {
        dataSharingPermissionListeners.add(listener);
    }

    public static void removeDataSharingPermissionListener(DataSharingPermissionListener listener) {
        dataSharingPermissionListeners.remove(listener);
    }

    public static void clearDataSharingPermissionListeners() {
        dataSharingPermissionListeners.clear();
    }

    private static void notifyDataSharingPermissionListeners(boolean isSharingUsageData) {
        for (DataSharingPermissionListener listener : dataSharingPermissionListeners) {
            listener.onDataSharingPermissionChanged(isSharingUsageData);
        }
    }
}
