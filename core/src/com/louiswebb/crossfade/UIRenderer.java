package com.louiswebb.crossfade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by louiswebb on 10/15/15.
 */
public class UIRenderer implements Disposable {

    private Viewport viewport;
    private MainScreen screen;
    public Stage mainStage;
    public Stage pauseStage;
    public Stage winStage;
    private Label timeNum;
    private Label movesNum;
    private Label winTime;
    private Label winMoves;
    private Label winLevel;
    private int time;
    private int moves;

    public UIRenderer(MainScreen screen) {
        this.viewport = screen.viewport;
        this.screen = screen;
        time = -1;
        moves = -1;
        initUIStages();
    }

    public void render(ShapeRenderer renderer) {

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

        //Draw main UI layer
        mainStage.draw();

        //Draw pause or win screen.
        if (screen.paused) {
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(new Color(0, 0, 0, UIText.PAUSE_BOX_OPACITY));
            renderer.rect(0f, 0f, MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT);
            renderer.end();
            if (screen.win) {
                winLevel.setText(UIText.LEVEL + ": " + screen.level);
                winStage.draw();
            } else {
                pauseStage.draw();
            }
        }

    }

    public void initUIStages() {

        //Skin and font inits.
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        titleFont.getData().setScale(UIText.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont uiFont = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        uiFont.getData().setScale(UIText.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Skin skin = new Skin();
        skin.add("titleFont", titleFont);
        skin.add("default", uiFont);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = skin.getFont("default");
        buttonStyle.fontColor = UIText.TEXT_COLOR;
        skin.add("default", buttonStyle);

        //Main UI
        mainStage = new Stage(viewport);
        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.pad(UIText.TEXT_OFFSET, UIText.TEXT_OFFSET, MainScreen.WORLD_WIDTH + UIText.TEXT_OFFSET, UIText.TEXT_OFFSET);
        //Uncomment to see wireframe.
        //table.setDebug(true);
        mainStage.addActor(table);
        Label titleLabel = new Label(UIText.TITLE, skin, "titleFont", UIText.TEXT_COLOR);
        Label timeLabel = new Label(UIText.TIME, skin, "default", UIText.TEXT_COLOR);
        Label movesLabel = new Label(UIText.MOVES, skin, "default", UIText.TEXT_COLOR);
        timeNum = new Label("0", skin, "default", UIText.TEXT_COLOR);
        movesNum = new Label("0", skin, "default", UIText.TEXT_COLOR);
        TextButton prevButton = new TextButton(UIText.PREVIOUS, skin);
        TextButton nextButton = new TextButton(UIText.NEXT, skin);
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
        table.add(titleLabel).colspan(2).expandY();
        table.row();
        table.add(timeLabel);
        table.add(movesLabel);
        table.row();
        table.add(timeNum);
        table.add(movesNum);
        table.row();
        table.add(prevButton).expandY();
        table.add(nextButton).expandY();

        //Pause Screen
        pauseStage = new Stage(viewport);
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();
        //Uncomment to see wireframe.
        //pauseTable.setDebug(true);
        pauseStage.addActor(pauseTable);
        Label pauseLabel = new Label(UIText.PAUSED, skin, "default", UIText.PAUSE_TEXT_COLOR);
        pauseTable.add(pauseLabel);

        //Win Screen
        winStage = new Stage(viewport);
        Table winTable = new Table();
        winTable.setFillParent(true);
        winTable.center();
        //Uncomment to see wireframe.
        //winTable.setDebug(true);
        winStage.addActor(winTable);
        Label winMsg = new Label(UIText.WIN_MSG, skin, "default", UIText.PAUSE_TEXT_COLOR);
        winLevel = new Label("", skin, "default", UIText.PAUSE_TEXT_COLOR);
        winTime = new Label("", skin, "default", UIText.PAUSE_TEXT_COLOR);
        winMoves = new Label("", skin, "default", UIText.PAUSE_TEXT_COLOR);
        Label contMsg = new Label(UIText.CONT_MSG, skin, "default", UIText.PAUSE_TEXT_COLOR);
        winTable.add(winMsg);
        winTable.row();
        winTable.add(winLevel);
        winTable.row();
        winTable.add(winTime);
        winTable.row();
        winTable.add(winMoves);
        winTable.row();
        winTable.add(contMsg);

    }

    public void newLevel() {
        moves = -1;
        time = -1;
    }

    public void dispose() {
        mainStage.dispose();
        pauseStage.dispose();
        winStage.dispose();
    }
}