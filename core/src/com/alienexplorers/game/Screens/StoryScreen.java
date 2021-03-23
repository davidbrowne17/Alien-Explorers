package com.alienexplorers.game.Screens;

import com.alienexplorers.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class StoryScreen implements Screen {
    private FillViewport gamePort;
    private Game game;
    private AssetManager manager;
    private int level;
    private Label dialoguelabel;
    private Skin mySkin;
    private BitmapFont font;
    private Stage stage;
    private String line;

    public StoryScreen(final Game game, final AssetManager manager, final int level) {
        this.level=level;
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal("scripts/script.json"));
        line= base.getString(("level"+(level-1)));
        this.game=game;
        this.manager=manager;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Bromine.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);
        generator.dispose();
        mySkin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        gamePort = new FillViewport(Game.V_WIDTH, Game.V_HEIGHT, new OrthographicCamera());
        dialoguelabel=new Label(getScriptLine(), new Label.LabelStyle(font, Color.WHITE));
        stage = new Stage(gamePort,game.batch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button1 = new TextButton("Continue",mySkin,"default");
        button1.setPosition((Gdx.graphics.getWidth()/2)-(button1.getWidth()/2),Gdx.graphics.getHeight()/2);
        button1.setTransform(true);
        button1.scaleBy(1.0f);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(level!=24){
                    game.setScreen(new PlayScreen(game,manager,level));
                    dispose();
                    return true;
                }else{
                    game.setScreen(new MainMenuScreen(game,manager));
                    dispose();
                    return true;
                }
            }
        });
        table.add(dialoguelabel).center().expandY().padTop(100).padBottom(100);
        table.row();
        table.add(button1).center().expandY().padBottom(400).padRight(100);
        stage.addActor(table);
    }

    public String getScriptLine(){
        return line;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
