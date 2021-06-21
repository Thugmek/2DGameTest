package gui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import input.KeyboardInput;
import runners.Game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class RightClickMenu {

    private static ImBoolean active = new ImBoolean(true);
    private static int x = 0;
    private static int y = 0;

    public static void render(){
        if(!active.get()) return;

        int menuWidth = 400;
        int menuHeight = 200;

        ImGui.setNextWindowPos(x,y);
        ImGui.setNextWindowSize(menuWidth,menuHeight);

        ImGui.begin("RightClickMenu",active, ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse|ImGuiWindowFlags.NoDecoration);
        ImGui.button("Button");

        ImGui.end();
    }

    public static void open(int x, int y){
        RightClickMenu.x = x;
        RightClickMenu.y = y;

        active.set(true);
    }

    public static void setActive(boolean b){
        active.set(b);
    }
}
