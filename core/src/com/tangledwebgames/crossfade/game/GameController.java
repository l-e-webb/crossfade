package com.tangledwebgames.crossfade.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.SettingsManager;

public abstract class GameController extends Stage implements GameState {

    boolean active;
    private int level;
    private float time;
    protected boolean animateTiles;
    protected boolean highlightTiles;
    protected WinListener winListener = null;

    GameController(Viewport viewport) {
        super(viewport);
        animateTiles = SettingsManager.isAnimateTiles();
        highlightTiles = SettingsManager.isHighlightTiles();
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

    public abstract int getMoves();

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

    public abstract void clearActiveTiles();

    public abstract void updateSize();

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAnimateTiles(boolean animateTiles) {
        this.animateTiles = animateTiles;
    }

    public void setHighlightTiles(boolean highlightTiles) {
        this.highlightTiles = highlightTiles;
    }

    public void setWinListener(WinListener winListener) {
        this.winListener = winListener;
    }

    public void reset() {
        time = 0f;
        resetBoard();
    }

    protected abstract void resetBoard();

}
