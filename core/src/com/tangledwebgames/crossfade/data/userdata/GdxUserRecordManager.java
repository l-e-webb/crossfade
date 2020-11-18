package com.tangledwebgames.crossfade.data.userdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.auth.SignInListener;

/**
 * Generic user record manager using libGDX local storage, which has existing implementations each
 * distribution platform. Only persists records from the most recent logged in user. Saving records
 * for a new user will erase prior ones.
 */
public class GdxUserRecordManager extends UserRecordManager {

    private static final String LOG_TAG = GdxUserRecordManager.class.getSimpleName();

    private static final String USER_RECORD_FILEPATH = "records.json";

    @Override
    public void saveRecord(LevelRecord record) {
        addRecord(record);
        saveRecords();
        notifyRecordChangeListeners();
    }

    @Override
    public void refreshRecords() {
        String userId = CrossFadeGame.game.authManager.getUserId();
        String filePath = userId + "_" + USER_RECORD_FILEPATH;
        userRecords = new UserRecords(userId);
        if (!Gdx.files.isLocalStorageAvailable()) {
            return;
        }
        FileHandle recordsFile = Gdx.files.local(filePath);
        if (!recordsFile.exists()) {
            return;
        }
        try {
            userRecords = new Json().fromJson(UserRecords.class, recordsFile);
        } catch (SerializationException e) {
            Gdx.app.error(LOG_TAG, "Error deserializing record data JSON.", e);
        } catch (Exception e) {
            Gdx.app.error(LOG_TAG, "Error loading record data.", e);
        }
    }

    private void saveRecords() {
        if (!Gdx.files.isLocalStorageAvailable()) {
            return;
        }
        FileHandle recordsFile = Gdx.files.local(userRecords.userId + "_" + USER_RECORD_FILEPATH);
        try {
            new Json().toJson(userRecords, recordsFile);
        } catch (SerializationException e) {
            Gdx.app.error(LOG_TAG, "Error serializing record data as JSON.", e);
        } catch (RuntimeException e) {
            Gdx.app.error(LOG_TAG, "Error writing record data to file.", e);
        }
    }
}
