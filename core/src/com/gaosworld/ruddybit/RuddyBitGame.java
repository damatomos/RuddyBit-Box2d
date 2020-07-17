package com.gaosworld.ruddybit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Logger;
import com.gaosworld.ruddybit.ads.AdHandler;
import com.gaosworld.ruddybit.data.GameData;
import com.gaosworld.ruddybit.screens.MenuScreen;
import com.gaosworld.ruddybit.tools.GameAssets;

public class RuddyBitGame extends Game {

	public AssetManager manager;
	public AdHandler handlerBanner;
	public GameData gameData;
	public RuddyBitGame(AdHandler handlerBanner) {
		this.handlerBanner = handlerBanner;
	}
	public RuddyBitGame() {
		this(null);
	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(Logger.DEBUG);
		manager = new AssetManager();
		manager.getLogger().setLevel(Logger.DEBUG);
		manager.load(GameAssets.BACKGROUND, Texture.class);
		manager.load(GameAssets.PLAYER, Texture.class);
		manager.load(GameAssets.BASE, Texture.class);
		manager.load(GameAssets.TITLE, Texture.class);
		manager.load(GameAssets.GAMEOVER, Texture.class);
		manager.load(GameAssets.TAP, Texture.class);
		manager.load(GameAssets.TUBE_TOP, Texture.class);
		manager.load(GameAssets.TUBE_BOTTOM, Texture.class);
		manager.load(GameAssets.FONT32, BitmapFont.class);
		manager.load(GameAssets.FONT16, BitmapFont.class);
		manager.load(GameAssets.FONT8, BitmapFont.class);
		manager.load(GameAssets.REDCOIN, Texture.class);
		manager.load(GameAssets.PLAYER_DEAD, Texture.class);
		manager.finishLoading();
		Gdx.app.log("GAME", "DATA: ");
		gameData = new GameData();
		setScreen(new MenuScreen(this));

	}

}
