package com.tangledwebgames.crossfade;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tangledwebgames.crossfade.data.GameDataManager;
import com.tangledwebgames.crossfade.game.SavedGameState;

import androidx.annotation.NonNull;

public class AndroidGameDataManager implements GameDataManager {

    private DatabaseReference userRecordsRef;
    private int[] userRecords = new int[50];

    private DatabaseReference savedGameStateRef;
    private SavedGameState savedGameState;

    @Override
    public int[] loadRecords() {
        return userRecords;
    }

    @Override
    public void saveRecords() {
        userRecordsRef.setValue(userRecords);
    }

    @Override
    public SavedGameState loadSavedGameState() {
        return null;
    }

    @Override
    public void saveGameState(SavedGameState savedGameState) {

    }

    private void getCurrentUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getDataBase().child("userData").child(user.getUid());
        }
    }

    private DatabaseReference getDataBase() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
