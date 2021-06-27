package gui;

import gameobjects.GameObject;
import gameobjects.entities.Entity;
import gameobjects.entities.entityStates.FindingPathState;
import imgui.ImGui;
import org.joml.Vector2i;
import runners.Game;

public class EntityDetailWindow {
    public static GameObject go;

    public static void render(){

        if(go != null) {
            ImGui.begin("Entity detail");
            ImGui.labelText("Entity class", go.getClass().getName());
            ImGui.labelText("Entity state", ((Entity)go).getState().toString());
            if(ImGui.button("Go!")){
                ((Entity)go).setState(new FindingPathState((Entity)go,new Vector2i(-5,-5), Game.map));
            }
            ImGui.end();
        }
    }
}
