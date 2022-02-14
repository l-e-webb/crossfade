package com.tangledwebgames.crossfade.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.AuthManagerEmpty;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) MainScreen.WORLD_WIDTH;
		config.height = (int) MainScreen.WORLD_HEIGHT;
		CrossFadeGame game = new CrossFadeGame();
		game.authManager = new AuthManagerEmpty(AuthManager.DESKTOP_USER_ID);
		game.onConfigComplete();
		new LwjglApplication(game, config);
	}
}
