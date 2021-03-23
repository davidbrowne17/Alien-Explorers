package com.alienexplorers.game.Sprites;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Bullet extends Sprite {
    private PlayScreen screen;
    private World world;
    private Array<TextureRegion> frames;
    private Animation<TextureRegion> fireAnimation;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private boolean fireRight;
    private Body b2body;
    public Bullet(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();
        frames.add(screen.getAtlas().findRegion("discGreen"));
        fireAnimation = new Animation(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 64 / Game.PPM, 64 / Game.PPM);
        defineBullet();
    }

    public void defineBullet(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 /Game.PPM : getX() - 12 /Game.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30/ Game.PPM);
        fdef.filter.categoryBits = Game.BULLET_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT |
                Game.BRICK_BIT |
                Game.ENEMY_BIT |
                Game.BOX_BIT;
        fdef.shape = shape;
        fdef.restitution = 1f;
        fdef.friction = 0f;
        b2body.createFixture(fdef).setUserData(this);
        int velocity;
        if(fireRight)
            velocity=8;
        else
            velocity=-13;
        b2body.setLinearVelocity(new Vector2(velocity, 2.5f));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
