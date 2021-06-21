package input;

import gameobjects.Cursor;
import gui.DevStatsWindow;
import gui.Gui;
import gui.RightClickMenu;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Texture;
import runners.Game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class CursorInput {

    private static boolean lastState = false;

    private static Vector2i pos = new Vector2i();
    private static Cursor cursor = new Cursor();
    private static Texture texture;

    public static void init(){
        texture = ResourceManager.getTexture("cursor");
    }

    public static void update(){
        boolean state = MouseInput.getMouseButton(GLFW_MOUSE_BUTTON_1) == 1;
        boolean state2 = MouseInput.getMouseButton(GLFW_MOUSE_BUTTON_2) == 1;
        if(state){
            if(!lastState && !Gui.isCaptureMouse()){
                Vector2f mousePos = MouseInput.getPos();
                Vector2f v = Game.cam.screenPosToWorld(mousePos);
                pos = new Vector2i((int)Math.floor(v.x),(int)Math.floor(v.y));
                cursor.setPos(new Vector2f(pos.x,pos.y));
                DevStatsWindow.mousePos = v;
                RightClickMenu.setActive(false);
            }
        }

        if(state2){
            if(!lastState && !Gui.isCaptureMouse()){
                Vector2f mousePos = MouseInput.getPos();
                RightClickMenu.open((int)mousePos.x,(int)mousePos.y);
            }
        }

        lastState = state||state2;
    }

    public static void render(){
        glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D,texture.getId());
        cursor.render();
    }
}
