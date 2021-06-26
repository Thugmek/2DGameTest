package gameobjects.entities.entityStates;

import gameobjects.Entity;
import javafx.application.Application;
import org.joml.Vector2i;
import runners.Game;
import world.AStarAlg;
import world.WorldMap;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FindingPathState implements EntityState {

    private Entity entity;
    private Future<List<Vector2i>> path;

    public FindingPathState(Entity entity, Vector2i pos, WorldMap map){
        this.entity = entity;

        path = Game.executor.submit(() -> {
            AStarAlg a = null;
            try {
                a = new AStarAlg(new Vector2i(Math.round(entity.getPos().x), Math.round(entity.getPos().y)), pos, map);
            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
            return a.getResult();
        });
    }

    @Override
    public void onUpdate(float delta) {
        if(path.isDone()) {
            try {
                entity.setState(new PathFollowingState(entity, path.get()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
