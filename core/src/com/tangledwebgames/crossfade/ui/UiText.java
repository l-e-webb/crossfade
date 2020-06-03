package com.tangledwebgames.crossfade.ui;

import com.tangledwebgames.crossfade.data.AssetLoader;

/**
 * Data class for storing UI text.
 */
public class UiText {

    static String TITLE;
    static String MOVES;
    static String TIME;
    static String LEVEL;
    static String NEXT;
    static String RESET;
    static String PREVIOUS;
    static String RANDOM;
    static String RANDOM_BUTTON;
    static String UNKNOWN_LEVEL;
    static String PAUSED;
    static String WIN_MSG;
    static String NEW_RECORD;
    static String SFX;
    static String SFX_LEVEL;
    static String MUSIC;
    static String MUSIC_LEVEL;
    static String ANIMATE_TILES;
    static String HIGHLIGHT_TILES;
    static String CONTINUE;
    static String BEST;
    static String MENU;
    static String LEVEL_SELECT;
    static String SELECT;
    static String LEVEL_SELECT_HEADING;
    static String SANDBOX;
    static String BUY_FULL_VERSION;
    static String CONTENT_UNAVAILABLE;
    static String RESTORE_PROMPT;
    static String BUY;
    static String RESTORE;
    static String OK;
    static String CANCEL;
    static String FULL_VERSION_UNLOCKED;
    static String PURCHASE_ERROR;
    static String NO_RESTORE;
    static String LOCKED;

    private UiText() {
    }

    public static void init() {
        TITLE = AssetLoader.instance.gameText.get("TITLE");
        MOVES = AssetLoader.instance.gameText.get("MOVES");
        TIME = AssetLoader.instance.gameText.get("TIME");
        LEVEL = AssetLoader.instance.gameText.get("LEVEL");
        NEXT = AssetLoader.instance.gameText.get("NEXT");
        RESET = AssetLoader.instance.gameText.get("RESET");
        PREVIOUS = AssetLoader.instance.gameText.get("PREVIOUS");
        RANDOM = AssetLoader.instance.gameText.get("RANDOM");
        RANDOM_BUTTON = AssetLoader.instance.gameText.get("RANDOM_BUTTON");
        UNKNOWN_LEVEL = AssetLoader.instance.gameText.get("UNKNOWN_LEVEL");
        PAUSED = AssetLoader.instance.gameText.get("PAUSED");
        WIN_MSG = AssetLoader.instance.gameText.get("WIN_MSG");
        NEW_RECORD = AssetLoader.instance.gameText.get("NEW_RECORD");
        SFX = AssetLoader.instance.gameText.get("SFX");
        SFX_LEVEL = AssetLoader.instance.gameText.get("SFX_LEVEL");
        MUSIC = AssetLoader.instance.gameText.get("MUSIC");
        MUSIC_LEVEL = AssetLoader.instance.gameText.get("MUSIC_LEVEL");
        ANIMATE_TILES = AssetLoader.instance.gameText.get("ANIMATE_TILES");
        HIGHLIGHT_TILES = AssetLoader.instance.gameText.get("HIGHLIGHT_TILES");
        CONTINUE = AssetLoader.instance.gameText.get("CONTINUE");
        BEST = AssetLoader.instance.gameText.get("RECORD");
        MENU = AssetLoader.instance.gameText.get("MENU");
        LEVEL_SELECT = AssetLoader.instance.gameText.get("LEVEL_SELECT");
        SELECT = AssetLoader.instance.gameText.get("SELECT");
        LEVEL_SELECT_HEADING = AssetLoader.instance.gameText.get("LEVEL_SELECT_HEADING");
        SANDBOX = AssetLoader.instance.gameText.get("SANDBOX");
        BUY_FULL_VERSION = AssetLoader.instance.gameText.get("BUY_FULL_VERSION");
        CONTENT_UNAVAILABLE = AssetLoader.instance.gameText.get("CONTENT_UNAVAILABLE");
        RESTORE_PROMPT = AssetLoader.instance.gameText.get("RESTORE_PROMPT");
        BUY = AssetLoader.instance.gameText.get("BUY");
        RESTORE = AssetLoader.instance.gameText.get("RESTORE");
        OK = AssetLoader.instance.gameText.get("OK");
        CANCEL = AssetLoader.instance.gameText.get("CANCEL");
        FULL_VERSION_UNLOCKED = AssetLoader.instance.gameText.get("FULL_VERSION_UNLOCKED");
        PURCHASE_ERROR = AssetLoader.instance.gameText.get("PURCHASE_ERROR");
        LOCKED = AssetLoader.instance.gameText.get("LOCKED");
        NO_RESTORE = AssetLoader.instance.gameText.get("NO_RESTORE");
    }

}
