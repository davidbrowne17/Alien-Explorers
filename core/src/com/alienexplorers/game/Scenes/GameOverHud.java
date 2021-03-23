package com.alienexplorers.game.Scenes;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.GameOverScreen;
import com.alienexplorers.game.Screens.LevelPickScreen;
import com.alienexplorers.game.Screens.MainMenuScreen;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverHud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Skin mySkin;
    private Label scoreLabel;
    private Preferences prefs;
    private int level;
    private Label gameOverLabel;
    private Label deadLabel;
    private BitmapFont font;

    public GameOverHud(SpriteBatch sb, final GameOverScreen screen, final AssetManager manager, final int level){
        this.level=level;
        prefs = Gdx.app.getPreferences("MyPreferences");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Bromine.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);
        generator.dispose();
        mySkin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        viewport = new FillViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        // Text Button
        Button button1 = new TextButton("Restart",mySkin,"default");
        button1.setPosition((Gdx.graphics.getWidth()/2)-(button1.getWidth()/2),Gdx.graphics.getHeight()/2);
        button1.setTransform(true);
        button1.scaleBy(1.0f);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                screen.getGame().setScreen(new PlayScreen(screen.getGame(),manager,level));
                screen.dispose();
                Gdx.app.log("pressed","");
                dispose();
                return true;
            }
        });
        Button button2 = new TextButton("Main menu",mySkin,"default");
        button2.setPosition((Gdx.graphics.getWidth()/2)-(button2.getWidth()/2),Gdx.graphics.getHeight()/2);
        button2.setTransform(true);
        button2.scaleBy(1.0f);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                int level = prefs.getInteger("level");
                screen.getGame().setScreen(new MainMenuScreen(screen.getGame(),manager));
                screen.dispose();
                dispose();
                return true;
            }
        });
        gameOverLabel =new Label("GAME OVER", new Label.LabelStyle(font, Color.BLACK));
        deadLabel = new Label("DEAD AT LEVEL: "+level, new Label.LabelStyle(font, Color.BLACK));
        scoreLabel = new Label("SCORE: "+prefs.getInteger("score"), new Label.LabelStyle(font, Color.BLACK));
        table.add(deadLabel).center().padTop(200);
        table.row();
        table.add(scoreLabel).center().padTop(60);
        table.row();
        table.add(gameOverLabel).center().padTop(80);
        table.row();
        table.add(button1).center().padTop(100).padRight(70);
        table.row();
        table.add(button2).center().padTop(100).padRight(90);
        stage.addActor(table);
    }

    @Override
    public void dispose(){
        stage.dispose();
        mySkin.dispose();

    }
}
