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

    public static int toDrop = 30;

    public static void render(){
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        float avg = 0;
        if(toDrop > 0){
            fps.remove(0);
            toDrop--;
        }
        if(fps.size()>600) fps.remove(0);
        int n = fps.size();
        float[] myArray = new float[n];
        for(int i = 0;i<n;i++){
            myArray[i] = fps.get(i);
            if(myArray[i] > 80 || myArray[i] < 2) fps.remove(i);
            if(myArray[i] < min) min = myArray[i];
            if(myArray[i] > max) max = myArray[i];
            avg += myArray[i];
        }
        avg /= n;

        ImGui.begin("Dev stats");

        ImGui.plotLines("FPS",myArray,n,0,"",0,65,400,200);
        ImGui.labelText("Min",""+min);
        ImGui.labelText("Max",""+max);
        ImGui.labelText("Avg",""+avg);
        ImGui.labelText("Camera pos",String.format("[%f,%f]",Game.cam.getPos().x,Game.cam.getPos().y));
        ImGui.labelText("Camera zoom",String.format("%f",Game.cam.getScale()));
        ImGui.end();
    }
}
