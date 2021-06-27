package runners;

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
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Shader;
import resources.TextureDefinition;
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

        List<TextureDefinition> textures = new ArrayList<>();

        textures.add(new TextureDefinition("dirt","sprites\\textures\\Suelo tierra.jpg"));
        textures.add(new TextureDefinition("sand","sprites\\textures\\Suelo arena.jpg"));
        textures.add(new TextureDefinition("grass","sprites\\textures\\Suelo hierba.jpg"));
        textures.add(new TextureDefinition("cursor","sprites\\cursor.png"));
        textures.add(new TextureDefinition("cat","sprites\\cat.png"));
        textures.add(new TextureDefinition("walls","sprites\\textures\\walls.png"));
        //textures.add(new TextureDefinition("walls","sprites\\textures\\Suelo arena.jpg"));

        ResourceManager.loadTextures(textures);

        Biome.MEADOW.texture = ResourceManager.getTexture("grass");
        Biome.DESERT.texture = ResourceManager.getTexture("dirt");

        map = new WorldMap();
        setMap(map);

        Gui.init(new Configuration(),w.getId());

        CursorInput.init(map);

        List<Entity> cats = new LinkedList<>();

        for(int i = 0;i<1000;i++){
            Entity cat = new Cat(map);
            cat.getPos().add(ran.nextInt(100),ran.nextInt(100));
            cat.setState(new IdleState(cat,map));
            cat.setSpeed(ran.nextFloat()*3+1.5f);
            map.addGameObject(cat);
        }

        ResourceManager.getShader("shader").setUniform1i("sampler",0 );
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
                //map.update(-(int)mapX,-(int)mapY,0, w.getDelta());

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
        System.exit(0);

    }

    public static void setMap(WorldMap map){
        int[] walls = new int[]{
                0,1,1,1,1,1,1,1,1,1,
                1,1,0,0,0,0,0,0,0,1,
                1,0,0,0,0,0,0,0,0,1,
                1,1,1,0,1,1,1,1,0,1,
                0,0,0,0,1,0,0,0,0,1,
                1,1,1,1,1,0,1,1,0,1,
                1,0,0,0,1,0,1,1,0,1,
                1,1,0,1,1,0,1,1,0,1,
                1,1,0,1,1,0,1,1,0,1,
                1,1,0,0,0,0,1,1,0,1,
                1,1,1,1,1,1,1,1,1,1,
        };

        int width = 10;

        for(int i = 0;i<walls.length;i++){
            if(walls[i] == 1){
                map.getTile(i%width,i/width, true).wall = true;
            }
        }

        map.getChunk(0,0).generateModel();
    }

    public static Window getWindow(){
        return  window;
    }
}
