package com.tangledwebgames.crossfade.data.savedgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;

public class SavedGameManager {

    private static final String LOG_TAG = SavedGameManager.class.getSimpleName();

    private static final String SAVED_GAME_FILEPATH = "saved_game.json";

    private static SavedGameState savedGameState;

    public static SavedGameState getSavedGameState() {
        return savedGameState;
    }

    public static void saveGameState(SavedGameState savedGameState) {
        SavedGameManager.savedGameState = savedGameState;
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

    public static void loadSavedGameState() {
        savedGameState = null;
        if (!Gdx.files.isLocalStorageAvailable()) {
            return;
        }
        FileHandle savedGameFile = Gdx.files.local(SAVED_GAME_FILEPATH);
        if (!savedGameFile.exists()) {
            return;
        }
        try {
            savedGameState = new Json().fromJson(SavedGameState.class, savedGameFile);
        } catch (SerializationException e) {
            Gdx.app.error(LOG_TAG, "Error deserializing saved game state from JSON.", e);
        } catch (Exception e) {
            Gdx.app.error(LOG_TAG, "Error loading saved game state.", e);
        }
    }
}
