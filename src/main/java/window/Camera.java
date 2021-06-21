package window;

import gui.DevStatsWindow;
import input.KeyboardInput;
import input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import resources.Shader;
import runners.Game;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Camera {
    private Vector2f pos;
    private float scale;

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

        if(MouseInput.getMouseButton(GLFW_MOUSE_BUTTON_LEFT) == 1){
            DevStatsWindow.screenPos = MouseInput.getPos();
            DevStatsWindow.mousePos = screenPosToWorld(MouseInput.getPos());
        }
    }

    public void forShader(Shader shader){
        shader.bind();

        shader.setAspectRatio(Game.getWindow().getAspectRatio());
        shader.setCamePos(pos);
        shader.setCameraZoom(scale);
        shader.unbind();
    }

    public Vector2f screenPosToWorld(Vector2f screen){
        int height = Game.getWindow().getHeight();
        int width = Game.getWindow().getWidth();

        //screen = new Vector2f(width/2,height/2);

        float ar = Game.getWindow().getAspectRatio();

        Vector2f res = new Vector2f(screen.x/width,1-(screen.y/height));
        res.mul(2);
        res.add(-1,-1);

        res.mul(1/scale);

        res = new Vector2f(res.x,res.y/ar);

        res.add(-pos.x,-pos.y);

        return res;
    }

    public Vector2f getPos() {
        return pos;
    }
}
