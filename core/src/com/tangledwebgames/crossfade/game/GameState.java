package com.tangledwebgames.crossfade.game;

public interface GameState {

    int getTime();

    int getMoves();

    int getLevel();

    boolean[][] getBoardState();

    boolean isWinningState();

}
