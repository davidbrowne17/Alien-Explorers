package com.alienexplorers.game.Tools;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.alienexplorers.game.Sprites.AlienEnemy;
import com.alienexplorers.game.Sprites.Boss;
import com.alienexplorers.game.Sprites.Box;
import com.alienexplorers.game.Sprites.Brick;
import com.alienexplorers.game.Sprites.Coin;
import com.alienexplorers.game.Sprites.Enemy;
import com.alienexplorers.game.Sprites.FlyingEnemy;
import com.alienexplorers.game.Sprites.LevelEnd;
import com.alienexplorers.game.Sprites.Player;
import com.alienexplorers.game.Sprites.SpikeEnemy;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class B2WorldCreator {
    private Array<AlienEnemy> alienEnemies;
    private Array<Boss> bossEnemies;
    private Array<SpikeEnemy> spikeEnemies;
    private Array<FlyingEnemy> flyingEnemies;
    private Array<Enemy> enemies;
    private PlayScreen screen;

    public B2WorldCreator(PlayScreen screen){
        this.screen=screen;
        enemies = new Array<Enemy>();
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        AssetManager manager = screen.getManager();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //create ground
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(((rect.getX()+rect.getWidth()/2)/ Game.PPM),(rect.getY()+rect.getHeight()/2)/ Game.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 / Game.PPM,rect.getHeight()/2 / Game.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Coin(screen,object,manager);
        }
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Box(screen,object,manager);
        }
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Brick(screen,object,manager);
        }
        //create Alien Enemies
        alienEnemies = new Array<AlienEnemy>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            alienEnemies.add(new AlienEnemy(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //create Spike Enemies
        spikeEnemies = new Array<SpikeEnemy>();
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            spikeEnemies.add(new SpikeEnemy(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //create Spike Enemies
        flyingEnemies = new Array<FlyingEnemy>();
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            flyingEnemies.add(new FlyingEnemy(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
        }
        //spawn player
        for(MapObject object : screen.getMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            Player player = new Player(screen,(int)rect.getX(),(int)rect.getY());
            screen.setPlayer(player);
        }
        //finish level
        for(MapObject object : screen.getMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new LevelEnd(screen,object,manager);
        }
        //spawn boss
        if(screen.getLevel()==23){
            //create Alien Enemies
           bossEnemies = new Array<Boss>();
            for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                bossEnemies.add(new Boss(screen,rect.getX()/Game.PPM,rect.getY()/Game.PPM));
            }
        }
    }
    public Array<Enemy> getEnemies() {
        if(enemies.isEmpty()){
            enemies.addAll(spikeEnemies);
            enemies.addAll(flyingEnemies);
            enemies.addAll(alienEnemies);
            if(screen.getLevel()==23)
                enemies.addAll(bossEnemies);
        }
        return enemies;
    }
}
