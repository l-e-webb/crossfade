package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.tangledwebgames.crossfade.Assets;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.PreferenceWrapper;
import com.tangledwebgames.crossfade.game.Board;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.sound.SoundManager;

/**
 * Manages all UI elements through a Stage.
 */
public class UIRenderer implements Disposable {

    public static final float BUTTON_PADDING = 10f;
    public static final float CHECKBOX_SIZE = 42f;
    public static final float CHECKBOX_RIGHT_PADDING = 25f;
    public static final float PAUSE_TABLE_WIDTH_RATIO = 0.875f;
    public static final int SLIDER_KNOB_WIDTH = 12;
    public static final int SLIDER_KNOB_HEIGHT = 20;
    public static final int SLIDER_PADDING_LEFT = 10;
    public static final float ROW_PADDING = 20f;

    private MainScreen screen;
    private Stage stage;
    private Table mainUiTable;
    private Table pauseUiTable;
    private Table winUiTable;
    
    private Label timeNum;
    private Label levelNum;
    private Label movesNum;
    private Label winTime;
    private Label winMoves;
    private Label winLevel;
    private TextButton leftButton;
    private TextButton centerButton;
    private TextButton rightButton;
    private CheckBox sfxOn;
    private CheckBox musicOn;
    private Slider sfxVolumeSlider;
    private Slider musicVolumeSlider;
    private CheckBox animateTiles;
    private CheckBox highlightTiles;
    private int time;
    private int moves;

    public UIRenderer() {
        screen = MainScreen.instance;
        stage = new Stage(screen.getViewport());
        time = -1;
        moves = -1;
        initUI();
    }

    public void render() {

        //Update time and move UI elements
        if (time < (int) screen.getTime()) {
            time = (int) screen.getTime();
            timeNum.setText("" + time);
            winTime.setText(UIText.TIME + ": " + time);
        }
        if (moves < screen.getBoard().getMoves()) {
            moves = screen.getBoard().getMoves();
            movesNum.setText("" + moves);
            winMoves.setText(UIText.MOVES + ": " + moves);
        }

        //Update table visibility
        setVisibleTables();

        //Draw main UI layer
        stage.draw();

    }

    private void setVisibleTables() {
        switch (screen.getState()) {
            case WIN:
                winUiTable.setVisible(true);
                pauseUiTable.setVisible(false);
                break;
            case PAUSE:
                winUiTable.setVisible(false);
                pauseUiTable.setVisible(true);
                break;
            case PLAY:
                winUiTable.setVisible(false);
                pauseUiTable.setVisible(false);
                break;
        }
    }

    public void initPause() {
        sfxOn.setChecked(SoundManager.isSfxOn());
        musicOn.setChecked(SoundManager.isMusicOn());
        sfxVolumeSlider.setValue(SoundManager.getSfxVolume());
        musicVolumeSlider.setValue(SoundManager.getMusicVolume());
        animateTiles.setChecked(MainScreen.instance.getBoard().animateTiles);
        highlightTiles.setChecked(MainScreen.instance.getBoard().highlightTiles);
        leftButton.setDisabled(true);
        centerButton.setDisabled(true);
        rightButton.setDisabled(true);
    }

    public void initUnpause() {
        leftButton.setDisabled(false);
        centerButton.setDisabled(false);
        rightButton.setDisabled(false);
    }

    public Stage getStage() { return stage; }

    public void initUI() {

        //Font inits.
        BitmapFont titleFont = Assets.instance.titleFont;
        titleFont.getData().setScale(UIText.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont uiFont = Assets.instance.uiFont;
        uiFont.getData().setScale(UIText.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Style inits.
        Label.LabelStyle labelStyle = new Label.LabelStyle(uiFont, UIText.TEXT_COLOR);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, UIText.TEXT_COLOR);
        NinePatchDrawable baseFilled9Patch = new NinePatchDrawable(
                new NinePatch(Assets.instance.buttonFilled, 13, 13 , 13, 13));
        NinePatchDrawable baseEmpty9Patch = new NinePatchDrawable(
                new NinePatch(Assets.instance.buttonEmpty, 13, 13 , 13, 13));
        NinePatchDrawable baseTransparent9Patch = new NinePatchDrawable(
                new NinePatch(Assets.instance.buttonTransparent, 13, 13, 13,13)
        );
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = baseEmpty9Patch.tint(UIText.BUTTON_COLOR);
        buttonStyle.over = baseFilled9Patch.tint(UIText.ACTIVE_BUTTON_COLOR);
        buttonStyle.fontColor = UIText.TEXT_COLOR;
        buttonStyle.overFontColor = UIText.INVERTED_TEXT_COLOR;
        buttonStyle.font = uiFont;
        TextureRegionDrawable checkboxEmpty = new TextureRegionDrawable(
                Assets.instance.checkboxEmpty
        );
        TextureRegionDrawable checkboxFilled = new TextureRegionDrawable(
                Assets.instance.checkboxFilled
        );
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(
                checkboxEmpty.tint(UIText.TEXT_COLOR),
                checkboxFilled.tint(UIText.TEXT_COLOR),
                uiFont,
                UIText.TEXT_COLOR
        );
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(
                Assets.instance.sliderBackground
        );
        Sprite sliderKnobSprite = new Sprite(Assets.instance.sliderKnob);
        sliderKnobSprite.setSize(SLIDER_KNOB_WIDTH, SLIDER_KNOB_HEIGHT);
        SpriteDrawable sliderKnob = new SpriteDrawable(sliderKnobSprite);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(
                sliderBackground.tint(UIText.DARK_COLOR),
                sliderKnob.tint(UIText.TEXT_COLOR)
        );


        //Skin init.
        Skin skin = new Skin();
        skin.add("titleStyle", titleStyle);
        skin.add("default", labelStyle);
        skin.add("default", buttonStyle);
        skin.add("default", checkBoxStyle);
        skin.add("default-horizontal", sliderStyle);

        //Main UI
        mainUiTable = new Table();
        mainUiTable.setFillParent(true);
        mainUiTable.top();
        mainUiTable.pad(UIText.TEXT_OFFSET, UIText.TEXT_OFFSET, MainScreen.WORLD_WIDTH + UIText.TEXT_OFFSET, UIText.TEXT_OFFSET);
        //This listener handles events whenever the game state is not PLAY, preventing any UI
        //elements in this table from receiving events.
        mainUiTable.addCaptureListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (!screen.inGame()) {
                    event.stop();
                }
                return false;
            }
        });
        //Uncomment to see wireframe.
        //mainUiTable.setDebug(true);
        final Label titleLabel = new Label(UIText.TITLE, skin, "titleStyle");
        final Label timeLabel = new Label(UIText.TIME, skin);
        final Label levelLabel = new Label(UIText.LEVEL, skin);
        final Label movesLabel = new Label(UIText.MOVES, skin);
        timeNum = new Label("0", skin);
        levelNum = new Label("1", skin);
        movesNum = new Label("0", skin);
        leftButton = new TextButton(UIText.PREVIOUS, skin);
        centerButton = new TextButton(UIText.RESET, skin);
        rightButton = new TextButton(UIText.NEXT, skin);
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.onLeftButtonClick();
                SoundManager.buttonSound();
            }
        });
        centerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.onCenterButtonClick();
                SoundManager.buttonSound();
            }
        });
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.onRightButtonClick();
                SoundManager.buttonSound();
            }
        });
        mainUiTable.add(titleLabel).colspan(6).expandY();
        mainUiTable.row();
        mainUiTable.add(timeLabel).uniform().colspan(2);
        mainUiTable.add(levelLabel).uniform().colspan(2);
        mainUiTable.add(movesLabel).uniform().colspan(2);
        mainUiTable.row();
        mainUiTable.add(timeNum).uniform().colspan(2);
        mainUiTable.add(levelNum).uniform().colspan(2);
        mainUiTable.add(movesNum).uniform().colspan(2);
        mainUiTable.row();
        mainUiTable.add(leftButton).expandY().fillX().colspan(2).pad(8);
        mainUiTable.add(centerButton).expandY().fillX().colspan(2).pad(8);
        mainUiTable.add(rightButton).expandY().fillX().colspan(2).pad(8);

        //Pause Screen
        pauseUiTable = new Table().background(baseTransparent9Patch.tint(UIText.PAUSE_BOX_COLOR));
        //Uncomment to see wireframe.
        //pauseUiTable.setDebug(true);
        final Label pauseLabel = new Label(UIText.PAUSED, skin);
        sfxOn = new CheckBox(UIText.SFX, skin);
        sfxOn.getImageCell().maxSize(CHECKBOX_SIZE).spaceRight(CHECKBOX_RIGHT_PADDING);
        sfxOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setSfx(sfxOn.isChecked());
                if (screen.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        final Label sfxLevelLabel = new Label(UIText.SFX_LEVEL, skin);
        sfxVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setSfxVolume(sfxVolumeSlider.getValue());
            }
        });
        musicOn = new CheckBox(UIText.MUSIC, skin);
        musicOn.getImageCell().maxSize(CHECKBOX_SIZE).spaceRight(CHECKBOX_RIGHT_PADDING);
        musicOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setMusic(musicOn.isChecked());
                if (screen.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        final Label musicLevelLabel = new Label(UIText.MUSIC_LEVEL, skin);
        musicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.setMusicVolume(musicVolumeSlider.getValue());
            }
        });
        animateTiles = new CheckBox(UIText.ANIMATE_TILES, skin);
        animateTiles.getImageCell().maxSize(CHECKBOX_SIZE).spaceRight(CHECKBOX_RIGHT_PADDING);
        animateTiles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.getBoard().animateTiles = animateTiles.isChecked();
                if (screen.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        highlightTiles = new CheckBox(UIText.HIGHLIGHT_TILES, skin);
        highlightTiles.getImage().setSize(CHECKBOX_SIZE, CHECKBOX_SIZE);
        highlightTiles.getImageCell().maxSize(CHECKBOX_SIZE).spaceRight(CHECKBOX_RIGHT_PADDING);
        highlightTiles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.getBoard().highlightTiles = highlightTiles.isChecked();
                if (screen.getState() == MainScreen.State.PAUSE) {
                    SoundManager.buttonSound();
                }
            }
        });
        final Button pauseContinueButton = new TextButton(UIText.CONTINUE, skin);
        pauseContinueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.unpauseGame();
                SoundManager.buttonSound();
            }
        });
        pauseUiTable.add(pauseLabel).center().colspan(2);
        pauseUiTable.row();
        pauseUiTable.add(sfxOn).left().colspan(2).spaceTop(ROW_PADDING);
        pauseUiTable.row();
        pauseUiTable.add(sfxLevelLabel).left();
        pauseUiTable.add(sfxVolumeSlider).growX().padLeft(SLIDER_PADDING_LEFT);
        pauseUiTable.row();
        pauseUiTable.add(musicOn).left().colspan(2).spaceTop(ROW_PADDING);
        pauseUiTable.row();
        pauseUiTable.add(musicLevelLabel).left();
        pauseUiTable.add(musicVolumeSlider).growX().padLeft(SLIDER_PADDING_LEFT);
        pauseUiTable.row();
        pauseUiTable.add(animateTiles).left().colspan(2).spaceTop(ROW_PADDING);
        pauseUiTable.row();
        pauseUiTable.add(highlightTiles).left().colspan(2).spaceTop(ROW_PADDING);
        pauseUiTable.row();
        pauseUiTable.add(pauseContinueButton).center().colspan(2).pad(BUTTON_PADDING).spaceTop(ROW_PADDING);

        //Win Screen
        winUiTable = new Table().background(baseTransparent9Patch.tint(UIText.PAUSE_BOX_COLOR));
        //Uncomment to see wireframe.
        //winUiTable.setDebug(true);
        final Label winMsg = new Label(UIText.WIN_MSG, skin);
        winLevel = new Label("", skin);
        winTime = new Label("", skin);
        winMoves = new Label("", skin);
        final TextButton winContinueButton = new TextButton(UIText.CONTINUE, skin);
        winContinueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.goToNextLevel();
                SoundManager.buttonSound();
            }
        });
        winUiTable.add(winMsg).spaceBottom(ROW_PADDING);
        winUiTable.row();
        winUiTable.add(winLevel);
        winUiTable.row();
        winUiTable.add(winTime);
        winUiTable.row();
        winUiTable.add(winMoves).spaceBottom(ROW_PADDING);
        winUiTable.row();
        winUiTable.add(winContinueButton).pad(BUTTON_PADDING);

        stage.addActor(mainUiTable);
        stage.addActor(pauseUiTable);
        stage.addActor(winUiTable);
    }

    public void newLevel() {
        moves = -1;
        time = -1;
        int level = screen.getLevel();
        winLevel.setText(UIText.LEVEL + ": " + level);
        levelNum.setText("" + level);
        rightButton.setText(UIText.NEXT);
        if (level == Levels.getRandomizedLevelIndex()) {
            levelNum.setText(UIText.RANDOM);
            rightButton.setText(UIText.RANDOM_BUTTON);
            winLevel.setText(UIText.LEVEL + ": " + UIText.RANDOM);
        } else if (level == Levels.getTrollLevelIndex()) {
            levelNum.setText(UIText.UNKNOWN_LEVEL);
            //Pointless:
            winLevel.setText(UIText.LEVEL + ": " + UIText.UNKNOWN_LEVEL);
        }
    }

    public void updateTablePositions() {
        float x = screen.getViewport().getWorldWidth() / 2;
        float y = screen.getViewport().getWorldHeight() / 2;
        pauseUiTable.setSize(MainScreen.WORLD_WIDTH * PAUSE_TABLE_WIDTH_RATIO, pauseUiTable.getPrefHeight());
        pauseUiTable.setPosition(x, y, Align.center);
        winUiTable.setSize(MainScreen.WORLD_WIDTH * PAUSE_TABLE_WIDTH_RATIO, winUiTable.getPrefHeight());
        winUiTable.setPosition(x, y, Align.center);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}