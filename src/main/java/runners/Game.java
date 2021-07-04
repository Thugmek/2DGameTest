package runners;

import gameobjects.LinePath;
import gameobjects.Wall;
import gameobjects.entities.Cat;
import gameobjects.entities.Entity;
import gameobjects.entities.entityStates.FindingPathState;
import gameobjects.entities.entityStates.IdleState;
import gui.DevStatsWindow;
import gui.Gui;
import gui.PauseMenu;
import input.CursorInput;
import input.KeyboardInput;
import input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Shader;
import resources.TextureDefinition;
import util.GarbageCollectionUtils;
import util.TestWallsBuilder;
import window.Camera;
import window.GameState;
import window.LoadingGameState;
import window.Window;
import world.Biome;
import world.WorldMap;
import imgui.app.Configuration;
import world.WorldMapChunk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Game {

    public static Random ran = new Random();
    private static Window window;
    public static Camera cam;
    public static ThreadPoolExecutor executor;
    public static Shader shader;
    public static Properties props = new Properties();
    public static WorldMap map;
    public static GameState gameState;

    public static void main(String[] args) {

        try (FileInputStream fis = new FileInputStream("src/main/resources/app.config")) {
            props.load(fis);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);

        Thread.currentThread().setPriority(10);

        Window w = new Window();
        window = w;
        KeyboardInput.setWindow(w.getId());
        MouseInput.init(w.getId());
        ResourceManager.loadShader("shader","shaders/shader.vs","shaders/shader.fs");
        ResourceManager.getShader("shader").bind();

        map = new WorldMap();
        Gui.init(new Configuration(),w.getId());
        cam = new Camera();
        gameState = new LoadingGameState();

        //GAME LOOP-----------------------------------------------------------------------------------------------------
        while(!w.shouldClose()){
            DevStatsWindow.fps.add(1/w.getDelta());
            gameState.update();
        }

        executor.shutdown();
        window.clean();
        System.exit(0);

    }

    public static Window getWindow(){
        return  window;
    }
}
