package com.tangledwebgames.crossfade.ui;

import com.tangledwebgames.crossfade.data.AssetManager;

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
        TITLE = AssetManager.instance.gameText.get("TITLE");
        MOVES = AssetManager.instance.gameText.get("MOVES");
        TIME = AssetManager.instance.gameText.get("TIME");
        LEVEL = AssetManager.instance.gameText.get("LEVEL");
        NEXT = AssetManager.instance.gameText.get("NEXT");
        RESET = AssetManager.instance.gameText.get("RESET");
        PREVIOUS = AssetManager.instance.gameText.get("PREVIOUS");
        RANDOM = AssetManager.instance.gameText.get("RANDOM");
        RANDOM_BUTTON = AssetManager.instance.gameText.get("RANDOM_BUTTON");
        UNKNOWN_LEVEL = AssetManager.instance.gameText.get("UNKNOWN_LEVEL");
        PAUSED = AssetManager.instance.gameText.get("PAUSED");
        WIN_MSG = AssetManager.instance.gameText.get("WIN_MSG");
        NEW_RECORD = AssetManager.instance.gameText.get("NEW_RECORD");
        SFX = AssetManager.instance.gameText.get("SFX");
        SFX_LEVEL = AssetManager.instance.gameText.get("SFX_LEVEL");
        MUSIC = AssetManager.instance.gameText.get("MUSIC");
        MUSIC_LEVEL = AssetManager.instance.gameText.get("MUSIC_LEVEL");
        ANIMATE_TILES = AssetManager.instance.gameText.get("ANIMATE_TILES");
        HIGHLIGHT_TILES = AssetManager.instance.gameText.get("HIGHLIGHT_TILES");
        CONTINUE = AssetManager.instance.gameText.get("CONTINUE");
        BEST = AssetManager.instance.gameText.get("RECORD");
        MENU = AssetManager.instance.gameText.get("MENU");
        LEVEL_SELECT = AssetManager.instance.gameText.get("LEVEL_SELECT");
        SELECT = AssetManager.instance.gameText.get("SELECT");
        LEVEL_SELECT_HEADING = AssetManager.instance.gameText.get("LEVEL_SELECT_HEADING");
        SANDBOX = AssetManager.instance.gameText.get("SANDBOX");
        BUY_FULL_VERSION = AssetManager.instance.gameText.get("BUY_FULL_VERSION");
        CONTENT_UNAVAILABLE = AssetManager.instance.gameText.get("CONTENT_UNAVAILABLE");
        RESTORE_PROMPT = AssetManager.instance.gameText.get("RESTORE_PROMPT");
        BUY = AssetManager.instance.gameText.get("BUY");
        RESTORE = AssetManager.instance.gameText.get("RESTORE");
        OK = AssetManager.instance.gameText.get("OK");
        CANCEL = AssetManager.instance.gameText.get("CANCEL");
        FULL_VERSION_UNLOCKED = AssetManager.instance.gameText.get("FULL_VERSION_UNLOCKED");
        PURCHASE_ERROR = AssetManager.instance.gameText.get("PURCHASE_ERROR");
        LOCKED = AssetManager.instance.gameText.get("LOCKED");
        NO_RESTORE = AssetManager.instance.gameText.get("NO_RESTORE");
    }

}
