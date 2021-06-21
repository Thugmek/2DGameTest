package gui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import input.MouseInput;
import org.joml.Vector2f;
import runners.Game;
import window.Camera;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class DevStatsWindow {

    public static List<Float> fps = new LinkedList<>();
    public static Vector2f mousePos = new Vector2f(0,0);
    public static Vector2f screenPos = new Vector2f(0,0);

    public void render(){
        float[] myArray = new float[fps.size()];
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        if(fps.size()>600) fps.remove(0);
        for(int i = 0;i<fps.size();i++){
            myArray[i] = fps.get(i);
            if(myArray[i] < min) min = myArray[i];
            if(myArray[i] > max) max = myArray[i];
        }

        ImGui.begin("Dev stats");

        ImGui.plotLines("FPS",myArray,myArray.length);
        ImGui.labelText("Min",""+min);
        ImGui.labelText("Max",""+max);
        ImGui.labelText("Want capture mouse", ImGui.getIO().getWantCaptureMouse()?"true":"false");
        ImGui.labelText("Camera pos",String.format("[%f,%f]",Game.cam.getPos().x,Game.cam.getPos().y));
        ImGui.labelText("Click pos",String.format("[%f,%f]",mousePos.x,mousePos.y));
        ImGui.progressBar(0.5f,100,30,"Progress");
        ImGui.end();
    }
}
