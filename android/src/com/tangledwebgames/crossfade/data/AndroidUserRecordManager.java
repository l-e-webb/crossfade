package com.tangledwebgames.crossfade.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tangledwebgames.crossfade.auth.AuthChangeListener;
import com.tangledwebgames.crossfade.data.savedgame.SavedGameState;
import com.tangledwebgames.crossfade.data.userdata.LevelRecord;
import com.tangledwebgames.crossfade.data.userdata.UserRecordManager;

import androidx.annotation.NonNull;

public class AndroidUserRecordManager extends UserRecordManager implements ValueEventListener {

    private static final String USER_DATA_KEY = "userData";
    private static final String RECORDS_KEY = "records";

    private DatabaseReference userDataRef;

    @Override
    public void saveRecord(LevelRecord record) {
        // TODO
    }

    @Override
    public void refreshRecords() {
        if (userDataRef != null) {
            userDataRef.removeEventListener(this);
            userDataRef = null;
        }
        FirebaseUser user = getFirebaseUser();
        if (user == null) { return; }
        userDataRef = getUserDataReference(user);
        userDataRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        // TODO
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // TODO
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
