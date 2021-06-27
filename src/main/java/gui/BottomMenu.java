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
            ImGui.beginGroup();
                ImGui.button("click me 1");
                ImGui.button("click me 2 long");
            ImGui.endGroup();

            ImGui.sameLine();
            ImGui.beginGroup();
                ImGui.button("click me 3 long");
            ImGui.endGroup();

            ImGui.sameLine();
            ImGui.beginGroup();
                ImGui.button("click me 4");
            ImGui.endGroup();

            ImGui.sameLine();
            ImGui.beginGroup();
                ImGui.button("click me 5");
                ImGui.button("click me 6 long");
                ImGui.button("click me 7");
            ImGui.endGroup();
        ImGui.end();
    }
}
