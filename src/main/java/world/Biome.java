package world;

import resources.Texture;

import java.io.Serializable;

public enum Biome implements Serializable {
    DESERT,
    FORREST,
    MEADOW,
    SNOW;

    public Texture texture;
}
