package com.udacity.gamedev.crossfade;

import com.badlogic.gdx.Game;

public class CrossFadeGame extends Game {

	@Override
	public void create () {
		this.setScreen(new MainScreen());
	}
}
