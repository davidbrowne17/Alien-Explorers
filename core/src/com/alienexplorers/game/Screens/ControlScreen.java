package com.alienexplorers.game.Screens;

import com.alienexplorers.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class ControlScreen implements Screen {
    private Game game;
    private AssetManager manager;
    private Texture background;
    public Stage stage;
    private BitmapFont font;
    private Viewport viewport;
    private Skin mySkin;
    private Label runLabel,shootLabel,jumpLabel;
    public ControlScreen(final Game game, final AssetManager manager) {
        this.game=game;
        this.manager=manager;
        background=new Texture(Gdx.files.internal("bg.png"));
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
        runLabel = new Label(String.format("Press right or left side of screen to run"), new Label.LabelStyle(font, Color.BLACK));
        jumpLabel = new Label(String.format("Swipe up to jump"), new Label.LabelStyle(font, Color.BLACK));
        shootLabel = new Label(String.format("Double tap to shoot disks"), new Label.LabelStyle(font, Color.BLACK));
        Button button = new TextButton("Main Menu",mySkin,"default");
        button.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button.scaleBy(1f);
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game,manager));
                dispose();
                return true;
            }
        });
        table.add(runLabel).center().padTop(300);
        table.row();
        table.add(jumpLabel);
        table.row();
        table.add(shootLabel);
        table.row();
        table.add(button).center().padTop(100).expandY().padRight(110);
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
