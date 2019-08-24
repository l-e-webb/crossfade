package com.tangledwebgames.crossfade.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.MainScreen;

public class HtmlLauncher extends GwtApplication {

    private static final String LOADING_STROKE_COLOR = "#00ff00";
    private static final String LOADING_TEXT = "Loading...";
    private static final int LOADING_WIDTH = (int) MainScreen.WORLD_WIDTH;
    private static final int LOADING_HEIGHT = 150;

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration((int)MainScreen.WORLD_WIDTH, (int)MainScreen.WORLD_HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener () {
        return new CrossFadeGame();
    }

    @Override
    public Preloader.PreloaderCallback getPreloaderCallback() {
        final Canvas canvas = Canvas.createIfSupported();
        canvas.setWidth(LOADING_WIDTH + "px");
        canvas.setHeight(LOADING_HEIGHT + "px");
        getRootPanel().add(canvas);
        final Context2d context = canvas.getContext2d();
        context.setTextAlign(Context2d.TextAlign.CENTER);
        context.setTextBaseline(Context2d.TextBaseline.MIDDLE);
        context.setFont("32pt Arial");

        return new Preloader.PreloaderCallback() {
            @Override
            public void update(Preloader.PreloaderState state) {
                context.setFillStyle(LOADING_STROKE_COLOR);
                context.fillText(LOADING_TEXT, LOADING_WIDTH / 3, LOADING_HEIGHT / 2, LOADING_WIDTH);
            }

            @Override
            public void error(String file) {}
        };
    }
}