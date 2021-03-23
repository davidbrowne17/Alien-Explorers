package com.alienexplorers.game.Sprites;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Items.Disc;
import com.alienexplorers.game.Items.ItemDef;
import com.alienexplorers.game.Scenes.Hud;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

public class Brick extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private AssetManager manager;
    //tiled property id in tiled + 1
    private final int BLANK_BRICK_RED =101;
    private final int BLANK_BRICK_GREEN =151;
    public Brick(PlayScreen screen, MapObject object,AssetManager manager) {
        super(screen, object);
        this.manager=manager;
        this.map = screen.getMap();
        tileSet = map.getTileSets().getTileSet("tileset");
        fixture.setUserData(this);
        setCategoryFilter(Game.BRICK_BIT);
    }

    @Override
    public void onHit() {

    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(Game.USED_BRICK_BIT);
        if(getCell().getTile().getId()== BLANK_BRICK_RED || getCell().getTile().getId()== BLANK_BRICK_GREEN){
            manager.get("audio/sounds/low_bump.wav", Sound.class).play(screen.getGame().getVolume());
        }
        else{
            if(object.getProperties().containsKey("disc")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x,body.getPosition().y+64/Game.PPM) ,Disc.class));
                manager.get("audio/sounds/power_up.wav", Sound.class).play(screen.getGame().getVolume());
            }
            else{
                manager.get("audio/sounds/coin.wav", Sound.class).play(screen.getGame().getVolume());
                Hud.addScore(100);
            }
        }
        int colour = 0;
        if(screen.getLevel()<=5)
            colour=1;
        else if(screen.getLevel()<=30)
            colour=2;
        switch(colour){
            case 1:
                getCell().setTile(tileSet.getTile(BLANK_BRICK_RED));
                break;
            case 2:
                getCell().setTile(tileSet.getTile(BLANK_BRICK_GREEN));
                break;
            default:
                break;
        }
    }
}
