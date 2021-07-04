package gui;

import gameobjects.Wall;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import input.CursorInput;
import input.selectionModes.EntitySelectionMode;
import input.selectionModes.WallBuilderSelectionMode;
import runners.Game;

public class BottomMenu {

    public static void render(){
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        ImGui.setNextWindowPos(0,height-100);
        ImGui.setNextWindowSize(width,100);
        ImGui.begin("Bottom", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoDecoration);
            if(ImGui.button("Wall edit mode")){
                CursorInput.setSelectionMode(new WallBuilderSelectionMode());
            }
            if(ImGui.beginPopupContextItem()){
                if(ImGui.button("Sandstone")){
                    WallBuilderSelectionMode.wall = Wall.SANDSTONE;
                    ImGui.closeCurrentPopup();
                }
                if(ImGui.button("Granite")){
                    WallBuilderSelectionMode.wall = Wall.GRANITE;
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }
            ImGui.sameLine();
            if(ImGui.button("Entity select mode")){
                CursorInput.setSelectionMode(new EntitySelectionMode());
            }
        ImGui.end();
        ImGui.showDemoWindow();
    }
}
