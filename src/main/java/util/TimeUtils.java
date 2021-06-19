package util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TimeUtils {
    public static long getTime() {
        return (long)(glfwGetTime() * 1000000);
    }
}
