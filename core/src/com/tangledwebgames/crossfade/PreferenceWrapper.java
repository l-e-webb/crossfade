package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.sound.SoundManager;

public class PreferenceWrapper {

    public static Preferences prefs;
    public static final String SFX_ON_KEY = "sfx_on";
    public static final String MUSIC_ON_KEY = "music_on";
    public static final String SFX_VOLUME_KEY = "sfx_volume";
    public static final String MUSIC_VOLUME_KEY = "music_volume";
    public static final String ANIMATE_TILES_KEY = "animate_tiles";
    public static final String HIGHLIGHT_TILES_KEY = "highlight_tiles";
    public static final String FULL_VERSION_KEY = "full_version";

    private static final String PREFERENCES_NAME = "crossfade_preferences";


    static void init() {
        prefs = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    static void flush() {
        prefs.putBoolean(MUSIC_ON_KEY, SoundManager.isMusicOn());
        prefs.putFloat(MUSIC_VOLUME_KEY, SoundManager.getMusicVolume());
        prefs.putBoolean(SFX_ON_KEY, SoundManager.isSfxOn());
        prefs.putFloat(SFX_VOLUME_KEY, SoundManager.getSfxVolume());
        prefs.putBoolean(ANIMATE_TILES_KEY, MainScreen.instance.getBoard().animateTiles);
        prefs.putBoolean(HIGHLIGHT_TILES_KEY, MainScreen.instance.getBoard().highlightTiles);
        prefs.putBoolean(FULL_VERSION_KEY, CrossFadePurchaseManager.isFullVersion());
        prefs.flush();
    }
}
