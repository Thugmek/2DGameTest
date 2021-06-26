package runners;

import gameobjects.*;
import gameobjects.entities.entityStates.FindingPathState;
import gui.DevStatsWindow;
import gui.Gui;
import gui.PauseMenu;
import input.CursorInput;
import input.KeyboardInput;
import input.MouseInput;
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Shader;
import util.GarbageCollectionUtils;
import window.Camera;
import window.Window;
import world.Biome;
import world.WorldMap;
import imgui.app.Configuration;
import world.WorldMapChunk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.*;

import static org.lwjgl.opengl.GL11.*;

public class Game {

    public static Random ran = new Random();
    private static Window window;
    public static Camera cam;
    public static ThreadPoolExecutor executor;
    public static Shader shader;
    public static Properties props = new Properties();

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

        String[] files = new String[]{
                "sprites\\textures\\Suelo tierra.jpg",
                "sprites\\textures\\Suelo arena.jpg",
                "sprites\\textures\\Suelo hierba.jpg",
                "sprites\\cursor.png",
                "sprites\\cat.png",
        };

        String[] names = new String[]{
                "dirt",
                "sand",
                "grass",
                "cursor",
                "cat"
        };

        ResourceManager.loadTextures(files,names);

        Biome.MEADOW.texture = ResourceManager.getTexture("grass");
        Biome.DESERT.texture = ResourceManager.getTexture("dirt");

        glDisable(GL_DEPTH_TEST);

        WorldMap map = new WorldMap();

        Gui.init(new Configuration(),w.getId());

        CursorInput.init();

        List<Entity> cats = new LinkedList<>();

        for(int i = 0;i<100;i++){
            Entity cat = new Entity(new Sprite(ResourceManager.getTexture("cat")),map);
            cat.getPos().add(ran.nextInt(200)-100,ran.nextInt(200)-100);
            cat.setState(new FindingPathState(cat,new Vector2i(0,0),map));
            map.addGameObject(cat);
        }

        ResourceManager.getShader("shader").setUniform1i("ourTexture",0 );
        Camera c = new Camera();
        cam = c;
        shader = ResourceManager.getShader("shader");

        //GAME LOOP-----------------------------------------------------------------------------------------------------
        while(!w.shouldClose()){
            DevStatsWindow.fps.add(1/w.getDelta());

            float mapX = c.getPos().x;
            float mapY = c.getPos().y;
            mapX /= WorldMapChunk.CHUNK_SIZE;
            mapY /= WorldMapChunk.CHUNK_SIZE;
            mapX += 0.5f;
            mapY += 0.5f;
            mapX = Math.round(mapX);
            mapY = Math.round(mapY);

            //GAME UPDATE-----------------------------------------------------------------------------------------------
            if(!PauseMenu.getPaused()) {
                CursorInput.update();
                c.update(w.getDelta());
                map.update(-(int)mapX,-(int)mapY,(int)(1/(c.getScale()*WorldMapChunk.CHUNK_SIZE))+1,w.getDelta());

                for (Entity e : cats) {
                    e.update(w.getDelta());
                }
            }

            //GAME RENDER-----------------------------------------------------------------------------------------------
            w.renderStart();
            c.forShader(shader);
            shader.bind();
            map.render(-(int)mapX,-(int)mapY,(int)(1/(c.getScale()*WorldMapChunk.CHUNK_SIZE))+1);

            for(Entity e : cats){
                e.render();
            }

            CursorInput.render();

            //GAME GUI--------------------------------------------------------------------------------------------------
            Gui.run();

            w.renderEnd();
            GarbageCollectionUtils.update();
        }

        executor.shutdown();
        window.clean();


    }

    public static void setNewRandomPoint(Entity e, WorldMap map){
        while(true){

            int range = 10;

            int x = ran.nextInt(2*range+1)-range;
            int y = ran.nextInt(2*range+1)-range;

            x+=Math.round(e.getPos().x);
            y+=Math.round(e.getPos().y);

            if(!map.getTile(x,y).wall){
                e.goTo(new Vector2i(x,y));
                return;
            }
        }
    }

    public static void setMap(WorldMap map){
        int[] walls = new int[]{
                0,1,1,1,1,1,1,1,1,1,
                1,1,0,0,0,0,0,0,0,1,
                1,0,0,0,0,0,0,0,0,1,
                1,1,1,0,1,1,1,1,1,1,
                0,0,0,0,1,0,0,0,0,1,
                1,1,1,1,1,0,1,1,1,1,
        };

        int width = 10;

        for(int i = 0;i<walls.length;i++){
            if(walls[i] == 1){
                map.getTile(i%width,i/width).wall = true;
            }
        }
    }

    public static Window getWindow(){
        return  window;
    }
}
