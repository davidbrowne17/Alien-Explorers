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

public class AlienEnemy extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private PlayScreen screen;
    public AlienEnemy(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.screen = screen;
        frames = new Array<TextureRegion>();
        for(int i=0; i<3;i++){
            frames.add(screen.getAtlas().findRegion("enemyWalking1"));
            frames.add(screen.getAtlas().findRegion("enemyWalking2"));
            frames.add(screen.getAtlas().findRegion("enemyWalking3"));
        }
        walkAnimation = new Animation(.2f,frames);
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
            setRegion(screen.getAtlas().findRegion("enemyWalking4"));
            stateTime=0;
        }
        if(!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x- getWidth()/2, b2body.getPosition().y- getHeight()/2);
            setRegion( walkAnimation.getKeyFrame(stateTime,true));
        }
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
        vertice[0] = new Vector2(-28,40).scl(1/Game.PPM);
        vertice[1] = new Vector2(28,40).scl(1/Game.PPM);
        vertice[2] = new Vector2(-28,26).scl(1/Game.PPM);
        vertice[3] = new Vector2(28,26).scl(1/Game.PPM);
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
