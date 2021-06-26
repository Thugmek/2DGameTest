package gameobjects.entities.entityStates;

import gameobjects.Entity;
import gameobjects.LinePath;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.List;

public class PathFollowingState implements EntityState {

    private Entity entity;
    private List<Vector2i> path;
    private static final float DELTA = 0.05f;

    public PathFollowingState(Entity entity, List<Vector2i> path) {
        this.entity = entity;
        this.path = path;
    }

    @Override
    public void onUpdate(float delta) {
        if (path.size() > 0 && entity.getPos().distance(path.get(0).x, path.get(0).y) < DELTA) {
            path.remove(0);
        }
        if (path.size() > 0) {
            Vector2i currentPoint = path.get(0);
            Vector2f move = new Vector2f(currentPoint.x - entity.getPos().x, currentPoint.y - entity.getPos().y);
            if (move.length() > DELTA) {
                move.normalize().mul(delta * 2);
            }
            entity.getPos().add(move);
        } else {
            //end of path;
        }
    }
}
