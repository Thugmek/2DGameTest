package runners;

import org.joml.Vector2i;
import world.AStarAlg;
import world.WorldMap;

import java.util.Random;

public class AStarTest {
    public static void main(String[] args) throws Exception {
        Random rand = new Random();

        WorldMap map = new WorldMap();

        for(int i = 0;i<100;i++) {
            int x1 = -1;
            int y1 = rand.nextInt(10);
            int x2 = 10;
            int y2 = rand.nextInt(10);

            long time = System.nanoTime();

            System.out.println(String.format("Seach for path %d from [%d|%d] to [%d|%d]",i,x1,y1,x2,y2));
            AStarAlg a = new AStarAlg(new Vector2i(x1, y1), new Vector2i(x2, y2),map);

            long delta = System.nanoTime()-time;
            float fps = 1000000000f/delta;

            a.printResult();

            System.out.println(String.format("Time: %dns, theoretical FPS: %f",delta,fps));
        }

        //new AStarAlg(new Vector2i(1000, 1000), new Vector2i(-1000, -700));
    }
}
