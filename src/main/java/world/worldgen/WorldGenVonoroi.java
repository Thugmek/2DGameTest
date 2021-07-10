package world.worldgen;

import org.joml.Vector2f;
import util.Randomizers;
import world.Biome;
import world.WorldMapTile;

import java.util.Dictionary;
import java.util.Hashtable;

public class WorldGenVonoroi implements WorldGen {

    private int chunkSize = 1024;
    private int seed = 123462;
    private final Dictionary<Integer,Dictionary<Integer,VoronoiCenter>> lookup = new Hashtable<>();

    @Override
    public WorldMapTile getTile(int x, int y) {
        WorldMapTile tile = new WorldMapTile();
        float value = voronoi(x,y);
        tile.biome = value>0.66?Biome.FORREST:(value>0.33?Biome.MEADOW:Biome.DESERT);
        return tile;
    }

    private float voronoi(int x, int y){

        int chunkX = x/chunkSize;
        int chunkY = y/chunkSize;

        if(x<0) chunkX -= 1;
        if(y<0) chunkY -= 1;

        float restX = ((float)x/chunkSize) - chunkX;
        float restY = ((float)y/chunkSize) - chunkY;

        int offsetX = (restX<0.5)?-1:0;
        int offsetY = (restY<0.5)?-1:0;

        //System.out.println(String.format("[%f|%f]",restX,restY));

        float distance = chunkSize*2;
        float res = 0;

        for(int i = 0;i<=1;i++){
            for(int j = 0;j<=1;j++){
                VoronoiCenter vc = getVoronoi(chunkX+i+offsetX,chunkY+j+offsetY);
                float actualDistance = new Vector2f(x,y).distance(vc.x,vc.y);
                if(actualDistance < distance){
                    distance = actualDistance;
                    res = vc.a;
                }
            }
        }

        return res;
    }

    private VoronoiCenter getVoronoi(int x, int y){
        Dictionary<Integer,VoronoiCenter> row = lookup.get(x);
        if(row == null){
            row = new Hashtable<>();
            lookup.put(x,row);
        }

        VoronoiCenter vc = row.get(y);
        if(vc == null){
            vc = new VoronoiCenter(x,y);
            row.put(y,vc);
        }
        return vc;
    }

    private class VoronoiCenter{
        public final float x;
        public final float y;
        public final float a;

        public VoronoiCenter(float x, float y, float a) {
            this.x = x;
            this.y = y;
            this.a = a;
        }

        public VoronoiCenter(int x, int y) {
            this.x = chunkSize*(x+Randomizers.getByPos(x,y,seed));
            this.y = chunkSize*(y+Randomizers.getByPos(x,y,seed+1));
            this.a = Randomizers.getByPos(x,y,seed+2);
        }
    }
}
