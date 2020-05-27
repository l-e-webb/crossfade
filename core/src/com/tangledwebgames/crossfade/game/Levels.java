package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Loads and stores level data, has only static members.
 */
public class Levels {

    static final String LOG_TAG = Levels.class.getSimpleName();
    public static final int MAX_FREE_LEVEL = 25;

    public static boolean[][][] levels;
    public static boolean[][] trollLevel;
    public static boolean[][] randomLevelPlaceholder;
    public static boolean[][] sandboxLevelPlaceholder;
    public static int[] records;

    private static final String LEVEL_DATA_FILEPATH = "levels.dat";
    private static final String USER_RECORD_FILEPATH = "records.json";
    private static final char LIGHT_MARKER = '#';

    private Levels() {
    }

    public static void init() {
        //Get level strings from file.
        String[][] levelData = loadLevelData();
        if (levelData == null) return;

        //Init levels array.
        levels = new boolean[levelData.length][][];
        for (int levelIndex = 0; levelIndex < levelData.length; levelIndex++) {
            String[] levelRows = levelData[levelIndex];
            boolean[][] level = new boolean[Board.WIDTH][Board.WIDTH];
            for (int i = 0; i < levelRows.length && i < Board.WIDTH; i++) {
                String row = levelRows[i];
                for (int j = 0; j < row.length() && j < Board.WIDTH; j++) {
                    level[i][j] = row.charAt(j) == LIGHT_MARKER;
                }
            }
            levels[levelIndex] = level;
        }

        //Init troll level.
        trollLevel = new boolean[Board.WIDTH][Board.WIDTH];
        trollLevel[Board.WIDTH / 2][Board.WIDTH / 2] = true;

        //Init random & sandbox level placeholders.
        randomLevelPlaceholder = new boolean[][]{
                {false, true, true, true, false},
                {false, true, false, true, false},
                {false, true, true, true, false},
                {false, true, true, false, false},
                {false, true, false, true, false}
        };
        sandboxLevelPlaceholder = new boolean[5][5];

        //Init user record.
        records = new int[levels.length];
        if (!Gdx.files.isLocalStorageAvailable()) {
            return;
        }
        FileHandle recordsFile = Gdx.files.local(USER_RECORD_FILEPATH);
        if (!recordsFile.exists()) {
            return;
        }
        try {
            JsonValue recordJson = new JsonReader().parse(recordsFile);
            records = recordJson.asIntArray();
            if (records.length != levels.length) {
                records = new int[levels.length];
            }
        } catch (RuntimeException e) {
            Gdx.app.error(LOG_TAG, "Error parsing user record data as JSON.");
        }
    }

    private static String[][] loadLevelData() {
        //Load level data from file.
        FileHandle levelDataFile = Gdx.files.internal(LEVEL_DATA_FILEPATH);
        if (!levelDataFile.exists()) {
            Gdx.app.error(LOG_TAG, "Error loading level data.");
            return null;
        }
        String[] levelStrings = levelDataFile.readString().split("(\r|\n)+-----(\r|\n)*");
        String[][] levelData = new String[levelStrings.length + 1][];

        //Create blank level at index 0;
        levelData[0] = new String[]{"", "", "", "", ""};

        //Extract level data info.
        for (int i = 0; i < levelStrings.length; i++) {
            levelData[i + 1] = levelStrings[i].split("\n");
        }

        return levelData;
    }

    public static void saveRecords() {
        if (!Gdx.files.isLocalStorageAvailable()) {
            return;
        }
        FileHandle recordsFile = Gdx.files.local(USER_RECORD_FILEPATH);
        try {
            new Json().toJson(records, recordsFile);
        } catch (RuntimeException e) {
            Gdx.app.error(LOG_TAG, "Error writing record data to file.");
        }
    }

    public static int getHighestLevelIndex() {
        return levels != null ? levels.length - 1 : -1;
    }

    public static int getTrollLevelIndex() {
        return levels != null ? levels.length : -1;
    }

    public static int getRandomizedLevelIndex() {
        return levels != null ? levels.length + 2 : -1;
    }

    public static int getSandboxLevelIndex() {
        return levels != null ? levels.length + 1 : -1;
    }

    public static boolean[][] getLevel(int i) {
        if (i >= 0 && i <= getHighestLevelIndex()) {
            return levels[i];
        } else if (i == getTrollLevelIndex()) {
            return trollLevel;
        } else if (i == getSandboxLevelIndex()) {
            return sandboxLevelPlaceholder;
        } else if (i == getRandomizedLevelIndex()) {
            return randomLevelPlaceholder;
        } else {
            return null;
        }
    }
}
