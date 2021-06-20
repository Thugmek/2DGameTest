package gui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import input.KeyboardInput;
import runners.Game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class PauseMenu {
    private static ImBoolean active = new ImBoolean(true);
    private static boolean deadkey = false;

    public void render(){
        if(KeyboardInput.getKey(GLFW_KEY_ESCAPE) == 1){
            if(!deadkey) {
                active.set(!active.get());
                deadkey = true;
            }
        }else{
            deadkey = false;
        }
        if(!active.get()) return;
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();

        int menuWidth = 400;
        int menuHeight = 600;

        ImGui.setNextWindowPos(width/2-menuWidth/2,height/2-menuHeight/2);
        ImGui.setNextWindowSize(menuWidth,menuHeight);

        ImGui.begin("Paused",active,ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse);
        if(ImGui.button("Exit game")){
            glfwSetWindowShouldClose(Game.getWindow().getId(),true);
        }

        ImGui.end();
    }

    public static void setActive(){
        active.set(true);
    }

    public static boolean getPaused(){
        return active.get();
    }
}
