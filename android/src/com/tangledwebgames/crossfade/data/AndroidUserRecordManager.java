package com.tangledwebgames.crossfade.data;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.data.userdata.LevelRecord;
import com.tangledwebgames.crossfade.data.userdata.UserRecordManager;
import com.tangledwebgames.crossfade.data.userdata.UserRecords;

public class AndroidUserRecordManager extends UserRecordManager implements ValueEventListener {

    private static final String LOG_TAG = AndroidUserRecordManager.class.getSimpleName();

    private static final String USER_DATA_KEY = "users";
    private static final String RECORDS_KEY = "records";

    private DatabaseReference userDataRef;

    @Override
    public void saveRecord(LevelRecord record) {
        Gdx.app.log(LOG_TAG, "Saving record.");
        addRecord(record);
        if (userDataRef != null) {
            Gdx.app.log(LOG_TAG, "Updating record in database.");
            userDataRef.child(RECORDS_KEY).child(getLevelKey(record.level)).setValue(record);
        }
    }

    @Override
    public void refreshRecords() {
        Gdx.app.log(LOG_TAG, "Refreshing records.");
        unplugDataRef();
        FirebaseUser user = getFirebaseUser();

        // If user is anonymous
        if (user == null || user.isAnonymous()) {
            Gdx.app.log(LOG_TAG, "Refreshing for anonymous user.");
             if (userRecords == null || !userRecords.userId.equals(AuthManager.ANONYMOUS_USER_ID)) {
                 userRecords = new UserRecords(AuthManager.ANONYMOUS_USER_ID);
                 notifyRecordChangeListeners();
             }
             return;
        }


        Gdx.app.log(LOG_TAG, "Refreshing record for user: " + user.getUid());
        if (!userRecords.userId.equals(user.getUid())) {
            Gdx.app.log(LOG_TAG, "Removing prior user record in refresh.");
            userRecords = new UserRecords(user.getUid());
            notifyRecordChangeListeners();
        }
        userDataRef = getUserDataReference(user);
        userDataRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Gdx.app.log(LOG_TAG, "onDataChange callback fired, data key: " + snapshot.getKey());
        FirebaseUser currentUser = getFirebaseUser();
        if (!currentUser.getUid().equals(snapshot.getKey())) {
            // Data change is not for current user.
            Gdx.app.log(LOG_TAG, "Received data update for non current user. Current user ID: " + currentUser.getUid() + " ; update user ID: " + snapshot.getKey());
            refreshRecords();
            return;
        }

        UserRecords fetchedRecords = null;
        try {
            fetchedRecords = snapshot.getValue(UserRecords.class);
        } catch (Exception e) {
            Gdx.app.error(LOG_TAG, "Error parsing user records", e);
        }
        if (fetchedRecords == null) {
            initializeNewUserData(currentUser);
        } else if (consolidateRecords(fetchedRecords)) {
            // Update database if consolidation causes changes.
            Gdx.app.debug(LOG_TAG, "Updating database after consolidation.");
            userDataRef.setValue(userRecords);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Gdx.app.error(LOG_TAG, "Database error fetching user data: " + error.getMessage());
    }

    private void initializeNewUserData(FirebaseUser user) {
        Gdx.app.debug(LOG_TAG, "Initializing for new user: " + user.getUid());
        userRecords = new UserRecords(user.getUid());
        unplugDataRef();
        userDataRef = getUserDataReference(user);
        userDataRef.setValue(userRecords);
        userDataRef.addValueEventListener(this);
    }

    private void unplugDataRef() {
        if (userDataRef != null) {
            userDataRef.removeEventListener(this);
            userDataRef = null;
        }
    }

    private DatabaseReference getDataBase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    private FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private DatabaseReference getUserDataReference(FirebaseUser user) {
        return getDataBase().child(USER_DATA_KEY).child(user.getUid());
    }
}
