package com.alienexplorers.game.Sprites;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    public enum State {FALLING,JUMPING,RUNNING,STANDING,DEAD}
    public State currentState,previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerStanding;
    private Animation<TextureRegion> playerRun,playerJump,playerStand;
    private float stateTimer;
    private Boolean runningRight;
    private boolean isDead=false;
    private PlayScreen screen;
    private int spawnX,spawnY;
    private Array<Bullet> bullets;
    private int disks;
    public Player(PlayScreen screen,int spawnX,int spawnY){
        super(screen.getAtlas().findRegion("playerRed_walk1"));
        this.screen=screen;
        this.spawnX=spawnX;
        this.spawnY=spawnY;
        runningRight = true;
        world=screen.getWorld();
        currentState = State.RUNNING;
        previousState = State.RUNNING;
        stateTimer = 0;
        disks=0;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(screen.getAtlas().findRegion("playerRed_walk1"));
        frames.add(screen.getAtlas().findRegion("playerRed_walk2"));
        frames.add(screen.getAtlas().findRegion("playerRed_walk3"));
        frames.add(screen.getAtlas().findRegion("playerRed_walk4"));
        frames.add(screen.getAtlas().findRegion("playerRed_walk5"));
        playerRun = new Animation(0.1f,frames);
        frames.clear();
        frames.add(screen.getAtlas().findRegion("playerRed_up1"));
        frames.add(screen.getAtlas().findRegion("playerRed_up2"));
        frames.add(screen.getAtlas().findRegion("playerRed_up3"));
        playerJump = new Animation(0.1f,frames);
        frames.clear();
        frames.add(screen.getAtlas().findRegion("playerRed_stand"));
        playerStand = new Animation(0.1f,frames);
        definePlayer();
        playerStanding = new TextureRegion(screen.getAtlas().findRegion("playerRed_stand"));
        setBounds(0,0,64 / Game.PPM,64 / Game.PPM);
        setRegion(playerStanding);
        bullets = new Array<Bullet>();

    }

    public Player() {
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x  - getWidth()/2 ,b2body.getPosition().y - getHeight()/2 );
        setRegion(getFrame(dt));
        for(Bullet bullet : bullets){
            bullet.update(dt);
            if(bullet.isDestroyed())
                bullets.removeValue(bullet,true);
        }
    }
    public void draw(Batch batch){
        super.draw(batch);
        for(Bullet ball : bullets)
            ball.draw(batch);
    }

    public void fire(){
        if(disks>0){
            bullets.add(new Bullet(screen, b2body.getPosition().x +(!runningRight ? -25 / Game.PPM : 20 / Game.PPM), b2body.getPosition().y+(!runningRight ? -5 / Game.PPM : 5 / Game.PPM), runningRight));
            disks--;
        }
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case JUMPING:
                region = playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer,true);
                break;
            case STANDING:
                region = playerStand.getKeyFrame(stateTimer,true);
            default:
                region = playerStand.getKeyFrame(stateTimer,true);
                break;
        }
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void setRunningRight(Boolean runningRight) {
        this.runningRight = runningRight;
    }

    public State getState(){
        if(isDead)
            return State.DEAD;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y <0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x !=0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnX/ Game.PPM,spawnY/ Game.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30/ Game.PPM);
        fdef.filter.categoryBits = Game.PLAYER_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT
                | Game.COIN_BIT
                | Game.BRICK_BIT
                | Game.USED_BRICK_BIT
                | Game.BOX_BIT
                | Game.ITEM_BIT
                | Game.ENEMY_HEAD_BIT
                | Game.LEVEL_END_BIT
                | Game.ENEMY_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef);
        //create line for head collision detection
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-26/Game.PPM,30 / Game.PPM),new Vector2(27/Game.PPM,30 / Game.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");
    }

    public void die() {
        if (!isDead) {
            screen.getManager().get("audio/music/music.wav",Music.class).stop();
            screen.getManager().get("audio/sounds/game_over.wav", Sound.class).play(screen.getGame().getVolume());
            isDead=true;
            Filter filter = new Filter();
            filter.maskBits = Game.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }
    public void addDisks(){
        disks+=5;
    }
    public int getDisks(){
        return disks;
    }
}
