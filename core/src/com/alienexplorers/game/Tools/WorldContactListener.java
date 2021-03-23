package com.alienexplorers.game.Tools;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Items.Item;
import com.alienexplorers.game.Sprites.Bullet;
import com.alienexplorers.game.Sprites.Coin;
import com.alienexplorers.game.Sprites.Enemy;
import com.alienexplorers.game.Sprites.InteractiveTileObject;
import com.alienexplorers.game.Sprites.LevelEnd;
import com.alienexplorers.game.Sprites.Player;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData()=="head"||fixB.getUserData()=="head"){
            Fixture head = fixA.getUserData()=="head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            if(object.getUserData()!=null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onHeadHit();
            }
        }
        if(fixA.getUserData()!=null && Coin.class.isAssignableFrom(fixA.getUserData().getClass())){
            ((Coin)fixA.getUserData()).onHit();
        }else if(fixB.getUserData()!=null && Coin.class.isAssignableFrom(fixB.getUserData().getClass())){
            ((Coin)fixB.getUserData()).onHit();
        }
        enemyCollisions(fixA,fixB,cDef);
        itemCollisions(fixA,fixB,cDef);
        levelEndCollision(fixA,fixB,cDef);
    }

    public void levelEndCollision(Fixture fixA, Fixture fixB, int cDef){
        switch(cDef){
            case Game.LEVEL_END_BIT | Game.PLAYER_BIT:
                if(fixA.getUserData()!= "head"&&fixB.getUserData()!= "head"){
                    if(fixA.getFilterData().categoryBits==Game.LEVEL_END_BIT)
                        ((LevelEnd)(fixA.getUserData())).onHit();
                    else
                        ((LevelEnd)(fixB.getUserData())).onHit();
                }
                break;
            default:
                break;
        }
    }

    public void itemCollisions(Fixture fixA, Fixture fixB, int cDef){
        switch(cDef){
            case Game.BULLET_BIT | Game.BRICK_BIT:
                if(fixA.getFilterData().categoryBits==Game.BULLET_BIT)
                    ((Bullet)(fixA.getUserData())).setToDestroy();
                else
                    ((Bullet)(fixB.getUserData())).setToDestroy();
                break;
            case Game.BULLET_BIT | Game.BOX_BIT:
                if(fixA.getFilterData().categoryBits==Game.BULLET_BIT)
                    ((Bullet)(fixA.getUserData())).setToDestroy();
                else
                    ((Bullet)(fixB.getUserData())).setToDestroy();
                break;
            case Game.ITEM_BIT | Game.BRICK_BIT:
            case Game.ITEM_BIT | Game.BOX_BIT:
                if(fixA.getFilterData().categoryBits==Game.ITEM_BIT)
                    ((Item)(fixA.getUserData())).reverseVelocity(true,false);
                else
                    ((Item)(fixB.getUserData())).reverseVelocity(true,false);
                break;
            case Game.ITEM_BIT | Game.PLAYER_BIT:
                if(fixA.getUserData()!= "head"&&fixB.getUserData()!= "head"){
                    if(fixA.getFilterData().categoryBits==Game.ITEM_BIT)
                        ((Item)(fixA.getUserData())).use((Player) fixB.getUserData());
                    else
                        ((Item)(fixB.getUserData())).use((Player) fixA.getUserData());
                }
                break;
    }
}

    public void enemyCollisions(Fixture fixA, Fixture fixB, int cDef){
        switch(cDef){
            case Game.ENEMY_HEAD_BIT | Game.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_HEAD_BIT)
                    ((Enemy)(fixA.getUserData())).hitOnHead();
                else
                    ((Enemy)(fixB.getUserData())).hitOnHead();
                break;
            case Game.ENEMY_BIT | Game.BOX_BIT:
            case Game.ENEMY_BIT | Game.BRICK_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_BIT)
                    ((Enemy)(fixA.getUserData())).reverseVelocity(true,false);
                else
                    ((Enemy)(fixB.getUserData())).reverseVelocity(true,false);
                break;
            case Game.ENEMY_BIT | Game.ENEMY_BIT:
                ((Enemy)(fixA.getUserData())).reverseVelocity(true,false);
                ((Enemy)(fixB.getUserData())).reverseVelocity(true,false);
                break;
            case Game.PLAYER_BIT | Game.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_BIT)
                    ((Enemy)(fixA.getUserData())).OnHit();
                else
                    ((Enemy)(fixB.getUserData())).OnHit();
                break;
            case Game.BULLET_BIT | Game.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_BIT){
                    ((Enemy)(fixA.getUserData())).hitOnHead();
                    ((Bullet)(fixB.getUserData())).setToDestroy();}
                else{
                    ((Bullet)(fixA.getUserData())).setToDestroy();
                    ((Enemy)(fixB.getUserData())).hitOnHead();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
