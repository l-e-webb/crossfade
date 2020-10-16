package com.tangledwebgames.crossfade.data;

import com.tangledwebgames.crossfade.game.SavedGameState;

public interface GameDataManager {

    int[] loadRecords();
    void saveRecords();
    SavedGameState loadSavedGameState();
    void saveGameState(SavedGameState savedGameState);
}
