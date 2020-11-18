package com.tangledwebgames.crossfade.game;

import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.data.LevelLoader;

/**
 * Stores level data, has only static members.
 */
public class Levels {

    public static final int MAX_FREE_LEVEL = 25;

    public static boolean[][][] levels;
    public static boolean[][] trollLevel;
    public static boolean[][] randomLevelPlaceholder;
    public static boolean[][] sandboxLevelPlaceholder;

    private Levels() {
    }

    public static void init() {
        LevelLoader.loadLevelData();
    }

    public static int getHighestLevelIndex() {
        return levels != null ? levels.length - 1 : -1;
    }

    public static int getTrollLevelIndex() {
        return levels != null ? levels.length : -1;
    }

    public static int getRandomizedLevelIndex() {
        return levels != null ? levels.length + 2 : -1;
    }

    public static int getSandboxLevelIndex() {
        return levels != null ? levels.length + 1 : -1;
    }

    public static boolean[][] getLevel(int i) {
        if (i >= 0 && i <= getHighestLevelIndex()) {
            return levels[i];
        } else if (i == getTrollLevelIndex()) {
            return trollLevel;
        } else if (i == getSandboxLevelIndex()) {
            return sandboxLevelPlaceholder;
        } else if (i == getRandomizedLevelIndex()) {
            return randomLevelPlaceholder;
        } else {
            return null;
        }
    }
}
