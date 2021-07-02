package gui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import runners.Game;

public class BottomMenu {

    public void render(){
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        ImGui.setNextWindowPos(0,height-100);
        ImGui.setNextWindowSize(width,100);
        ImGui.begin("Bottom", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoDecoration);
            ImGui.button("click me 1");
            ImGui.sameLine();
            ImGui.button("click me 1");
            ImGui.sameLine();
            ImGui.button("click me 1");
            ImGui.sameLine();
            ImGui.button("click me 1");
            ImGui.sameLine();
        ImGui.end();
    }
}
