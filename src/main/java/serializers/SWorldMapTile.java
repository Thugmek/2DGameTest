package serializers;


import serializers.gameobjects.SWall;

import java.io.Serializable;

public class SWorldMapTile implements Serializable {
    public SWall wall = null;
    public SBiome biome;
    public SWorldMapTile(){

    }
}
