package com.louiswebb.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.louiswebb.crossfade.game.Levels;
import com.louiswebb.crossfade.sound.SoundManager;

public class CrossFadeGame extends Game {

	public static Application.ApplicationType APP_TYPE;

	@Override
	public void create () {
        APP_TYPE = Gdx.app.getType();
		Levels.init();
		SoundManager.init();
        this.setScreen(new MainScreen());
	}
}
