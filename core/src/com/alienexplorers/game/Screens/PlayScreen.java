package com.alienexplorers.game.Screens;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Items.Disc;
import com.alienexplorers.game.Items.Item;
import com.alienexplorers.game.Items.ItemDef;
import com.alienexplorers.game.Scenes.Hud;
import com.alienexplorers.game.Sprites.Enemy;
import com.alienexplorers.game.Sprites.Player;
import com.alienexplorers.game.Tools.B2WorldCreator;
import com.alienexplorers.game.Tools.FixedOrthogonalTiledMapRenderer;
import com.alienexplorers.game.Tools.LevelLoader;
import com.alienexplorers.game.Tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {
    private Game game;
    private TextureAtlas atlas;
    private Viewport gamePort;
    private OrthographicCamera gamecam;
    private Hud hud;
    private TiledMap map;
    private FixedOrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;
    private Rectangle rectLeft,rectRight;
    private Music music;
    private Preferences prefs;
    private AssetManager manager;
    private B2WorldCreator creator;
    private Texture background;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private int level;
    private boolean dispose;

    public PlayScreen(Game game, AssetManager manager,int level){
        prefs = Gdx.app.getPreferences("MyPreferences");
        if(level >= prefs.getInteger("level")){
            prefs.putInteger("level",level);
        }
        prefs.flush();
        background=new Texture(Gdx.files.internal("bg.png"));
        atlas = new TextureAtlas("Player_and_enemies.atlas");
        this.game=game;
        this.manager=manager;
        player=new Player();
        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(Game.V_WIDTH/ Game.PPM,Game.V_HEIGHT/ Game.PPM,gamecam);
        hud = new Hud(game.batch,this);
        this.level=level;
        LevelLoader loader = new LevelLoader(level);
        map = loader.getMap();
        renderer = new FixedOrthogonalTiledMapRenderer(map,1/ Game.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
        //create  Box2D world setting no gravity in X -80 gravity in Y and allow bodies to sleep
        world = new World(new Vector2(0,-80),true);
        //box2D
        b2dr = new Box2DDebugRenderer();
        creator =new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());
        music = manager.get("audio/music/music.wav",Music.class);
        if(game.getVolume()!=0){
        music.setLooping(true);
        music.setVolume(game.getVolume());
        music.play();
        }
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
        game.resetInputProcessor();
        updateRects();
    }
    public void setPlayer(Player player){
        this.player=player;
    }
    public Player getPlayer() { return player; }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public Game getGame() {
        return game;
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type== Disc.class){
                items.add(new Disc(this,idef.position.x,idef.position.y));
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void handleInput(float dt){
        Vector3 vec=new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
        gamecam.unproject(vec);
        if(game.doubleTapped){
            player.fire();
            game.doubleTapped = false;
        }
        if(Gdx.input.isTouched()){
            if(rectLeft.contains(vec.x,vec.y)){
                if(player.b2body.getLinearVelocity().x>-8){
                    player.b2body.applyLinearImpulse(new Vector2(-1.0f, 0), player.b2body.getWorldCenter(), true);
                    player.setRunningRight(false);
                }
            }
            if(rectRight.contains(vec.x,vec.y)){
                if(player.b2body.getLinearVelocity().x<8){
                    player.b2body.applyLinearImpulse(new Vector2(1.0f, 0), player.b2body.getWorldCenter(), true);
                    player.setRunningRight(true);
                }
            }
        }
        if(game.swippedUp&& player.b2body.getLinearVelocity().y==0){
            player.b2body.applyLinearImpulse(new Vector2(0,20f),player.b2body.getWorldCenter(), true);
            game.swippedUp=false;
        }
        //PC CONTROLS WILL NOT WORK ON ANDROID
        /*if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);*/
    }
    public void updateRects(){
        rectRight = new Rectangle(gamecam.position.x,gamecam.position.y-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
        rectLeft = new Rectangle(gamecam.position.x-Gdx.graphics.getWidth()/2,gamecam.position.y-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
    }

    public void update(float dt){
        handleInput(dt);
        handleSpawningItems();
        world.step(1/60f,6,2);
        player.update(dt);
        for(Enemy enemy : creator.getEnemies())
            enemy.update(dt);
        for(Item item : items)
            item.update(dt);
        hud.update(dt);
        updateRects();
        gamecam.update();
        gamecam.position.x = player.b2body.getPosition().x;
        renderer.setView(gamecam);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
        //clear screen with black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0,Game.V_WIDTH,Game.V_HEIGHT);
        game.batch.end();
        game.batch.begin();
        game.batch.setProjectionMatrix(gamecam.combined);
        renderer.render();
        for(Enemy enemy : creator.getEnemies()){
            enemy.draw(game.batch);
            if(enemy.getX() < player.getX()+1664 /Game.PPM && !enemy.b2body.isActive())
                enemy.b2body.setActive(true);
        }
        for(Item item : items)
            item.draw(game.batch);
        player.draw(game.batch);
        game.batch.end();
        hud.stage.draw();
        //b2d debug lines
        //b2dr.render(world,gamecam.combined);
        if(player.b2body.getPosition().y<0-player.getFrame(delta).getTexture().getHeight()/2 | hud.getWorldTimer()<=0){
            player.die();
            game.setGameOverScreen(level);
            dispose();
        }
        else if(Player.State.DEAD == player.currentState){
            float delay = 0.2f; // seconds
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    game.setGameOverScreen(level);
                    dispose();
                }
            }, delay);
            }
        if(dispose)
            dispose();
    }
    public int getDisks(){
        return player.getDisks();
    }

    public void finishLevel(){
        if(level<=23){
            level++;
            game.setScreen(new StoryScreen(game,manager,level));
        }else{
            game.setScreen(new MainMenuScreen(game,manager));
        }
        if(level>=prefs.getInteger("level"))
            prefs.putInteger("level",level);
        dispose=true;
    }
    public TiledMap getMap(){
        return map;
    }

    public World getWorld() {
        return world;
    }
    public AssetManager getManager(){
        return manager;
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
        background.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
