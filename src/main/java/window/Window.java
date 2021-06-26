package window;

import input.KeyboardInput;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import runners.Game;
import util.TimeUtils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public class Window {
    private long id;
    private long time;
    private float delta = 0.0000001f;

    private int width = 800;
    private int height = 800;
    private float aspectRatio;

    public Window(){
        glfwInit();

        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwWindowHint(GLFW_RED_BITS, mode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());

        width = mode.width();
        height = mode.height();
        long mon = glfwGetPrimaryMonitor();

        if(Game.props.getProperty("game.windowed").equals("true")){
            mon = 0;
            width = Integer.parseInt(Game.props.getProperty("game.windowed.width"));
            height = Integer.parseInt(Game.props.getProperty("game.windowed.height"));
        }

        aspectRatio = width/(float)height;

        id = glfwCreateWindow(width,height,"LWJGL project",mon,0);

        glfwShowWindow(id);
        glfwMakeContextCurrent(id);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glAlphaFunc (GL_GREATER, 0.1f);
        glEnable(GL_ALPHA_TEST);

        glClearColor(0.0f,0f,0.5f,1);

    }

    public void renderStart(){
        long newTime = TimeUtils.getTime()+1000000/60;
        delta = (newTime - time)/1000000.0f;
        time = newTime;
        glfwPollEvents();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void renderEnd(){
        glfwSwapBuffers(id);

        //FPS limit
        while(time > TimeUtils.getTime()){
            try {
                long i = (time-TimeUtils.getTime())/1000;
                if(i > 1)
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clean(){
        glfwTerminate();
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(id);
    }

    public long getId(){
        return id;
    }

    public float getDelta() {
        return delta;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public float getAspectRatio(){
        return aspectRatio;
    }
}
