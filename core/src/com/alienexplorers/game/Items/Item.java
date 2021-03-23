package com.alienexplorers.game.Items;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.alienexplorers.game.Sprites.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen , float x, float y){
        this.screen= screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        setBounds(getX(),getY(),64/ Game.PPM,64 / Game.PPM);
        defineItem();
        toDestroy=false;
        destroyed=false;
    }
    public void reverseVelocity(boolean x,boolean y){
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;
    }

    public abstract void defineItem();
    public abstract void use(Player player);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }
    public void destroy(){
        toDestroy=true;
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

}
