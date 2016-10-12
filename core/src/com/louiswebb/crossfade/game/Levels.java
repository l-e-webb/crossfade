package com.louiswebb.crossfade.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Loads and stores level data, has only static members.
 */
public class Levels {

    public static final String LOG_TAG = Levels.class.getSimpleName();

    public static boolean[][][] levels;

    public static boolean[][] trollLevel;

    private static final String LEVEL_DATA_FILEPATH = "levels.dat";

    private static final char LIGHT_MARKER = '#';

    private Levels() {}

    public static void init() {
        //Get level strings from file.
        String[][] levelData = getLevelData();
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
    }

    private static String[][] getLevelData() {
        //Load level data from file.
        FileHandle levelDataFile = Gdx.files.internal(LEVEL_DATA_FILEPATH);
        if (!levelDataFile.exists()) {
            Gdx.app.log(LOG_TAG, "Error loading level data.");
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

    public static int getHighestLevelIndex() {
        return levels != null ? levels.length - 1 : -1;
    }

    public static int getTrollLevelIndex() {
        return levels != null ? levels.length : -1;
    }

    public static int getRandomizedLevelIndex() {
        return levels != null ? levels.length + 1 : -1;
    }
}
