package com.alienexplorers.game.Screens;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Scenes.GameOverHud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GameOverScreen implements Screen {
    private FillViewport gamePort;
    private Game game;
    private GameOverHud hud;
    private AssetManager manager;
    private Texture background;

    public GameOverScreen(Game game, AssetManager assetManager,PlayScreen screen,int level) {
        this.game=game;
        this.manager=assetManager;
        background=new Texture(Gdx.files.internal("menuBG.png"));
        gamePort = new FillViewport(Game.V_WIDTH, Game.V_HEIGHT, new OrthographicCamera());
        hud = new GameOverHud(game.batch,this,manager,level);
        manager.get("audio/sounds/lose.mp3", Sound.class).play(game.getVolume());
    }

    public Game getGame() {
        return game;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0,Game.V_WIDTH,game.V_HEIGHT);
        game.batch.end();
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hud.dispose();
    }
}
