package serializers;

import gameobjects.Wall;
import serializers.gameobjects.SWall;
import world.Biome;

public class EnumConvertor {
    public static SBiome serializeBiome(Biome biome){
        switch (biome){
            case SNOW:
                return SBiome.SNOW;
            case DESERT:
                return SBiome.DESERT;
            case MEADOW:
                return SBiome.MEADOW;
            case FORREST:
                return SBiome.FORREST;
            default:
                return null;
        }
    }
    public static Biome deserializeBiome(SBiome biome){
        switch (biome){
            case SNOW:
                return Biome.SNOW;
            case DESERT:
                return Biome.DESERT;
            case MEADOW:
                return Biome.MEADOW;
            case FORREST:
                return Biome.FORREST;
            default:
                return null;
        }
    }

    public static SWall serializeWall(Wall wall){
        if(wall == null) return null;
        switch (wall){
            case GRANITE:
                return SWall.GRANITE;
            case SANDSTONE:
                return SWall.SANDSTONE;
            default:
                return null;
        }
    }
    public static Wall deserializeWall(SWall wall){
        if(wall == null) return null;
        switch (wall){
            case GRANITE:
                return Wall.GRANITE;
            case SANDSTONE:
                return Wall.SANDSTONE;
            default:
                return null;
        }
    }
}
