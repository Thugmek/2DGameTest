package input.selectionModes;

import gameobjects.Cursor;
import gameobjects.GameObject;
import gameobjects.entities.Entity;
import gui.DevStatsWindow;
import gui.EntityDetailWindow;
import gui.RightClickMenu;
import input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector2i;
import runners.Game;

import java.util.List;

public abstract class SelectionMode {

    protected List<Cursor> cursors;

    public SelectionMode(List<Cursor> cursors){
        this.cursors = cursors;
    }

    public void primaryCursorClick(Vector2f mousePos){
    }
    public void secondaryCursorClick(Vector2f mousePos){
    }
    public void primaryCursorDrag(Vector2f mousePos, Vector2f mousePos2){
    }
    public void secondaryCursorDrag(Vector2f mousePos, Vector2f mousePos2){
    }

    protected GameObject getObject(Vector2f v){
        List<GameObject> go = Game.map.getChunkByPos(v.x,v.y).getGameObjects();

        float min = 100;
        Entity nearest = null;
        for(GameObject g:go){
            if(g.getPos().distance(v) < min){
                min = g.getPos().distance(v);
                nearest = (Entity) g;
            }
        }

        return nearest;
    }
}