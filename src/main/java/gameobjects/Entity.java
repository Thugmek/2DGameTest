package gameobjects;


import gameobjects.entities.entityStates.EntityState;
import gameobjects.entities.entityStates.FindingPathState;
import org.joml.Vector2f;
import org.joml.Vector2i;
import runners.Game;
import world.AStarAlg;
import world.WorldMap;

import java.util.List;
import java.util.concurrent.Future;

public class Entity implements GameObject {
    private Sprite sprite;
    private WorldMap map;
    private LinePath l;

    private Future<List<Vector2i>> path;
    private boolean isOnWay = false;

    private EntityState state;

    public Entity(Sprite sprite, WorldMap map){
        this.sprite = sprite;
        this.map = map;
    }

    public void setState(EntityState state){
        this.state = state;
    }

    public void goTo(Vector2i pos){
        path = Game.executor.submit(() -> {
                AStarAlg a = new AStarAlg(new Vector2i(Math.round(sprite.getPos().x),Math.round(sprite.getPos().y)),pos,map);
                return a.getResult();
        });
        isOnWay = true;
    }

    public void update(float delta){
        state.onUpdate(delta);
    }

    public void render(){
        sprite.render();
    }

    public boolean isOnWay(){
        return isOnWay;
    }

    public Vector2f getPos(){
        return sprite.getPos();
    }
}
