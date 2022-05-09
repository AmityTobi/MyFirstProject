package com.basic_mario;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isoterik.racken.GameDriver;
import com.isoterik.racken.Racken;
import com.isoterik.racken.Scene;

public class SuperMario extends GameDriver {


	@Override
	protected Scene initGame() {
		racken.defaultSettings.VIEWPORT_WIDTH = 1024;
		racken.defaultSettings.VIEWPORT_HEIGHT = 768;
		racken.defaultSettings.PIXELS_PER_UNIT = 16;

		racken.assets.enqueueAsset("Map/map.tmx", TiledMap.class);
		racken.assets.enqueueSkin("Skin/glassy-ui.json");
		racken.assets.enqueueAsset("bg/background.png", Texture.class);
		racken.assets.enqueueAsset("Sfx/Mario.mp3", Music.class);
		racken.assets.loadAssetsInBackground(new Runnable() {
			@Override
			public void run() {
				racken.setScene(new MenuScene());
			}
		});
		return new SplashScene();
	}
}
