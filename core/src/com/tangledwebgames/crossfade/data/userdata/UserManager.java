package com.tangledwebgames.crossfade.data.userdata;

import com.badlogic.gdx.Gdx;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;

import java.util.HashSet;
import java.util.Set;

import jdk.internal.jline.internal.Nullable;

public abstract class UserManager implements AuthChangeListener {

    private static final String LOG_TAG = UserManager.class.getSimpleName();

    protected static String getLevelKey(int level) {
        return "L_" + level;
    }

    protected UserRecords userRecords;
    private final Set<UserDataChangeListener> userDataChangeListeners = new HashSet<>();

    @Override
    public void onSignIn() {
        refreshUser();
    }

    @Override
    public void onSignOut() {
        refreshUser();
    }

    @Override
    public void onAnonymousSignIn() {
        refreshUser();
    }

    public abstract void refreshUser();
    public abstract void saveRecord(LevelRecord record);
    public abstract void saveHasFullVersion(boolean hasFullVersion);

    public void saveRecord(int level, int moves) {
        saveRecord(new LevelRecord(level, moves));
    }

    public void initialize() {
        userRecords = new UserRecords(CrossFadeGame.game.authManager.getUserId());
        CrossFadeGame.game.authManager.addChangeListener(this);
    }

    public UserRecords getUserRecords() {
        return userRecords;
    }

    public boolean hasFullVersion() {
        return userRecords.hasFullVersion;
    }

    protected boolean checkUpdateFullVersion(boolean hasFullVersion) {
        if (userRecords.hasFullVersion != hasFullVersion) {
            userRecords.hasFullVersion = hasFullVersion;
            return true;
        }
        return false;
    }

    @Nullable
    public LevelRecord getRecord(int level) {
        return userRecords.records.get(getLevelKey(level));
    }

    public int getRecordMoves(int level) {
        LevelRecord record = getRecord(level);
        return (record != null) ? record.moves : -1;
    }

    protected void addRecord(LevelRecord record) {
        LevelRecord priorRecord = getRecord(record.level);
        if (priorRecord != null && priorRecord.moves < record.moves) {
            Gdx.app.log(LOG_TAG, "Overwriting record with fewer moves.");
        }
        userRecords.records.put(getLevelKey(record.level), record);
    }

    public boolean isRecord(int level, int moves) {
        LevelRecord record = getRecord(level);
        return record == null || record.moves > moves;
    }

    public boolean hasBeatenLevel(int level) {
        return getRecord(level) != null;
    }

    protected boolean consolidateRecords(UserRecords newRecords) {
        boolean recordsDifferent = false;
        boolean fullVersionDifferent = false;
        UserRecords updateNewRecords = RecordUpdater.updateRecord(newRecords);
        if (!updateNewRecords.equals(newRecords)) {
            recordsDifferent = true;
            newRecords = updateNewRecords;
        }

        if (userRecords.userId.equals(newRecords.userId)) {
            // Combine existing and new records.
            for (LevelRecord record : newRecords.records.values()) {
                if (isRecord(record.level, record.moves)) {
                    addRecord(record);
                    recordsDifferent = true;
                }
            }
            if (userRecords.records.size() > newRecords.records.size()) {
                recordsDifferent = true;
            }
            if (userRecords.hasFullVersion != newRecords.hasFullVersion) {
                userRecords.hasFullVersion = newRecords.hasFullVersion;
                fullVersionDifferent = true;
            }
        } else {
            // Replace existing records with new user's records.
            userRecords = newRecords;
            recordsDifferent = true;
            fullVersionDifferent = true;
        }

        if (recordsDifferent) {
            notifyRecordChangeListeners();
        }
        if (fullVersionDifferent) {
            notifyFullVersionChangeListeners();
        }
        return recordsDifferent || fullVersionDifferent;
    }

    public void addUserDataChangeListener(UserDataChangeListener listener) {
        userDataChangeListeners.add(listener);
    }

    public void removeUserDataChangeListener(UserDataChangeListener listener) {
        userDataChangeListeners.remove(listener);
    }

    protected void notifyFullVersionChangeListeners() {
        for (UserDataChangeListener listener : userDataChangeListeners) {
            listener.onFullVersionChange();
        }
    }

    protected void notifyRecordChangeListeners() {
        for (UserDataChangeListener listener : userDataChangeListeners) {
            listener.onRecordChange();
        }
    }
}
