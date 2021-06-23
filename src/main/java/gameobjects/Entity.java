package gameobjects;


import org.joml.Vector2f;
import org.joml.Vector2i;
import runners.Game;
import world.AStarAlg;
import world.WorldMap;

import java.util.List;
import java.util.concurrent.Future;

public class Entity {
    private Sprite sprite;
    private WorldMap map;
    private LinePath l;

    private Future<List<Vector2i>> path;

    private boolean isOnWay = false;

    private static final float DELTA = 0.05f;

    public Entity(Sprite sprite, WorldMap map){
        this.sprite = sprite;
        this.map = map;
    }

    /*public void goTo(Vector2i pos){
        AStarAlg a = new AStarAlg(new Vector2i(Math.round(sprite.getPos().x),Math.round(sprite.getPos().y)),pos,map);
        path = a.getResult();
        isOnWay = true;
        l = new LinePath(path);
    }*/

    public void goTo(Vector2i pos){
        path = Game.executor.submit(() -> {
                AStarAlg a = new AStarAlg(new Vector2i(Math.round(sprite.getPos().x),Math.round(sprite.getPos().y)),pos,map);
                return a.getResult();
        });
        isOnWay = true;
    }

    public void update(float delta){

        if(false)
        return;

        if(path != null && !path.isDone()){
            return;
        }

        try {

            if (path != null && path.get().size() > 0 && sprite.getPos().distance(path.get().get(0).x, path.get().get(0).y) < DELTA) {
                path.get().remove(0);
                l = new LinePath(path.get());
            }
            if (path != null && path.get().size() > 0) {
                Vector2i currentPoint = path.get().get(0);
                Vector2f move = new Vector2f(currentPoint.x - sprite.getPos().x, currentPoint.y - sprite.getPos().y);
                if (move.length() > DELTA) {
                    move.normalize().mul(delta * 2);
                }


                //System.out.println(String.format("[%d,%d][%f,%f][%f,%f]",currentPoint.x,currentPoint.y,move.x,move.y,sprite.getPos().x,sprite.getPos().y));
                sprite.getPos().add(move);
            } else {
                if (path != null) {
                    path = null;
                } else {
                    isOnWay = false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void render(){
        //if(l != null) l.render();
        sprite.render();
    }

    public boolean isOnWay(){
        return isOnWay;
    }

    public Vector2f getPos(){
        return sprite.getPos();
    }
}
