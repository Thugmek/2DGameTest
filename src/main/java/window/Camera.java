package window;

import input.KeyboardInput;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import resources.Shader;
import runners.Game;

public class Camera {
    Vector2f pos;
    float scale;

    public Camera(){
        pos = new Vector2f(0,0);
        scale = 0.07f;
    }

    public void update(float delta){
        if(KeyboardInput.getKey(GLFW.GLFW_KEY_A)==1){
            pos.add(new Vector2f(delta/scale,0));
        }
        if(KeyboardInput.getKey(GLFW.GLFW_KEY_D)==1){
            pos.add(new Vector2f(-delta/scale,0));
        }
        if(KeyboardInput.getKey(GLFW.GLFW_KEY_W)==1){
            pos.add(new Vector2f(0,-delta/scale));
        }
        if(KeyboardInput.getKey(GLFW.GLFW_KEY_S)==1){
            pos.add(new Vector2f(0,delta/scale));
        }

        if(KeyboardInput.getKey(GLFW.GLFW_KEY_Q)==1){
            scale *= 1.01;
        }
        if(KeyboardInput.getKey(GLFW.GLFW_KEY_E)==1){
            scale *= 0.99;
        }
    }

    public void forShader(Shader shader){
        shader.bind();

        shader.setAspectRatio(Game.getWindow().getAspectRatio());
        shader.setCamePos(pos);
        shader.setCameraZoom(scale);
        shader.unbind();
    }
}
