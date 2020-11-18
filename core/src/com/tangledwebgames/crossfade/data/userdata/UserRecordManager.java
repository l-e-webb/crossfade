package com.tangledwebgames.crossfade.data.userdata;

import com.badlogic.gdx.Gdx;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;

import java.util.HashSet;
import java.util.Set;

import jdk.internal.jline.internal.Nullable;

public abstract class UserRecordManager implements AuthChangeListener {

    private static final String LOG_TAG = UserRecordManager.class.getSimpleName();

    protected UserRecords userRecords;
    private final Set<RecordChangeListener> recordChangeListeners = new HashSet<>();

    @Override
    public void onSignIn() {
        refreshRecords();
    }

    @Override
    public void onSignOut() {
        refreshRecords();
    }

    @Override
    public void onAnonymousSignIn() {
        refreshRecords();
    }

    public abstract void refreshRecords();
    public abstract void saveRecord(LevelRecord record);

    public void saveRecord(int level, int moves) {
        saveRecord(new LevelRecord(level, moves));
    }

    public void initialize() {
        CrossFadeGame.game.authManager.addChangeListener(this);
        userRecords = new UserRecords(CrossFadeGame.game.authManager.getUserId());
    }

    public UserRecords getUserRecords() {
        return userRecords;
    }

    @Nullable
    public LevelRecord getRecord(int level) {
        return userRecords.records.get(level + "");
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
        userRecords.records.put(record.level + "", record);
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
        UserRecords updateNewRecords = RecordUpdater.updateRecord(newRecords);
        if (updateNewRecords != newRecords) {
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
        } else {
            // Replace existing records with new user's records.
            userRecords = newRecords;
            recordsDifferent = true;
        }

        if (recordsDifferent) {
            notifyRecordChangeListeners();
        }
        return recordsDifferent;
    }

    public void addRecordChangeListener(RecordChangeListener listener) {
        recordChangeListeners.add(listener);
    }

    public void removeRecordChangeListener(RecordChangeListener listener) {
        recordChangeListeners.remove(listener);
    }

    protected void notifyRecordChangeListeners() {
        for (RecordChangeListener listener : recordChangeListeners) {
            listener.onRecordChange();
        }
    }

}
