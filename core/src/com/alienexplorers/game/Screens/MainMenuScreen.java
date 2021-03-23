package com.alienexplorers.game.Screens;

import com.alienexplorers.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import jdk.nashorn.internal.runtime.Context;


public class MainMenuScreen implements Screen {
    private Game game;
    private AssetManager manager;
    private Texture background;
    public Stage stage;
    private BitmapFont font;
    private Viewport viewport;
    private Skin mySkin;
    private Preferences prefs;
    public MainMenuScreen(final Game game, final AssetManager manager) {
        this.game=game;
        this.manager=manager;
        prefs = Gdx.app.getPreferences("MyPreferences");
        background=new Texture(Gdx.files.internal("alienexplorersbanner.png"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Bromine.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);
        generator.dispose();
        mySkin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        viewport = new FillViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button = new TextButton("Play",mySkin,"default");
        button.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button.scaleBy(1f);
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("here","");
                game.setScreen(new LevelPickScreen(game,manager,prefs.getInteger("level")));
                dispose();
                return true;
            }
        });
        Button button2 = new TextButton("Settings",mySkin,"default");
        button2.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button2.scaleBy(1f);
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game,manager));
                dispose();
                return true;
            }
        });
        Button button3 = new TextButton("Controls",mySkin,"default");
        button3.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button3.scaleBy(1f);
        button3.setTransform(true);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new ControlScreen(game,manager));
                dispose();
                return true;
            }
        });
        table.add(button).center().padTop(500).padRight(65);
        table.row();
        table.add(button2).center().padTop(100).expandX().padRight(95);
        table.row();
        table.add(button3).center().padTop(100).expandX().padRight(95);
        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0,Game.V_WIDTH,game.V_HEIGHT);
        game.batch.end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
        mySkin.dispose();
    }
}
