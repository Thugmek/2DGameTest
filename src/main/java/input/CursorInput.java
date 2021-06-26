package input;

import gameobjects.Cursor;
import gameobjects.GameObject;
import gameobjects.entities.Entity;
import gui.DevStatsWindow;
import gui.Gui;
import gui.RightClickMenu;
import org.joml.Vector2f;
import org.joml.Vector2i;
import runners.Game;
import world.WorldMap;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class CursorInput {

    private static boolean lastState = false;

    private static Vector2i pos = new Vector2i();
    private static Cursor cursor = new Cursor();
    private static WorldMap map;

    public static void init(WorldMap map){
        CursorInput.map = map;
    }

    public static void update(){
        boolean state = MouseInput.getMouseButton(GLFW_MOUSE_BUTTON_1) == 1;
        boolean state2 = MouseInput.getMouseButton(GLFW_MOUSE_BUTTON_2) == 1;
        if(state){
            if(!lastState && !Gui.isCaptureMouse()){
                primaryCursorUpdate();
            }
        }

        if(state2){
            if(!lastState && !Gui.isCaptureMouse()){
                secondaryCursorUpdate();
            }
        }

        lastState = state||state2;
    }

    private static void primaryCursorUpdate(){
        Vector2f mousePos = MouseInput.getPos();
        Vector2f v = Game.cam.screenPosToWorld(mousePos);
        pos = new Vector2i((int)Math.floor(v.x),(int)Math.floor(v.y));

        List<GameObject> go = map.getChunkByPos(pos.x,pos.y).getGameObjects();

        GameObject nearest = getObject(v);

        if(nearest != null)
        cursor.toObject(nearest);

        DevStatsWindow.mousePos = v;
        RightClickMenu.setActive(false);
    }
    private static void secondaryCursorUpdate(){
        cursor.toNone();
        Vector2f mousePos = MouseInput.getPos();
        RightClickMenu.open((int)mousePos.x,(int)mousePos.y);
    }

    private static GameObject getObject(Vector2f v){
        List<GameObject> go = map.getChunkByPos(pos.x,pos.y).getGameObjects();

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

    public static void render(){
        //glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_3D,texture.getId());
        cursor.render();
    }
}
