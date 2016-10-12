package com.louiswebb.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.louiswebb.crossfade.game.Levels;

public class CrossFadeGame extends Game {

	public static Application.ApplicationType APP_TYPE;

	@Override
	public void create () {
        APP_TYPE = Gdx.app.getType();
		Levels.init();
        this.setScreen(new MainScreen());
	}
}
