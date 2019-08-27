package com.tangledwebgames.crossfade;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tangledwebgames.crossfade.game.Levels;
import com.tangledwebgames.crossfade.sound.SoundManager;

public class CrossFadeGame extends Game {

	public static Application.ApplicationType APP_TYPE;

	@Override
	public void create () {
        APP_TYPE = Gdx.app.getType();
		if (APP_TYPE == Application.ApplicationType.Android) {
			Gdx.input.setCatchKey(Input.Keys.MENU, true);
		}
        Assets.instance.loadAll();
		PreferenceWrapper.init();
		Levels.init();
		SoundManager.init();
        this.setScreen(new MainScreen());
	}
}
