package serializers;

import gameobjects.GameObject;
import serializers.gameobjects.SGameObject;
import world.WorldMapTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SWorldMapChunk implements Serializable {
    private int pos;

    private SWorldMapTile[][] tiles;
    private List<SGameObject> gameObjects = new ArrayList<>();

    public SWorldMapChunk(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public SWorldMapTile[][] getTiles() {
        return tiles;
    }

    public void setTiles(SWorldMapTile[][] tiles) {
        this.tiles = tiles;
    }

    public List<SGameObject> getGameObjects() {
        return gameObjects;
    }
}
