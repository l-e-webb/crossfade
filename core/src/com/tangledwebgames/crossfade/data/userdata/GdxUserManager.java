package com.tangledwebgames.crossfade.data.userdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.tangledwebgames.crossfade.CrossFadeGame;

/**
 * Generic user record manager using libGDX local storage, which has existing implementations each
 * distribution platform. Only persists records from the most recent logged in user. Saving records
 * for a new user will erase prior ones.
 */
public class GdxUserManager extends UserManager {

    private static final String LOG_TAG = GdxUserManager.class.getSimpleName();

    private static final String USER_RECORD_FILEPATH = "records.json";

    @Override
    public void saveRecord(LevelRecord record) {
        addRecord(record);
        saveRecords();
        notifyRecordChangeListeners();
    }

    @Override
    public void saveHasFullVersion(boolean hasFullVersion) {
        if (checkUpdateFullVersion(hasFullVersion)) {
            saveRecords();
            notifyFullVersionChangeListeners();
        }
    }

    @Override
    public void refreshUser() {
        Gdx.app.log(LOG_TAG, "Refreshing user data.");
        String userId = CrossFadeGame.game.authManager.getUserId();

        if (!userRecords.userId.equals(userId)) {
            // Save prior user's records.
            saveRecords();
        }

        UserRecords newRecords = loadRecords(userId);
        if (consolidateRecords(newRecords)) {
            Gdx.app.log(LOG_TAG, "Updating records after consolidation");
            saveRecords();
        }
    }

    private UserRecords loadRecords(String userId) {
        UserRecords records = new UserRecords(userId);
        if (!Gdx.files.isLocalStorageAvailable()) {
            Gdx.app.log(LOG_TAG, "Creating empty user records due to absence of local storage.");
            return records;
        }

        String filePath = userId + "_" + USER_RECORD_FILEPATH;
        FileHandle recordsFile = Gdx.files.local(filePath);
        if (!recordsFile.exists()) {
            Gdx.app.log(LOG_TAG, "Creating empty user records since none found in local storage.");
            return records;
        }

        try {
            records = new Json().fromJson(UserRecords.class, recordsFile);
            Gdx.app.log(LOG_TAG, "Successfully loaded user records from local storage.");
        } catch (SerializationException e) {
            Gdx.app.error(LOG_TAG, "Error deserializing user records JSON.", e);
        } catch (Exception e) {
            Gdx.app.error(LOG_TAG, "Error loading user records from local storage.", e);
        }

        return records;
    }

    private void saveRecords() {
        if (!Gdx.files.isLocalStorageAvailable()) {
            Gdx.app.log(LOG_TAG, "Skipping saving user records due to absence of local storage.");
            return;
        }
        FileHandle recordsFile = Gdx.files.local(userRecords.userId + "_" + USER_RECORD_FILEPATH);
        UserRecords recordsToSave = userRecords.duplicate();
        // Full version state should not be saved locally. If this user has the full version, it
        // will be automatically restored when their records are loaded.
        recordsToSave.hasFullVersion = false;
        try {
            new Json().toJson(recordsToSave, recordsFile);
            Gdx.app.log(LOG_TAG, "Successfully saved user records to local storage.");
        } catch (SerializationException e) {
            Gdx.app.error(LOG_TAG, "Error serializing user records as JSON.", e);
        } catch (RuntimeException e) {
            Gdx.app.error(LOG_TAG, "Error writing user records to local storage.", e);
        }
    }
}
