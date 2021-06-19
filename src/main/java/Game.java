import gameobjects.Cursor;
import gameobjects.Model;
import input.KeyboardInput;
import input.MouseInput;
import resources.ResourceManager;
import resources.Texture;
import window.Camera;
import window.Window;
import world.WorldMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Game {
    public static void main(String[] args) {
        Window w = new Window();
        KeyboardInput.setWindow(w.getId());
        MouseInput.init(w.getId());
        ResourceManager.loadShader("shader","shaders/shader.vs","shaders/shader.fs");

        glDisable(GL_DEPTH_TEST);

        WorldMap map = new WorldMap();
        for(int i = -5;i<=5;i++){
            for(int j = -5;j<=5;j++){
                map.generate(i,j);
            }
        }

        Texture t = new Texture("sprites\\textures\\Suelo tierra.jpg");
        Texture t2 = new Texture("sprites\\cursor.png");

        ResourceManager.getShader("shader").setUniform1i("ourTexture",0 );

        Camera c = new Camera();

        Cursor cur = new Cursor();

        while(!w.shouldClose()){

            c.update(w.getDelta());

            w.renderStart();
            c.forShader(ResourceManager.getShader("shader"));
            glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D,t.getId());
            map.render(1,1,2);
            glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D,t2.getId());
            cur.render();
            w.renderEnd();
        }

    }
}
