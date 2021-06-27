package input;

import gameobjects.Cursor;
import gameobjects.GameObject;
import gameobjects.entities.Entity;
import gui.DevStatsWindow;
import gui.EntityDetailWindow;
import gui.Gui;
import gui.RightClickMenu;
import input.selectionModes.EntitySelectionMode;
import input.selectionModes.SelectionMode;
import org.joml.Vector2f;
import org.joml.Vector2i;
import runners.Game;
import world.WorldMap;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class CursorInput {

    private static final float MIN_SELECTION = 30;

    private static boolean lastState1 = false;
    private static boolean lastState2 = false;

    private static Vector2i pos = new Vector2i();
    private static List<Cursor> cursors = new ArrayList<>();
    private static WorldMap map;
    private static SelectionMode selectionMode = new EntitySelectionMode(cursors);

    private static Vector2f startPos;

    public static void init(WorldMap map){
        CursorInput.map = map;
    }

    public static void update(){
        boolean state = MouseInput.getMouseButton(GLFW_MOUSE_BUTTON_1) == 1;
        boolean state2 = MouseInput.getMouseButton(GLFW_MOUSE_BUTTON_2) == 1;

        if(state){
            if(!lastState1 && !Gui.isCaptureMouse()){
                startPos = MouseInput.getPos();
            }
        }
        if(!state){
            if(lastState1 && !Gui.isCaptureMouse()){
                Vector2f endPos = MouseInput.getPos();
                if(startPos.distance(endPos) > 30){
                    cursors.removeAll(cursors);
                    selectionMode.primaryCursorDrag(startPos,endPos);
                }else {
                    cursors.removeAll(cursors);
                    selectionMode.primaryCursorClick(endPos);
                }
            }
        }

        if(!state2){
            if(lastState2 && !Gui.isCaptureMouse()){
                selectionMode.secondaryCursorClick(MouseInput.getPos());
            }
        }

        lastState1 = state;
        lastState2 = state2;
    }

    public static void render(){
        for(Cursor cursor:cursors)
            cursor.render();
    }
}
