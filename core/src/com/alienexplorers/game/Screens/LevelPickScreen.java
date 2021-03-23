package com.alienexplorers.game.Screens;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Scenes.LevelPickHud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class LevelPickScreen implements Screen {
    private FillViewport gamePort;
    private Game game;
    private LevelPickHud hud;
    private AssetManager manager;
    private Preferences prefs;
    private int levelReached,lastLevel;
    private Texture background;

    public LevelPickScreen(Game game, AssetManager assetManager,int lastLevel) {
        this.lastLevel=lastLevel;
        this.game=game;
        this.manager=assetManager;
        background=new Texture(Gdx.files.internal("menuBG.png"));
        prefs = Gdx.app.getPreferences("MyPreferences");
        gamePort = new FillViewport(Game.V_WIDTH, Game.V_HEIGHT, new OrthographicCamera());
        hud = new LevelPickHud(game.batch,this,manager,levelAt());
    }
    public int levelAt(){
        levelReached = prefs.getInteger("level");
        if(lastLevel>=levelReached){
            prefs.putInteger("level",lastLevel);
            levelReached=lastLevel;
        }
        prefs.flush();
        return levelReached;
    }

    public Game getGame() {
        return game;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0,Game.V_WIDTH,game.V_HEIGHT);
        game.batch.end();
        hud.stage.draw();
    }
    public void update(){
        hud.update();
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
