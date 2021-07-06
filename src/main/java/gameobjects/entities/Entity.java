package gameobjects.entities;


import gameobjects.GameObject;
import gameobjects.Sprite;
import gameobjects.entities.entityStates.EntityState;
import gameobjects.entities.entityStates.IdleState;
import org.joml.Vector2f;
import runners.Game;
import serializers.gameobjects.SEntity;
import serializers.gameobjects.SGameObject;
import world.WorldMap;

public class Entity implements GameObject {
    private Sprite sprite;
    private float speed = 1;

    private EntityState state;

    public Entity(Sprite sprite){
        this.sprite = sprite;
        state = new IdleState(this, Game.map);
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

    public void setPos(Vector2f pos){ sprite.setPos(pos);}

    @Override
    public SGameObject serialize() {
        return new SEntity(sprite.getTextureName(), sprite.getPos(),speed);
    }
}
