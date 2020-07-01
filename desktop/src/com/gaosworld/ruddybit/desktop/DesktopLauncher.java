package com.gaosworld.ruddybit.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gaosworld.ruddybit.RuddyBitGame;
import com.gaosworld.ruddybit.tools.GameTools;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameTools.WIDTH;
		config.height = GameTools.HEIGHT;
		config.resizable = false;
		config.backgroundFPS = 30;
		config.foregroundFPS = 30;
		new LwjglApplication(new RuddyBitGame(), config);
	}
}
