package com.tangledwebgames.crossfade.game;

public class SavedGameState implements GameState {

    private long timeStamp;
    private int time;
    private int moves;
    private int level;
    private boolean[][] boardState;

    SavedGameState() {
        timeStamp = 0L;
        time = 0;
        moves = 0;
        level = 0;
        boardState = new boolean[0][0];
    }

    SavedGameState(long timeStamp, GameState gameState) {
        this(
                timeStamp,
                gameState.getTime(),
                gameState.getMoves(),
                gameState.getLevel(),
                gameState.getBoardState()
        );
    }

    SavedGameState(long timeStamp, int time, int moves, int level, boolean[][] boardState) {
        this.timeStamp = timeStamp;
        this.time = time;
        this.moves = moves;
        this.level = level;
        this.boardState = boardState;
    }

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
