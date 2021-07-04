package gui;

import gameobjects.GameObject;
import gameobjects.entities.Entity;
import gameobjects.entities.entityStates.FindingPathState;
import imgui.ImGui;
import input.CursorInput;
import input.selectionModes.EntitySelectionMode;
import input.selectionModes.PositionSelectionMode;
import org.joml.Vector2i;
import runners.Game;

public class EntityDetailWindow {
    public static GameObject go;

    private static boolean state = true;

    public static void render(){

        if(go != null) {
            ImGui.begin("Entity detail");
            ImGui.labelText("Entity class", go.getClass().getName());
            ImGui.labelText("Entity state", ((Entity)go).getState().toString());
            if(state) {
                if (ImGui.button(" Go to ")) {
                    CursorInput.setSelectionMode(new PositionSelectionMode((Entity)go));
                    state = false;
                }
            }else{
                if (ImGui.button(">Go to<")) {
                    CursorInput.setSelectionMode(new EntitySelectionMode());
                    state = true;
                }
            }
            ImGui.end();
        }
    }
}
