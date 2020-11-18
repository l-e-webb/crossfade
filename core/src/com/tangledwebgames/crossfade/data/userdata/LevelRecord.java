package com.tangledwebgames.crossfade.data.userdata;

import com.badlogic.gdx.utils.TimeUtils;
import com.tangledwebgames.crossfade.CrossFadeGame;

public class LevelRecord {

    public String versionCode;
    public Long timeStamp;
    public int level;
    public int moves;

    public LevelRecord() {}

    public LevelRecord(int level, int moves) {
        versionCode = CrossFadeGame.VERSION;
        timeStamp = TimeUtils.millis();
        this.level = level;
        this.moves = moves;
    }
}
