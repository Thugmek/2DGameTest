package gameobjects.entities.entityStates;

import gameobjects.entities.Entity;
import org.joml.Vector2i;
import runners.Game;
import world.WorldMap;

public class IdleState implements EntityState{

    private Entity entity;
    private WorldMap map;

    public IdleState(Entity entity, WorldMap map){
        this.entity = entity;
        this.map = map;
    }

    @Override
    public void onUpdate(float delta) {
        if(Game.ran.nextFloat()>0.95) {
            entity.setState(new FindingPathState(entity, new Vector2i(Game.ran.nextInt(100), Game.ran.nextInt(100)), map));
        }
    }
}
