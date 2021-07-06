package world;

import gameobjects.Wall;

import java.io.Serializable;

public class WorldMapTile implements Serializable {
    public Wall wall = null;
    public Biome biome;
    public WorldMapTile(){

    }
}
