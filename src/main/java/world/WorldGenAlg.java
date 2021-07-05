package world;

import org.joml.Vector2f;
import util.Randomizers;

public class WorldGenAlg {
    public static long seed = 2135432135431l;

    public static WorldMapTile getTile(int x, int y){
        WorldMapTile tile = new WorldMapTile();
        float value = multiOctavePerlin(x,y,3,17,(int)seed);
        tile.biome = value>0.2?Biome.DESERT:Biome.MEADOW;
        return tile;
    }

    private static float multiOctavePerlin(int x, int y, int octaves, int step, int seed){
        float res = 0;
        for(int i = 1;i<=octaves;i++){
            res += perlin(x,y,step*i,seed+i)*i/octaves;
        }
        return res;
    }

    private static float perlin(int x, int y, int chunk, int seed){
        int x1 = (x/chunk) * chunk;
        int y1 = (y/chunk) * chunk;

        if(x1 > x) x1 -= chunk;
        if(y1 > y) y1 -= chunk;
        int x2 = x1 + chunk;
        int y2 = y1 + chunk;

        float kx = (float)(x-x1)/(chunk);
        float ky = (float)(y-y1)/(chunk);

        float res = 0;
        float diag = (float)Math.sqrt(2);

        float a = cornerVector(Math.round(Randomizers.getByPos(x1,y1,seed)*8)/8f).dot(new Vector2f(x-x1,y-y1));
        float b = cornerVector(Math.round(Randomizers.getByPos(x1,y2,seed)*8)/8f).dot(new Vector2f(x-x1,y-y2));
        float c = cornerVector(Math.round(Randomizers.getByPos(x2,y1,seed)*8)/8f).dot(new Vector2f(x-x2,y-y1));
        float d = cornerVector(Math.round(Randomizers.getByPos(x2,y2,seed)*8)/8f).dot(new Vector2f(x-x2,y-y2));

        float e = (1-ky)*a + ky*b;
        float f = (1-ky)*c + ky*d;

        return (1-kx)*e + kx*f;

    }

    private static Vector2f cornerVector(float f){
        return new Vector2f((float)Math.sin(f*Math.PI*2),(float)Math.cos(f*Math.PI*2));
    }
}
