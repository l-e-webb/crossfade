package com.tangledwebgames.crossfade.data.userdata;

class RecordUpdater {

    /**
     * Updates user records to match the levels of the current version.
     * @param records UserRecords to be updated.
     * @return Updated UserRecords, now consistent with current version.
     */
    static UserRecords updateRecord(UserRecords records) {
        // No version update has changed levels yet.
        return records;
    }
}
