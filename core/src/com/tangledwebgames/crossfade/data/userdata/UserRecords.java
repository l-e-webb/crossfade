package com.tangledwebgames.crossfade.data.userdata;

import com.tangledwebgames.crossfade.auth.AuthManager;

import java.util.HashMap;
import java.util.Map;

public class UserRecords {

    public String userId;
    public Map<String, LevelRecord> records = new HashMap<>();

    public UserRecords() {
        this(AuthManager.ANONYMOUS_USER_ID);
    }

    public UserRecords(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserRecords) {
            UserRecords other = (UserRecords) obj;
            return other.userId.equals(userId) && records.equals(other.records);
        } else {
            return false;
        }
    }
}
