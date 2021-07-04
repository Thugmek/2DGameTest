package gameobjects;

import resources.ResourceManager;
import resources.Texture;

public enum Wall {

    GRANITE(300,"wallsGranite"),
    SANDSTONE(100, "wallsSandstone");

    public final int durability;
    public final Texture texture;

    Wall(int durability, String tex){
        this.durability = durability;
        texture = ResourceManager.getTexture(tex);
    }
}
