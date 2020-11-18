package com.tangledwebgames.crossfade.data.userdata;

import java.util.HashMap;
import java.util.Map;

public class UserRecords {

    public String userId;
    public Map<String, LevelRecord> records;

    public UserRecords() {}

    public UserRecords(String userId) {
        this.userId = userId;
        records = new HashMap<>();
    }
}
