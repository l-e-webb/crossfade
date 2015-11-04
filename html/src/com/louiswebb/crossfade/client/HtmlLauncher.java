package com.louiswebb.crossfade.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.louiswebb.crossfade.CrossFadeGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(600, 960);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new CrossFadeGame();
        }
}