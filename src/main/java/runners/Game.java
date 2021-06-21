package runners;

import gameobjects.*;
import gui.DevStatsWindow;
import gui.Gui;
import gui.PauseMenu;
import input.CursorInput;
import input.KeyboardInput;
import input.MouseInput;
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Texture;
import util.GarbageCollectionUtils;
import window.Camera;
import window.Window;
import world.WorldMap;
import imgui.app.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Game {

    public static Random ran = new Random();
    private static Window window;
    public static Camera cam;

    public static void main(String[] args) {
        Window w = new Window();
        window = w;
        KeyboardInput.setWindow(w.getId());
        MouseInput.init(w.getId());
        ResourceManager.loadShader("shader","shaders/shader.vs","shaders/shader.fs");
        ResourceManager.loadShader("lineShader","shaders/lineShader.vs","shaders/lineShader.fs");

        glDisable(GL_DEPTH_TEST);

        WorldMap map = new WorldMap();
        for(int i = -5;i<=5;i++){
            for(int j = -5;j<=5;j++){
                map.generate(i,j);
            }
        }

        Gui.init(new Configuration(),w.getId());

        setMap(map);

        map.getChunk(0,0).generateModel();


        Texture t = new Texture("sprites\\textures\\Suelo tierra.jpg");
        Texture t2 = new Texture("sprites\\cursor.png");

        ResourceManager.loadTexture("dirt","sprites\\textures\\Suelo tierra.jpg");
        ResourceManager.loadTexture("cursor","sprites\\cursor.png");
        ResourceManager.loadTexture("cat","sprites\\cat.png");

        CursorInput.init();

        List<Entity> cats = new LinkedList<>();

        for(int i = 0;i<100;i++){
            cats.add(new Entity(new Sprite(ResourceManager.getTexture("cat")),map));
        }

        ResourceManager.getShader("shader").setUniform1i("ourTexture",0 );

        Camera c = new Camera();
        cam = c;

        Cursor cur = new Cursor();

        while(!w.shouldClose()){
            System.out.println(1/w.getDelta());

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
            glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D,t.getId());
            map.render(1,1,2);

            for(Entity e : cats){
                e.render();
            }

            CursorInput.render();

            Gui.run();

            w.renderEnd();
            GarbageCollectionUtils.update();
        }

    }

    public static void setNewRandomPoint(Entity e, WorldMap map){
        while(true){

            int range = 5;

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
                1,1,1,0,1,1,1,1,0,1,
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
