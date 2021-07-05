package runners;

import gui.Gui;
import imgui.app.Configuration;
import input.KeyboardInput;
import input.MouseInput;
import resources.ResourceManager;
import resources.Shader;
import window.Camera;
import window.gameStates.GameState;
import window.gameStates.MenuGameState;
import window.Window;
import world.WorldMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
        gameState = new MenuGameState();

        //GAME LOOP-----------------------------------------------------------------------------------------------------
        while(!w.shouldClose()){
            gameState.update();
        }

        executor.shutdown();
        Gui.dispose();
        window.clean();
        System.exit(0);

    }

    public static Window getWindow(){
        return  window;
    }
}
