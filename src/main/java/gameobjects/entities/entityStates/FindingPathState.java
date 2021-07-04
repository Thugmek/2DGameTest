package gameobjects.entities.entityStates;

import gameobjects.entities.Entity;
import org.joml.Vector2i;
import runners.Game;
import world.AStarAlg;
import world.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FindingPathState implements EntityState {

    private Entity entity;
    private WorldMap map;
    private Future<List<Vector2i>> path;

    public FindingPathState(Entity entity, Vector2i pos, WorldMap map){
        this.entity = entity;
        this.map = map;
        path = Game.executor.submit(() -> {
            AStarAlg a = null;
            try {
                a = new AStarAlg(new Vector2i(Math.round(entity.getPos().x), Math.round(entity.getPos().y)), pos, map);
                return a.getResult();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            return new ArrayList<>();
        });
    }

    @Override
    public void onUpdate(float delta) {
        //if(path.isDone()) {
            try {
                path.get();
                if(path.isDone())
                entity.setState(new PathFollowingState(entity, path.get(),map));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        //}
    }
}
