package com.louiswebb.crossfade.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.louiswebb.crossfade.CrossFadeGame;
import com.louiswebb.crossfade.MainScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) MainScreen.WORLD_WIDTH;
		config.height = (int) MainScreen.WORLD_HEIGHT;
		new LwjglApplication(new CrossFadeGame(), config);
	}
}
