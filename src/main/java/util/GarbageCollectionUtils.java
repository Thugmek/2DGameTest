package util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public class GarbageCollectionUtils {

    private static long lastTime = 5000000000l;

    private static Queue<Integer> buffers = new LinkedBlockingDeque<>();
    private static Queue<Integer> buffers2 = new LinkedBlockingDeque<>();

    public static Queue<Integer> getBuffersList(){
        return buffers;
    }

    public static void update(){

        if(System.nanoTime() > lastTime){
            lastTime = System.nanoTime() + 5000000000l;

            for(int i:buffers2) {
                glDeleteBuffers(i);
            }

            buffers2 = buffers;
            buffers = new LinkedBlockingQueue<>();
        }
    }
}
