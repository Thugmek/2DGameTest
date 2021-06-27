package gameobjects.entities;


import gameobjects.GameObject;
import gameobjects.Sprite;
import gameobjects.entities.entityStates.EntityState;
import org.joml.Vector2f;
import world.WorldMap;

public class Entity implements GameObject {
    private Sprite sprite;
    private WorldMap map;
    private float speed = 1;

    private EntityState state;

    public Entity(Sprite sprite, WorldMap map){
        this.sprite = sprite;
        this.map = map;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setState(EntityState state){
        this.state = state;
    }

    public EntityState getState() {
        return state;
    }

    public void update(float delta){
        state.onUpdate(delta);
    }

    public void render(){
        sprite.render();
    }

    public Vector2f getPos(){
        return sprite.getPos();
    }
}
