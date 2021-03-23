package com.alienexplorers.game.Sprites;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Boss extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy,runningRight=false;
    private PlayScreen screen;
    public Boss(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.screen = screen;
        frames = new Array<TextureRegion>();
        for(int i=0; i<6;i++){
            frames.add(screen.getAtlas().findRegion("playerGrey_stand"));
            frames.add(screen.getAtlas().findRegion("playerGrey_walk1"));
            frames.add(screen.getAtlas().findRegion("playerGrey_walk2"));
            frames.add(screen.getAtlas().findRegion("playerGrey_walk3"));
            frames.add(screen.getAtlas().findRegion("playerGrey_walk4"));
            frames.add(screen.getAtlas().findRegion("playerGrey_walk5"));
        }
        walkAnimation = new Animation(.1f,frames);
        stateTime= 0;
        setBounds(getX(),getY(),64/Game.PPM,64/Game.PPM);
        setToDestroy=false;
        destroyed=false;
        velocity = new Vector2(-2,0);
    }

    public void update(float dt){
        stateTime+=dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed=true;
            setRegion(screen.getAtlas().findRegion("playerGrey_dead"));
            stateTime=0;
        }
        if(!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x- getWidth()/2, b2body.getPosition().y- getHeight()/2);
            setRegion( flip(walkAnimation.getKeyFrame(stateTime,true)));
        }
    }
    public TextureRegion flip(TextureRegion region){
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
        }
        return region;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30/ Game.PPM);
        fdef.filter.categoryBits = Game.ENEMY_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT
                | Game.USED_BRICK_BIT
                | Game.PLAYER_BIT
                | Game.GROUND_BIT
                | Game.BRICK_BIT
                | Game.USED_BRICK_BIT
                | Game.ITEM_BIT
                | Game.BOX_BIT
                | Game.BULLET_BIT
                | Game.ENEMY_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        //head for enemy
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-25,40).scl(1/Game.PPM);
        vertice[1] = new Vector2(25,40).scl(1/Game.PPM);
        vertice[2] = new Vector2(-25,26).scl(1/Game.PPM);
        vertice[3] = new Vector2(25,26).scl(1/Game.PPM);
        head.set(vertice);
        fdef.shape = head;
        fdef.restitution = 1f;
        fdef.filter.categoryBits = Game.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = Game.PLAYER_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 3){
            super.draw(batch);
        }
    }

    @Override
    public void reverseVelocity(boolean x, boolean y) {
        super.reverseVelocity(x, y);
        if(runningRight)
            runningRight=false;
        else
            runningRight=true;
    }

    @Override
    public void OnHit() {
        if(!setToDestroy)
            screen.getPlayer().die();
    }

    @Override
    public void hitOnHead() {
        setToDestroy=true;
        screen.getManager().get("audio/sounds/splat.wav", Sound.class).play(screen.getGame().getVolume());
        screen.getPlayer().b2body.applyLinearImpulse(new Vector2(0,8f),screen.getPlayer().b2body.getWorldCenter(), true);
    }
}
