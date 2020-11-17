package com.tangledwebgames.crossfade.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.game.SavedGameState;

/**
 * Generic game data manager which uses libGDX local storage, which has existing implementations
 * for each distribution platform.
 */
public class GdxGameDataManager implements GameDataManager {

    private static final String LOG_TAG = GdxGameDataManager.class.getSimpleName();

    private static final String USER_RECORD_FILEPATH = "records.json";
    private static final String SAVED_GAME_FILEPATH = "saved_game.json";

    @Override
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

    @Override
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

    @Override
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

    @Override
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
