package world;

import java.util.Dictionary;
import java.util.Hashtable;

public class WorldMap {

    private Dictionary<Integer,Dictionary<Integer,WorldMapChunk>> map;

    public WorldMap(){
        map = new Hashtable<>();
    }

    public void generate(int x, int y){
        if(map.get(x) == null){
            System.out.println("generating row: " + x);
            map.put(x,new Hashtable<>());
        }

        map.get(x).put(y,new WorldMapChunk(x,y));
    }

    public WorldMapTile getTile(int x, int y){

        int chunkX = x/WorldMapChunk.CHUNK_SIZE;
        int chunkY = y/WorldMapChunk.CHUNK_SIZE;

        int restX = x%WorldMapChunk.CHUNK_SIZE;
        int restY = y%WorldMapChunk.CHUNK_SIZE;

        if(restX < 0){
            restX += WorldMapChunk.CHUNK_SIZE;
            chunkX --;
        }
        if(restY < 0){
            restY += WorldMapChunk.CHUNK_SIZE;
            chunkY --;
        }

        Dictionary<Integer,WorldMapChunk> row = map.get(chunkX);
        if(row == null){
            row = new Hashtable<>();
            map.put(chunkX,row);
        }
        WorldMapChunk chunk = row.get(chunkY);
        if(chunk == null){
            chunk = new WorldMapChunk(chunkX,chunkY);
            row.put(chunkY,chunk);
        }

        //System.out.println(String.format("Cords: [%d|%d], chunk: [%d|%d], tile: [%d|%d]",x,y,chunkX,chunkY,restX,restY));

        return chunk.getTile(restX,restY);
    }

    public WorldMapChunk getChunk(int x, int y){
        return map.get(x).get(y);
    }

    public void render(int x, int y, int zoom) {
        for(int i = x-zoom;i<=x+zoom;i++){
            for(int j = y-zoom;j<=y+zoom;j++){
                map.get(i).get(j).render();
            }
        }
    }
}
