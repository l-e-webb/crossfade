package com.tangledwebgames.crossfade.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.game.SavedGameState;

/**
 * Loads and saves level and record data.
 */
public class GameDataLoader {

    private static final String LOG_TAG = GameDataLoader.class.getSimpleName();

    private static final String LEVEL_DATA_FILEPATH = "levels.dat";
    private static final String USER_RECORD_FILEPATH = "records.json";
    private static final String SAVED_GAME_FILEPATH = "saved_game.json";
    private static final char LIGHT_MARKER = '#';

    private static final GameDataManager gameDataManager = new GdxGameDataManager();

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

    public static int[] loadRecords() {
        return gameDataManager.loadRecords();
    }

    public static void saveRecords() {
        gameDataManager.saveRecords();
    }

    public static SavedGameState loadSavedGameState() {
        return gameDataManager.loadSavedGameState();
    }

    public static void saveGameState(SavedGameState savedGameState) {
        gameDataManager.saveGameState(savedGameState);
    }

    private static class GdxGameDataManager implements GameDataManager {

        public int[] loadRecords() {
            //Init user record.
            int[] records = new int[Levels.levels.length];
            if (!Gdx.files.isLocalStorageAvailable()) {
                return records;
            }
            FileHandle recordsFile = Gdx.files.local(USER_RECORD_FILEPATH);
            if (!recordsFile.exists()) {
                return records;
            }
            try {
                JsonValue recordJson = new JsonReader().parse(recordsFile);
                int[] recordsFromJson = recordJson.asIntArray();
                if (recordsFromJson != null
                        && recordsFromJson.length == Levels.levels.length) {
                    records = recordsFromJson;
                }
            } catch (SerializationException e) {
                Gdx.app.error(LOG_TAG, "Error deserializing record data JSON.", e);
            } catch (Exception e) {
                Gdx.app.error(LOG_TAG, "Error loading record data.", e);
            }
            return records;
        }

        public void saveRecords() {
            if (!Gdx.files.isLocalStorageAvailable()) {
                return;
            }
            FileHandle recordsFile = Gdx.files.local(USER_RECORD_FILEPATH);
            try {
                new Json().toJson(Levels.records, recordsFile);
            } catch (SerializationException e) {
                Gdx.app.error(LOG_TAG, "Error serializing record data as JSON.", e);
            } catch (RuntimeException e) {
                Gdx.app.error(LOG_TAG, "Error writing record data to file.", e);
            }
        }

        public SavedGameState loadSavedGameState() {
            if (!Gdx.files.isLocalStorageAvailable()) {
                return null;
            }
            FileHandle savedGameFile = Gdx.files.local(SAVED_GAME_FILEPATH);
            if (!savedGameFile.exists()) {
                return null;
            }
            try {
                return new Json().fromJson(SavedGameState.class, savedGameFile);
            } catch (SerializationException e) {
                Gdx.app.error(LOG_TAG, "Error deserializing saved game state from JSON.", e);
            } catch (Exception e) {
                Gdx.app.error(LOG_TAG, "Error loading saved game state.", e);
            }
            return null;
        }

        public void saveGameState(SavedGameState savedGameState) {
            if (!Gdx.files.isLocalStorageAvailable()) {
                return;
            }
            FileHandle savedGameFile = Gdx.files.local(SAVED_GAME_FILEPATH);
            try {
                new Json().toJson(savedGameState, SavedGameState.class, savedGameFile);
            } catch (SerializationException e) {
                Gdx.app.error(LOG_TAG, "Error serializing saved game data", e);
            } catch (Exception e) {
                Gdx.app.error(LOG_TAG, "Error saving game state", e);
            }
        }
    }

}
