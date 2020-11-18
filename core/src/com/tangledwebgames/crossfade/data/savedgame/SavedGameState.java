package com.tangledwebgames.crossfade.data.savedgame;

import com.badlogic.gdx.utils.TimeUtils;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.game.GameState;

public class SavedGameState implements GameState {

    public String version;
    public String userId;
    public long timeStamp;
    public int time;
    public int moves;
    public int level;
    public boolean[][] boardState;

    public SavedGameState() {
        version = "";
        userId = "";
        timeStamp = 0L;
        time = 0;
        moves = 0;
        level = 0;
        boardState = new boolean[0][0];
    }

    public SavedGameState(GameState gameState) {
        this(
                gameState.getTime(),
                gameState.getMoves(),
                gameState.getLevel(),
                gameState.getBoardState()
        );
    }

    public SavedGameState(int time, int moves, int level, boolean[][] boardState) {
        version = CrossFadeGame.VERSION;
        userId = CrossFadeGame.game.authManager.getUserId();
        this.timeStamp = TimeUtils.millis();
        this.time = time;
        this.moves = moves;
        this.level = level;
        this.boardState = boardState;
    }

    public String getVersion() { return version; }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public int getMoves() {
        return moves;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public boolean[][] getBoardState() {
        return boardState;
    }

    @Override
    public boolean isWinningState() {
        for (boolean[] row : boardState) {
            for (boolean tileValue : row) {
                if (tileValue) return false;
            }
        }
        return true;
    }
}
