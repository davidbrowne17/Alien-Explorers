package com.alienexplorers.game.Sprites;

import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    protected boolean destroyed;
    public Enemy (PlayScreen screen,float x, float y){
        this.screen = screen;
        world = screen.getWorld();
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(0,0);
        b2body.setActive(false);
    }
    public abstract void update(float dt);
    protected abstract void defineEnemy();
    public abstract void hitOnHead();
    public abstract void OnHit();
    public void reverseVelocity(boolean x,boolean y){
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;
    }
}
