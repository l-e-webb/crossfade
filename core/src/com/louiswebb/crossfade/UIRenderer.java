package com.louiswebb.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by louiswebb on 10/15/15.
 */
public class UIRenderer implements Disposable {
    
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
        if (moves < screen.boardRenderer.getMoves()) {
            moves = screen.boardRenderer.getMoves();
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

    public void initUI() {

        //Font inits.
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        titleFont.getData().setScale(UIText.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont uiFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        uiFont.getData().setScale(UIText.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Style inits.
        Label.LabelStyle labelStyle = new Label.LabelStyle(uiFont, UIText.TEXT_COLOR);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, UIText.TEXT_COLOR);
        NinePatchDrawable baseFilled9Patch = new NinePatchDrawable(
                new NinePatch(new Texture("button_filled_9patch.png"), 25, 25 , 25, 25));
        NinePatchDrawable baseEmpty9Patch = new NinePatchDrawable(
                new NinePatch(new Texture("button_empty_9patch.png"), 25, 25 , 25, 25));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = baseEmpty9Patch.tint(UIText.TEXT_COLOR);
        buttonStyle.over = baseFilled9Patch.tint(UIText.TEXT_COLOR);
        buttonStyle.fontColor = UIText.TEXT_COLOR;
        buttonStyle.overFontColor = UIText.INVERTED_TEXT_COLOR;
        buttonStyle.font = uiFont;


        //Skin init.
        Skin skin = new Skin();
        skin.add("titleStyle", titleStyle);
        skin.add("default", labelStyle);
        skin.add("default", buttonStyle);

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
        final TextButton prevButton = new TextButton(UIText.PREVIOUS, skin);
        final TextButton nextButton = new TextButton(UIText.NEXT, skin);
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.goToLevel(screen.level - 1);
            }
        });
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.goToLevel(screen.level + 1);
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
        mainUiTable.add(prevButton).expandY().fillX().colspan(3);
        mainUiTable.add(nextButton).expandY().fillX().colspan(3);

        //Pause Screen
        pauseUiTable = new Table().background(baseFilled9Patch.tint(Color.DARK_GRAY));
        //Uncomment to see wireframe.
        //pauseUiTable.setDebug(true);
        stage.addActor(pauseUiTable);
        final Label pauseLabel = new Label(UIText.PAUSED, skin);
        final Label pauseContLabel = new Label((CrossFadeGame.APP_TYPE == Application.ApplicationType.Android) ?
                UIText.CONT_MSG_AND : UIText.CONT_MSG_PAUSE, skin);
        pauseUiTable.add(pauseLabel);
        pauseUiTable.row();
        pauseUiTable.add(pauseContLabel);

        //Win Screen
        winUiTable = new Table().background(baseFilled9Patch.tint(Color.DARK_GRAY));
        //Uncomment to see wireframe.
        //winUiTable.setDebug(true);
        stage.addActor(winUiTable);
        final Label winMsg = new Label(UIText.WIN_MSG, skin);
        winLevel = new Label("", skin);
        winTime = new Label("", skin);
        winMoves = new Label("", skin);
        final Label contMsg = new Label((CrossFadeGame.APP_TYPE == Application.ApplicationType.Android) ?
                UIText.CONT_MSG_AND : UIText.CONT_MSG, skin);
        winUiTable.add(winMsg);
        winUiTable.row();
        winUiTable.add(winLevel);
        winUiTable.row();
        winUiTable.add(winTime);
        winUiTable.row();
        winUiTable.add(winMoves);
        winUiTable.row();
        winUiTable.add(contMsg);

    }

    public void newLevel() {
        moves = -1;
        time = -1;
        winLevel.setText(UIText.LEVEL + ": " + screen.level);
        levelNum.setText("" + screen.level);
    }

    public void updateTablePositions() {
        float x = screen.viewport.getWorldWidth() / 2;
        float y = screen.viewport.getWorldHeight() / 2;
        float w = 3 * screen.viewport.getWorldWidth() / 5;
        float h = screen.viewport.getWorldHeight() / 2;
        pauseUiTable.setPosition(x, y, Align.center);
        pauseUiTable.setSize(pauseUiTable.getPrefWidth(), pauseUiTable.getPrefHeight());
        winUiTable.setPosition(x, y, Align.center);
        winUiTable.setSize(winUiTable.getPrefWidth(), winUiTable.getPrefHeight());
    }

    public void dispose() {
        stage.dispose();
    }
}