package com.tangledwebgames.crossfade.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.MainScreen;
import com.tangledwebgames.crossfade.auth.AuthManager;
import com.tangledwebgames.crossfade.auth.AuthManagerEmpty;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(
				(int) MainScreen.WORLD_WIDTH,
				(int) MainScreen.WORLD_HEIGHT
		);
		config.setTitle("CrossFade");
		CrossFadeGame game = new CrossFadeGame();
		game.debug = true;
		game.authManager = new AuthManagerEmpty(AuthManager.DESKTOP_USER_ID);
		game.onConfigComplete();
		new Lwjgl3Application(game, config);
	}
}
