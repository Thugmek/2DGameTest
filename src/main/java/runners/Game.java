package runners;

import gameobjects.*;
import gui.DevStatsWindow;
import gui.Gui;
import gui.PauseMenu;
import input.CursorInput;
import input.KeyboardInput;
import input.MouseInput;
import org.joml.Vector2i;
import org.lwjgl.system.CallbackI;
import resources.ResourceManager;
import resources.Texture;
import resources.TextureGroup;
import util.GarbageCollectionUtils;
import window.Camera;
import window.Window;
import world.WorldMap;
import imgui.app.Configuration;
import world.WorldMapChunk;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_MAX_3D_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Game {

    public static Random ran = new Random();
    private static Window window;
    public static Camera cam;
    public static ThreadPoolExecutor executor;

    public static void main(String[] args) {

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

        Thread.currentThread().setPriority(10);

        Window w = new Window();
        window = w;
        KeyboardInput.setWindow(w.getId());
        MouseInput.init(w.getId());
        ResourceManager.loadShader("shader","shaders/shader.vs","shaders/shader.fs");
        ResourceManager.loadShader("lineShader","shaders/lineShader.vs","shaders/lineShader.fs");

        String[] files = new String[]{
                "sprites\\textures\\Suelo tierra.jpg",
                "sprites\\textures\\Suelo arena.jpg",
                "sprites\\cursor.png",
                "sprites\\cat.png"
        };

        String[] names = new String[]{
                "dirt",
                "grass",
                "cursor",
                "cat"
        };

        ResourceManager.loadTextures(files,names);

        glDisable(GL_DEPTH_TEST);

        System.out.println("Max texture size: " + GL_MAX_TEXTURE_SIZE);
        System.out.println("Max 3D texture size: " + GL_MAX_3D_TEXTURE_SIZE);

        WorldMap map = new WorldMap();

        Gui.init(new Configuration(),w.getId());

        setMap(map);

        map.getChunk(0,0).generateModel();
        CursorInput.init();

        List<Entity> cats = new LinkedList<>();

        for(int i = 0;i<10000;i++){
            Entity cat = new Entity(new Sprite(ResourceManager.getTexture("cat")),map);
            cat.getPos().add(ran.nextInt(200)-100,ran.nextInt(200)-100);
            cats.add(cat);
        }

        ResourceManager.getShader("shader").setUniform1i("ourTexture",0 );

        Camera c = new Camera();
        cam = c;

        while(!w.shouldClose()){
            DevStatsWindow.fps.add(1/w.getDelta());

            CursorInput.update();

            if(!PauseMenu.getPaused()) {
                c.update(w.getDelta());

                for (Entity e : cats) {
                    e.update(w.getDelta());
                    if (!e.isOnWay()) setNewRandomPoint(e, map);
                }
            }

            w.renderStart();
            c.forShader(ResourceManager.getShader("shader"));
            c.forShader(ResourceManager.getShader("lineShader"));
            //glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_3D,t.getId());
            ResourceManager.getShader("shader").bind();

            float mapX = c.getPos().x;
            float mapY = c.getPos().y;
            mapX /= WorldMapChunk.CHUNK_SIZE;
            mapY /= WorldMapChunk.CHUNK_SIZE;
            mapX += 0.5f;
            mapY += 0.5f;
            mapX = Math.round(mapX);
            mapY = Math.round(mapY);
            map.render(-(int)mapX,-(int)mapY,(int)(1/(c.getScale()*32))+1);

            //ResourceManager.getTexture("cat").bind();
            for(Entity e : cats){
                e.render();
            }

            CursorInput.render();

            Gui.run();

            w.renderEnd();
            GarbageCollectionUtils.update();
        }

        executor.shutdown();

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
