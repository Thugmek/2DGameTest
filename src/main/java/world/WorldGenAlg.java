package world;

import util.Randomizers;

public class WorldGenAlg {
    public static long seed = 2135432135431l;

    public static WorldMapTile getTile(int x, int y){
        WorldMapTile tile = new WorldMapTile();
        float value = perlin(x,y,64,(int)seed)/1.5f;
        value += perlin(x,y,16,(int)seed+1)/3;
        tile.biome = value>0.5?Biome.DESERT:Biome.MEADOW;
        return tile;
    }

    private static float perlin(int x, int y, int chunk, int seed){
        int x1 = (x/chunk) * chunk;
        int y1 = (y/chunk) * chunk;

        if(x1 > x) x1 -= chunk;
        if(y1 > y) y1 -= chunk;
        int x2 = x1 + chunk;
        int y2 = y1 + chunk;

        float kx = (float)(x-x1)/chunk;
        float ky = (float)(y-y1)/chunk;

        float a = Randomizers.getByPos(x1,y1,seed);
        float b = Randomizers.getByPos(x1,y2,seed);
        float c = Randomizers.getByPos(x2,y1,seed);
        float d = Randomizers.getByPos(x2,y2,seed);

        float e = (1-ky)*a + ky*b;
        float f = (1-ky)*c + ky*d;

        //System.out.println(String.format("Perlin [%d,%d] - x1:%d, y1:%d, x2:%d, y2:%d - a:%f, b:%f, c:%f, d:%f, e:%f, f:%f",x,y,x1,y1,x2,y2,a,b,c,d,e,f));

        return (1-kx)*e + kx*f;

    }
}
