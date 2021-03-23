package com.alienexplorers.game;

import com.alienexplorers.game.Screens.GameOverScreen;
import com.alienexplorers.game.Screens.MainMenuScreen;
import com.alienexplorers.game.Screens.PlayScreen;
import com.alienexplorers.game.Screens.StoryScreen;
import com.alienexplorers.game.Tools.AdHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class Game extends com.badlogic.gdx.Game implements GestureDetector.GestureListener {
	public SpriteBatch batch;
	public static final int V_WIDTH = 1600;
 	public static final int V_HEIGHT = 900;
	public static final float PPM = 100;
	public static final short GROUND_BIT =1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT=4;
	public static final short COIN_BIT=8;
	public static final short DESTROYED_BIT=16;
	public static final short USED_BRICK_BIT=32;
	public static final short BOX_BIT=64;
	public static final short FLYING_ENEMY_BIT=128;
	public static final short ENEMY_BIT = 256;
	public static final short ENEMY_HEAD_BIT = 512;
	public static final short ITEM_BIT = 1024;
	public static final short NOTHING_BIT = 2048;
	public static final short LEVEL_END_BIT = 4096;
	public static final short BULLET_BIT = 8192;
	private GestureDetector gestureDetector;
	private float volume= 100;
	public boolean doubleTapped=false;
	public boolean swippedUp=false;
	public AssetManager manager;
	AdHandler handler;
	boolean toggle = true;

	public Game(AdHandler handler) {
		this.handler = handler;
		handler.showAds(true);
	}


	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/music.wav", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/jump.wav", Sound.class);
		manager.load("audio/sounds/low_bump.wav", Sound.class);
		manager.load("audio/sounds/bump.mp3", Sound.class);
		manager.load("audio/sounds/lose.mp3", Sound.class);
		manager.load("audio/sounds/power_up.wav", Sound.class);
		manager.load("audio/sounds/splat.wav", Sound.class);
		manager.load("audio/sounds/game_over.wav", Sound.class);
		manager.finishLoading();
		setScreen(new MainMenuScreen(this,manager));
		gestureDetector = new GestureDetector(20, 40, 0.5f, 2, 0.15f, this);
		Gdx.input.setInputProcessor(gestureDetector);
	}

	public void resetInputProcessor(){
		Gdx.input.setInputProcessor(gestureDetector);
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (count == 2) {
			System.out.println("Double tap!");
			doubleTapped=true;
			return true;
		}
		return false;
	}
	public void setGameOverScreen(int level){
		setScreen(new GameOverScreen(this,manager,(PlayScreen)getScreen(),level));
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(Math.abs(velocityX)>Math.abs(velocityY)){
			if(velocityX>0){
				//swiped right
				return false;
			}else{
				//swiped left
				return false;
			}
		}else{
			if(velocityY>0){
				//swiped down
				return false;
			}else{
				//swiped up
				swippedUp=true;
				return true;
			}
		}
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}
}
