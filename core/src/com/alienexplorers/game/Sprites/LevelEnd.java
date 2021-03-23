package com.alienexplorers.game.Sprites;
import com.alienexplorers.game.Game;
import com.alienexplorers.game.Screens.PlayScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;

public class LevelEnd extends InteractiveTileObject {
    private AssetManager manager;
    public LevelEnd(PlayScreen screen, MapObject object,AssetManager manager) {
        super(screen, object);
        this.manager=manager;
        this.map = screen.getMap();
        fixture.setUserData(this);
        setCategoryFilter(Game.LEVEL_END_BIT);
    }

    @Override
    public void onHit() {
        screen.finishLevel();
    }

    @Override
    public void onHeadHit() {

    }
}
