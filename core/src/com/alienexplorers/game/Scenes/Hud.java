package com.alienexplorers.game.Scenes;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private int worldTimer;
    private float timeCount;
    public static int score=0;
    private Label countdownLabel;
    private Preferences prefs;
    private static Label scoreLabel;
    private Label timeLabel,discLabel,discNumLabel;
    private Label playerLabel;
    private BitmapFont font;
    private int diskNum;
    private PlayScreen screen;

    public Hud(SpriteBatch sb, PlayScreen screen){
        this.screen=screen;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Bromine.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);
        prefs = Gdx.app.getPreferences("MyPreferences");
        score = prefs.getInteger("score");
        generator.dispose();
        worldTimer = 300;
        timeCount=0;
        viewport = new FillViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        countdownLabel = new Label(String.format("Time: %03d",worldTimer), new Label.LabelStyle(font, Color.BLACK));
        scoreLabel = new Label(String.format("Score: %06d",score), new Label.LabelStyle(font, Color.BLACK));
        discLabel = new Label(String.format("Disks: %02d", diskNum), new Label.LabelStyle(font, Color.BLACK));
        table.add(scoreLabel).expandX().padTop(100).padLeft(100);
        table.add(discLabel).expandX().padTop(100);
        table.add(countdownLabel).expandX().padTop(100).padRight(100);
        stage.addActor(table);

    }

    public float getWorldTimer(){
        return worldTimer;
    }

    public static void addScore(int value){
        score+=value;
        scoreLabel.setText(String.format("Score: %06d",score));
    }

    public void update(float dt){
        timeCount +=dt;
        while(timeCount >= 1){
            worldTimer--;
            timeCount -= 1;
        }
        diskNum=screen.getDisks();
        countdownLabel.setText(String.format("Time: %03d", worldTimer));
        discLabel.setText(String.format("Disks: %02d", diskNum));
        if(score >= prefs.getInteger("score") && screen.getLevel()==prefs.getInteger("level")){
            prefs.putInteger("score",score);
            prefs.flush();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
