package com.tangledwebgames.crossfade.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.game.Levels;

/**
 * Loads level data from local asset.
 */
public class LevelLoader {

    private static final String LOG_TAG = LevelLoader.class.getSimpleName();

    private static final String LEVEL_DATA_FILEPATH = "levels.dat";
    private static final char LIGHT_MARKER = '#';

    public static void loadLevelData() {
        String[][] levelData = loadLevelStrings();
        if (levelData == null) return;

        //Init levels array.
        boolean[][][] levels = new boolean[levelData.length][][];
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
        boolean[][] trollLevel = new boolean[Board.WIDTH][Board.WIDTH];
        trollLevel[Board.WIDTH / 2][Board.WIDTH / 2] = true;

        //Init random & sandbox level placeholders.
        boolean[][] randomLevelPlaceholder = new boolean[][]{
                {false, true, true, true, false},
                {false, true, false, true, false},
                {false, true, true, true, false},
                {false, true, true, false, false},
                {false, true, false, true, false}
        };
        boolean[][] sandboxLevelPlaceholder = new boolean[5][5];

        Levels.levels = levels;
        Levels.trollLevel = trollLevel;
        Levels.randomLevelPlaceholder = randomLevelPlaceholder;
        Levels.sandboxLevelPlaceholder = sandboxLevelPlaceholder;
    }

    private static String[][] loadLevelStrings() {
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

}
