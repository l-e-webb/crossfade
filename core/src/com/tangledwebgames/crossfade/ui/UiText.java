package com.tangledwebgames.crossfade.ui;

import com.tangledwebgames.crossfade.Assets;

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
        TITLE = Assets.instance.gameText.get("TITLE");
        MOVES = Assets.instance.gameText.get("MOVES");
        TIME = Assets.instance.gameText.get("TIME");
        LEVEL = Assets.instance.gameText.get("LEVEL");
        NEXT = Assets.instance.gameText.get("NEXT");
        RESET = Assets.instance.gameText.get("RESET");
        PREVIOUS = Assets.instance.gameText.get("PREVIOUS");
        RANDOM = Assets.instance.gameText.get("RANDOM");
        RANDOM_BUTTON = Assets.instance.gameText.get("RANDOM_BUTTON");
        UNKNOWN_LEVEL = Assets.instance.gameText.get("UNKNOWN_LEVEL");
        PAUSED = Assets.instance.gameText.get("PAUSED");
        WIN_MSG = Assets.instance.gameText.get("WIN_MSG");
        NEW_RECORD = Assets.instance.gameText.get("NEW_RECORD");
        SFX = Assets.instance.gameText.get("SFX");
        SFX_LEVEL = Assets.instance.gameText.get("SFX_LEVEL");
        MUSIC = Assets.instance.gameText.get("MUSIC");
        MUSIC_LEVEL = Assets.instance.gameText.get("MUSIC_LEVEL");
        ANIMATE_TILES = Assets.instance.gameText.get("ANIMATE_TILES");
        HIGHLIGHT_TILES = Assets.instance.gameText.get("HIGHLIGHT_TILES");
        CONTINUE = Assets.instance.gameText.get("CONTINUE");
        BEST = Assets.instance.gameText.get("RECORD");
        MENU = Assets.instance.gameText.get("MENU");
        LEVEL_SELECT = Assets.instance.gameText.get("LEVEL_SELECT");
        SELECT = Assets.instance.gameText.get("SELECT");
        LEVEL_SELECT_HEADING = Assets.instance.gameText.get("LEVEL_SELECT_HEADING");
        SANDBOX = Assets.instance.gameText.get("SANDBOX");
        BUY_FULL_VERSION = Assets.instance.gameText.get("BUY_FULL_VERSION");
        CONTENT_UNAVAILABLE = Assets.instance.gameText.get("CONTENT_UNAVAILABLE");
        RESTORE_PROMPT = Assets.instance.gameText.get("RESTORE_PROMPT");
        BUY = Assets.instance.gameText.get("BUY");
        RESTORE = Assets.instance.gameText.get("RESTORE");
        OK = Assets.instance.gameText.get("OK");
        CANCEL = Assets.instance.gameText.get("CANCEL");
        FULL_VERSION_UNLOCKED = Assets.instance.gameText.get("FULL_VERSION_UNLOCKED");
        PURCHASE_ERROR = Assets.instance.gameText.get("PURCHASE_ERROR");
        LOCKED = Assets.instance.gameText.get("LOCKED");
        NO_RESTORE = Assets.instance.gameText.get("NO_RESTORE");
    }

}
