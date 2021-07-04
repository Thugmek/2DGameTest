package gameobjects.entities.entityStates;

import gameobjects.entities.Entity;
import org.joml.Vector2f;
import org.joml.Vector2i;
import world.WorldMap;

import java.util.List;

public class PathFollowingState implements EntityState {

    private Entity entity;
    private List<Vector2i> path;
    private WorldMap map;
    private static final float DELTA = 0.05f;

    public PathFollowingState(Entity entity, List<Vector2i> path, WorldMap map) {
        this.entity = entity;
        this.path = path;
        this.map = map;
    }

    @Override
    public void onUpdate(float delta) {

        if(path == null) {
            System.out.println("No path found!");
            entity.setState(new IdleState(entity,map));
            return;
        }

        for(int i = 1;i<Math.min(path.size(),3);i++){
            Vector2i p = path.get(i);
            if(map.getTile(p.x, p.y, false).wall != null){
                entity.setState(new FindingPathState(entity,path.get(path.size()-1),map));
                return;
            }
        }

        if (path.size() > 0 && entity.getPos().distance(path.get(0).x, path.get(0).y) < DELTA) {
            path.remove(0);
        }
        if (path.size() > 0) {
            Vector2i currentPoint = path.get(0);
            Vector2f move = new Vector2f(currentPoint.x - entity.getPos().x, currentPoint.y - entity.getPos().y);
            if (move.length() > DELTA) {
                move.normalize().mul(delta * entity.getSpeed());
            }
            entity.getPos().add(move);
        } else {
            entity.setState(new IdleState(entity,map));
        }
    }
}
