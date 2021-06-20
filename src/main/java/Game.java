import gameobjects.*;
import gui.Gui;
import input.KeyboardInput;
import input.MouseInput;
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Texture;
import util.GarbageCollectionUtils;
import window.Camera;
import window.Window;
import world.AStarAlg;
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

    public static void main(String[] args) {
        Window w = new Window();
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

        map.getTile(1,1).wall = true;
        map.getTile(2,1).wall = true;
        map.getTile(3,1).wall = true;
        map.getTile(4,1).wall = true;
        map.getTile(1,2).wall = true;
        map.getTile(1,3).wall = true;
        map.getTile(1,4).wall = true;
        map.getTile(1,5).wall = true;
        map.getTile(1,6).wall = true;
        map.getTile(1,7).wall = true;
        map.getTile(1,8).wall = true;
        map.getTile(1,9).wall = true;
        map.getTile(2,9).wall = true;
        map.getTile(3,9).wall = true;
        map.getTile(4,9).wall = true;
        map.getTile(5,9).wall = true;

        map.getTile(0,4).wall = true;
        map.getTile(0,5).wall = true;
        map.getTile(4,0).wall = true;
        map.getTile(5,0).wall = true;
        map.getTile(6,0).wall = true;
        map.getTile(7,0).wall = true;
        map.getTile(8,0).wall = true;
        map.getTile(9,0).wall = true;
        map.getTile(9,1).wall = true;
        map.getTile(9,2).wall = true;
        map.getTile(9,3).wall = true;
        map.getTile(9,4).wall = true;
        map.getTile(9,5).wall = true;
        map.getTile(9,6).wall = true;
        map.getTile(9,7).wall = true;

        map.getTile(8,7).wall = true;
        map.getTile(7,7).wall = true;
        map.getTile(6,7).wall = true;

        map.getTile(9,9).wall = true;
        map.getTile(8,9).wall = true;
        map.getTile(7,9).wall = true;
        map.getTile(6,9).wall = true;

        map.getTile(4,2).wall = true;
        map.getTile(4,4).wall = true;
        map.getTile(4,5).wall = true;
        map.getTile(4,6).wall = true;

        map.getTile(2,4).wall = true;
        map.getTile(3,4).wall = true;
        map.getTile(5,4).wall = true;
        map.getTile(6,4).wall = true;

        Gui g = new Gui(w.getId());

        g.init(new Configuration());



        map.getChunk(0,0).generateModel();

        AStarAlg a = new AStarAlg(new Vector2i(0,0),new Vector2i(2,2),map);

        Texture t = new Texture("sprites\\textures\\Suelo tierra.jpg");
        Texture t2 = new Texture("sprites\\cursor.png");

        Texture cat = new Texture("sprites\\cat.png");

        List<Entity> cats = new LinkedList<>();

        for(int i = 0;i<1000;i++){
            cats.add(new Entity(new Sprite(cat),map));
        }

        ResourceManager.getShader("shader").setUniform1i("ourTexture",0 );

        Camera c = new Camera();

        Cursor cur = new Cursor();

        while(!w.shouldClose()){
            System.out.println(1/w.getDelta());

            c.update(w.getDelta());

            for(Entity e : cats){
                e.update(w.getDelta());
                if(!e.isOnWay()) setNewRandomPoint(e,map);
            }

            w.renderStart();
            c.forShader(ResourceManager.getShader("shader"));
            c.forShader(ResourceManager.getShader("lineShader"));
            glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D,t.getId());
            map.render(1,1,2);

            for(Entity e : cats){
                e.render();
            }

            glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D,t2.getId());
            cur.render();

            g.run();

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

    public void setMap(){

    }
}
