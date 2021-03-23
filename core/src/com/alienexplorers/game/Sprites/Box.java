package com.alienexplorers.game.Sprites;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;


public class Box extends InteractiveTileObject {
    private AssetManager manager;
    public Box(PlayScreen screen, MapObject object,AssetManager manager) {
        super(screen, object);
        this.manager = manager;
        fixture.setUserData(this);
        setCategoryFilter(Game.BOX_BIT);
    }

    @Override
    public void onHit() {

    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(Game.DESTROYED_BIT);
        manager.get("audio/sounds/bump.mp3", Sound.class).play(screen.getGame().getVolume());
        getCell().setTile(null);
    }
}
