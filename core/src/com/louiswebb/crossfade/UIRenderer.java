package com.louiswebb.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.louiswebb.crossfade.game.Levels;

/**
 * Manages all UI elements through a Stage.
 */
public class UIRenderer implements Disposable {

    public static final float BUTTON_PADDING = 10f;
    public static final float CHECKBOX_SIZE = 40f;
    public static final float CHECKBOX_RIGHT_PADDING = 25f;
    public static final float PAUSE_TABLE_WIDTH_RATIO = 0.85f;
    public static final int SLIDER_KNOB_SIZE = 25;
    public static final int SLIDER_HEIGHT = 30;
    public static final float ROW_PADDING = 15f;
    
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
    private int time;
    private int moves;

    public UIRenderer(MainScreen screen) {
        this.screen = screen;
        stage = new Stage(screen.viewport);
        time = -1;
        moves = -1;
        initUI();
    }

    public void render() {

        //Update time and move UI elements
        if (time < (int) screen.time) {
            time = (int) screen.time;
            timeNum.setText("" + time);
            winTime.setText(UIText.TIME + ": " + time);
        }
        if (moves < screen.board.getMoves()) {
            moves = screen.board.getMoves();
            movesNum.setText("" + moves);
            winMoves.setText(UIText.MOVES + ": " + moves);
        }

        //Update table visibility
        setVisibleTables();

        //Draw main UI layer
        stage.draw();

    }

    private void setVisibleTables() {
        switch (screen.state) {
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

    public Stage getStage() { return stage; }

    void initUI() {

        //Font inits.
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        titleFont.getData().setScale(UIText.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont uiFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        uiFont.getData().setScale(UIText.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Style inits.
        Texture baseLabelTexture = new Texture("base_label_9patch.png");
        NinePatchDrawable baseLabel = new NinePatchDrawable(
                new NinePatch(baseLabelTexture, 5, 5, 5, 5)
        );
        Label.LabelStyle labelStyle = new Label.LabelStyle(uiFont, UIText.TEXT_COLOR);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, UIText.TEXT_COLOR);
        NinePatchDrawable baseFilled9Patch = new NinePatchDrawable(
                new NinePatch(new Texture("button_filled_9patch.png"), 13, 13 , 13, 13));
        NinePatchDrawable baseEmpty9Patch = new NinePatchDrawable(
                new NinePatch(new Texture("button_empty_9patch.png"), 13, 13 , 13, 13));
        NinePatchDrawable baseTransparent9Patch = new NinePatchDrawable(
                new NinePatch(new Texture("button_transparent_filled_9patch.png"), 13, 13, 13,13)
        );
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = baseEmpty9Patch.tint(UIText.TEXT_COLOR);
        buttonStyle.over = baseFilled9Patch.tint(UIText.TEXT_COLOR);
        buttonStyle.fontColor = UIText.TEXT_COLOR;
        buttonStyle.overFontColor = UIText.INVERTED_TEXT_COLOR;
        buttonStyle.font = uiFont;
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(
                baseFilled9Patch.tint(UIText.TEXT_COLOR),
                baseEmpty9Patch.tint(UIText.TEXT_COLOR),
                uiFont,
                UIText.TEXT_COLOR
        );
        Sprite sliderBackgroundSprite = new Sprite(baseLabelTexture);
        sliderBackgroundSprite.setSize(SLIDER_HEIGHT, SLIDER_HEIGHT);
        SpriteDrawable sliderBackground = new SpriteDrawable(sliderBackgroundSprite).tint(Color.FOREST);
        Sprite sliderKnobSprite = new Sprite(new Texture("slider_knob.png"));
        sliderKnobSprite.setSize(SLIDER_KNOB_SIZE, SLIDER_KNOB_SIZE);
        SpriteDrawable sliderKnob = new SpriteDrawable(sliderKnobSprite).tint(Color.GREEN);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(
                sliderBackground,
                sliderKnob
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
        //Uncomment to see wireframe.
        //mainUiTable.setDebug(true);
        stage.addActor(mainUiTable);
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
            }
        });
        centerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.onCenterButtonClick();
            }
        });
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.onRightButtonClick();
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
        stage.addActor(pauseUiTable);
        final Label pauseLabel = new Label(UIText.PAUSED, skin);
        final CheckBox sfxOn = new CheckBox(UIText.SFX, skin);
        sfxOn.getImageCell().maxSize(CHECKBOX_SIZE).spaceRight(CHECKBOX_RIGHT_PADDING);
        final Label sfxLevelLabel = new Label(UIText.SFX_LEVEL, skin);
        final Slider sfxLevelSlider = new Slider(0, 1, 0.1f, false, skin);
        final CheckBox musicOn = new CheckBox(UIText.MUSIC, skin);
        musicOn.getImageCell().maxSize(CHECKBOX_SIZE).spaceRight(CHECKBOX_RIGHT_PADDING);
        final Label musicLevelLabel = new Label(UIText.MUSIC_LEVEL, skin);
        final Slider musicLevelSlider = new Slider(0, 1, 0.1f, false, skin);
        final CheckBox animateTiles = new CheckBox(UIText.ANIMATE_TILES, skin);
        animateTiles.getImageCell().maxSize(CHECKBOX_SIZE).spaceRight(CHECKBOX_RIGHT_PADDING);
        final Button pauseContinueButton = new TextButton(UIText.CONTINUE, skin);
        pauseContinueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.resume();
            }
        });
        pauseUiTable.add(pauseLabel).center().colspan(2);
        pauseUiTable.row();
        pauseUiTable.add(sfxOn).left().colspan(2).spaceTop(ROW_PADDING);
        pauseUiTable.row();
        pauseUiTable.add(sfxLevelLabel).left();
        pauseUiTable.add(sfxLevelSlider).growX();
        pauseUiTable.row();
        pauseUiTable.add(musicOn).left().colspan(2).spaceTop(ROW_PADDING);
        pauseUiTable.row();
        pauseUiTable.add(musicLevelLabel).left();
        pauseUiTable.add(musicLevelSlider).growX();
        pauseUiTable.row();
        pauseUiTable.add(animateTiles).left().colspan(2).spaceTop(ROW_PADDING);
        pauseUiTable.row();
        pauseUiTable.add(pauseContinueButton).center().colspan(2).pad(BUTTON_PADDING).spaceTop(ROW_PADDING);

        //Win Screen
        winUiTable = new Table().background(baseTransparent9Patch.tint(UIText.PAUSE_BOX_COLOR));
        //Uncomment to see wireframe.
        //winUiTable.setDebug(true);
        stage.addActor(winUiTable);
        final Label winMsg = new Label(UIText.WIN_MSG, skin);
        winLevel = new Label("", skin);
        winTime = new Label("", skin);
        winMoves = new Label("", skin);
        final TextButton winContinueButton = new TextButton(UIText.CONTINUE, skin);
        winContinueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.goToNextLevel();
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

    }

    void newLevel() {
        moves = -1;
        time = -1;
        int level = screen.level;
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

    void updateTablePositions() {
        float x = screen.viewport.getWorldWidth() / 2;
        float y = screen.viewport.getWorldHeight() / 2;
        pauseUiTable.setSize(MainScreen.WORLD_WIDTH * PAUSE_TABLE_WIDTH_RATIO, pauseUiTable.getPrefHeight());
        pauseUiTable.setPosition(x, y, Align.center);
        winUiTable.setSize(MainScreen.WORLD_WIDTH * PAUSE_TABLE_WIDTH_RATIO, winUiTable.getPrefHeight());
        winUiTable.setPosition(x, y, Align.center);
    }

    public void dispose() {
        stage.dispose();
    }
}