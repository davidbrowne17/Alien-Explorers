package com.alienexplorers.game.Tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class LevelLoader {
    private TiledMap map;

    public LevelLoader(int level){
        TmxMapLoader maploader = new TmxMapLoader();
        map = maploader.load(("levels/level_"+level+".tmx"));
    }
    public TiledMap getMap(){
        return map;
    }
}
