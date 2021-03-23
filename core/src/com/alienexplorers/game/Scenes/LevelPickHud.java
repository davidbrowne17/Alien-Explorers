package com.alienexplorers.game.Scenes;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.LevelPickScreen;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
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

public class LevelPickHud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Skin mySkin;
    private int levelReached,currentLevel;
    private Label messageLabel,levelLabel,nameLabel;
    private BitmapFont font;

    public LevelPickHud(SpriteBatch sb, final LevelPickScreen screen, final AssetManager manager, final int levelReached){
        this.levelReached=levelReached;
        currentLevel=1;
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
        Button button = new TextButton("Next",mySkin,"default");
        button.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button.scaleBy(1f);
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(levelReached>currentLevel && currentLevel<23)
                    currentLevel++;
                return true;
            }
        });
        // Text Button
        Button button2 = new TextButton("Previous",mySkin,"default");
        button2.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button2.scaleBy(1f);
        button2.setTransform(true);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(currentLevel>1)
                    currentLevel--;
                return true;
            }
        });
        // Text Button
        Button button3 = new TextButton("Start Level",mySkin,"default");
        button3.setPosition((Gdx.graphics.getWidth()/2)-(button.getWidth()/2),Gdx.graphics.getHeight()/2);
        button3.scaleBy(1f);
        button3.setTransform(true);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                screen.getGame().setScreen(new PlayScreen(screen.getGame(),manager,currentLevel));
                screen.dispose();
                dispose();
                return true;
            }
        });
        levelLabel = new Label(String.format("%02d",currentLevel), new Label.LabelStyle(font, Color.BLACK));
        messageLabel =new Label("Choose a level to play!", new Label.LabelStyle(font, Color.BLACK));
        nameLabel =new Label("Level:", new Label.LabelStyle(font, Color.BLACK));
        table.add(messageLabel).center().padTop(210);
        table.row();
        table.add(nameLabel).padTop(20).expandX().center();
        table.row();
        table.add(levelLabel).padTop(20).expandX().center();
        table.row();
        table.add(button).padTop(80).padRight(65);
        table.row();
        table.add(button2).padTop(80).padRight(95);
        table.row();
        table.add(button3).padTop(80).padRight(110);
        stage.addActor(table);
    }
    public void update(){
        Gdx.input.setInputProcessor(stage);
        levelLabel.setText(String.format("%02d",currentLevel));
    }

    @Override
    public void dispose(){
        stage.dispose();
        mySkin.dispose();

    }
}
