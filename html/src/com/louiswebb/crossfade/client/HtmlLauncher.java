package com.louiswebb.crossfade.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.louiswebb.crossfade.CrossFadeGame;
import com.louiswebb.crossfade.MainScreen;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration((int)MainScreen.WORLD_WIDTH, (int)MainScreen.WORLD_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new CrossFadeGame();
        }
}