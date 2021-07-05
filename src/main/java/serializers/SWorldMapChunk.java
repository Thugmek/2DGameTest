package serializers;

import gameobjects.GameObject;
import world.WorldMapTile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class SWorldMapChunk {
    private int pos;

    private WorldMapTile[][] tiles;
    private List<GameObject> gameObjects = new ArrayList<>();

    public SWorldMapChunk(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public WorldMapTile[][] getTiles() {
        return tiles;
    }

    public void setTiles(WorldMapTile[][] tiles) {
        this.tiles = tiles;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}
