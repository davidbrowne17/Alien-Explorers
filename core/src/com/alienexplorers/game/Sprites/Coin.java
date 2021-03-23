package com.alienexplorers.game.Sprites;

import com.alienexplorers.game.Game;
import com.alienexplorers.game.Scenes.Hud;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private AssetManager manager;
    //tiled property id in tiled + 1
    private final int BLANK_COIN =152;
    public Coin(PlayScreen screen, MapObject object,AssetManager manager) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset");
        this.manager = manager;
        fixture.setUserData(this);
        fixture.setSensor(true);
        setCategoryFilter(Game.COIN_BIT);
    }

    @Override
    public void onHit() {
        setCategoryFilter(Game.DESTROYED_BIT);
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        manager.get("audio/sounds/coin.wav", Sound.class).play(screen.getGame().getVolume());
        Hud.addScore(10);
    }

    @Override
    public void onHeadHit() {

    }

}
