package world;

import gameobjects.GameObject;
import org.joml.Vector2f;

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

    public WorldMapTile getTile(int x, int y, boolean force){

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

        WorldMapTile res = chunk.getTile(restX,restY);

        if(force && res == null){
            chunk.forceGenerate();
            res = chunk.getTile(restX,restY);
        }

        return res;
    }

    public void addGameObject(GameObject go){

        Vector2f pos = go.getPos();

        int chunkX = (int)pos.x/WorldMapChunk.CHUNK_SIZE;
        int chunkY = (int)pos.y/WorldMapChunk.CHUNK_SIZE;

        int restX = (int)pos.x%WorldMapChunk.CHUNK_SIZE;
        int restY = (int)pos.y%WorldMapChunk.CHUNK_SIZE;

        if(restX < 0){
            chunkX --;
        }
        if(restY < 0){
            chunkY --;
        }

        getChunk(chunkX,chunkY).addGameObject(go);
    }

    public WorldMapChunk getChunk(int x, int y){
        Dictionary<Integer,WorldMapChunk> row = map.get(x);
        if(row == null){
            row = new Hashtable<>();
            map.put(x,row);
        }
        WorldMapChunk chunk = row.get(y);
        if(chunk == null){
            chunk = new WorldMapChunk(x,y);
            row.put(y,chunk);
        }
        return chunk;
    }

    public WorldMapChunk getChunkByPos(float x, float y){

        int chunkX = (int)x/WorldMapChunk.CHUNK_SIZE;
        int chunkY = (int)y/WorldMapChunk.CHUNK_SIZE;

        float restX = x%WorldMapChunk.CHUNK_SIZE;
        float restY = y%WorldMapChunk.CHUNK_SIZE;

        if(restX < 0){
            chunkX --;
        }
        if(restY < 0){
            chunkY --;
        }

        return getChunk(chunkX,chunkY);
    }

    public void update(int x, int y, int zoom, float delta) {
        for(int i = x-zoom;i<=x+zoom;i++){
            for(int j = y-zoom;j<=y+zoom;j++){
                Dictionary<Integer,WorldMapChunk> row = map.get(i);
                if(row == null){
                    row = new Hashtable<>();
                    map.put(i,row);
                }
                WorldMapChunk chunk = row.get(j);
                if(chunk == null){
                    chunk = new WorldMapChunk(i,j);
                    row.put(j,chunk);
                }
                chunk.update(delta);
            }
        }
    }

    public void render(int x, int y, int zoom) {
        for(int i = x-zoom;i<=x+zoom;i++){
            for(int j = y-zoom;j<=y+zoom;j++){
                Dictionary<Integer,WorldMapChunk> row = map.get(i);
                if(row == null){
                    row = new Hashtable<>();
                    map.put(i,row);
                }
                WorldMapChunk chunk = row.get(j);
                if(chunk == null){
                    chunk = new WorldMapChunk(i,j);
                    row.put(j,chunk);
                }
                chunk.render();
            }
        }
    }

    public Dictionary<Integer, Dictionary<Integer, WorldMapChunk>> getDictionary() {
        return map;
    }
}
