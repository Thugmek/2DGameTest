package gameobjects.entities;

import gameobjects.Sprite;
import resources.ResourceManager;
import world.WorldMap;

public class Cat extends Entity {
    public Cat(WorldMap map) {
        super(new Sprite(ResourceManager.getTexture("cat")), map);
    }
}
