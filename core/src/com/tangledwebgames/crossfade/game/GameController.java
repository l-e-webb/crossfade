package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameController extends Stage implements GameState {

    boolean active;
    private int level;
    private float time;
    protected int moves;
    protected WinListener winListener = null;

    GameController(Viewport viewport) {
        super(viewport);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (active) {
            time += delta;
        }
    }

    public int getTime() {
        return (int) time;
    }

    public int getLevel() {
        return level;
    }

    public int getMoves() {
        return moves;
    }

    public void goToLevel(int level) {
        if (level >= Levels.getRandomizedLevelIndex()) {
            //Randomized level
            level = Levels.getRandomizedLevelIndex();
            makeRandomLevel(true);
        } else if (level == Levels.getSandboxLevelIndex()) {
            //Sandbox level
            initializeLevel(Levels.sandboxLevelPlaceholder);
        } else if (level == Levels.getTrollLevelIndex()) {
            //Troll level
            initializeLevel(Levels.trollLevel);
        } else if (level <= Levels.getHighestLevelIndex() && level > 0) {
            //Normal level
            initializeLevel(Levels.levels[level]);
        } else { // if level <= 0
            level = 1;
            initializeLevel(Levels.levels[level]);
        }
        this.level = level;
        time = 0f;
    }

    protected abstract void makeRandomLevel(boolean solvable);

    protected abstract void initializeLevel(boolean[][] level);

    protected abstract void setTileValues(boolean[][] level);

    public abstract void clearActiveTiles();

    public abstract void updateSize();

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setWinListener(WinListener winListener) {
        this.winListener = winListener;
    }

    public void reset() {
        time = 0f;
        moves = 0;
        resetBoard();
    }

    protected abstract void resetBoard();

    public SavedGameState getSavedGameState() {
        return new SavedGameState(TimeUtils.millis(), this);
    }

    public void setToGameState(GameState state) {
        goToLevel(state.getLevel());
        if (state.getLevel() == Levels.getRandomizedLevelIndex()) {
            // Cannot resume state of a random level. In this case, a new random level is made.
            return;
        }
        setTileValues(state.getBoardState());
        time = state.getTime();
        moves = state.getMoves();
    }

}
