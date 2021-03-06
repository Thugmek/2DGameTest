package world;

import gameobjects.GameObject;
import gameobjects.Model;
import org.joml.Vector2f;
import resources.ResourceManager;
import resources.Texture;
import resources.WallMapper;
import runners.Game;
import world.worldgen.WorldGen;
import world.worldgen.WorldGenPerlin;
import world.worldgen.WorldGenVonoroi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class WorldMapChunk {

    public static final int CHUNK_SIZE = 128;

    private Future<WorldMapTile[][]> futureTiles;
    private WorldMapTile[][] tiles = new WorldMapTile[CHUNK_SIZE][CHUNK_SIZE];
    private List<GameObject> gameObjects = new ArrayList<>();

    private int x;
    private int y;

    private Model model;
    private Texture texture2;

    private WorldGen worldGen = new WorldGenVonoroi();

    public WorldMapChunk(int x, int y){
        texture2 = ResourceManager.getTexture("walls");
        this.x = x;
        this.y = y;
        if(futureTiles == null && !Game.executor.isShutdown()){
            futureTiles = Game.executor.submit(() -> {
                return generate();
            });
        }
    }

    private WorldMapTile[][] generate(){
        WorldMapTile[][] tiles = new WorldMapTile[CHUNK_SIZE][CHUNK_SIZE];
        for(int i = 0;i<CHUNK_SIZE;i++){
            for(int j = 0;j<CHUNK_SIZE;j++){
                tiles[i][j] = worldGen.getTile(x*CHUNK_SIZE + i, y*CHUNK_SIZE + j);
            }
        }
        return tiles;
    }

    public void forceGenerate(){
        tiles = generate();
    }

    public WorldMapTile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void generateModel(){
        if(tiles[0][0] == null) {
            try {
                tiles = futureTiles.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        float[] verts = new float[CHUNK_SIZE*CHUNK_SIZE*6*3];
        float[] cols = new float[CHUNK_SIZE*CHUNK_SIZE*6*3];
        float[] uvs = new float[CHUNK_SIZE*CHUNK_SIZE*6*3];

        for(int i = 0;i<CHUNK_SIZE;i++){
            for(int j = 0;j<CHUNK_SIZE;j++){
                int index = (i*CHUNK_SIZE+j)*6*3;

                verts[index] = 0+i;
                verts[index+1] = 0+j;
                verts[index+2] = 0;

                verts[index+3] = 1+i;
                verts[index+4] = 0+j;
                verts[index+5] = 0;

                verts[index+6] = 0+i;
                verts[index+7] = 1+j;
                verts[index+8] = 0;

                verts[index+9] = 1+i;
                verts[index+10] = 1+j;
                verts[index+11] = 0;

                verts[index+12] = 1+i;
                verts[index+13] = 0+j;
                verts[index+14] = 0;

                verts[index+15] = 0+i;
                verts[index+16] = 1+j;
                verts[index+17] = 0;

                if(tiles[i][j].wall == null){
                    float f[] = tiles[i][j].biome.texture.getUVs();
                    for(int u = 0;u<18;u++){
                        uvs[index + u] = f[u];
                    }
                }else{
                    float f[] = getWallsUVs(i,j,tiles[i][j].wall.texture);
                    for(int u = 0;u<18;u++){
                        uvs[index + u] = f[u];
                    }
                }
            }
        }

        model = new Model(verts,cols, uvs);
        model.setPos(new Vector2f(x*CHUNK_SIZE,y*CHUNK_SIZE));
        model.setShader(ResourceManager.getShader("shader"));
    }

    public void addGameObject(GameObject go){
        gameObjects.add(go);
    }

    public void update(float delta){
        for (int i = 0;i<gameObjects.size();i++) {
            GameObject go = gameObjects.get(i);
            go.update(delta);
            Vector2f pos = go.getPos();
            WorldMapChunk chunk = Game.map.getChunkByPos(pos.x,pos.y);

            if(this != chunk){
                chunk.addGameObject(go);
                gameObjects.remove(go);
                i--;
            }
        }
    }

    public void render(){
        //System.out.println(String.format("Chunk %d,%d render. Model is: %s",x,y,(model == null)?"null":model.toString()));
        if(model == null && futureTiles != null && futureTiles.isDone()) generateModel();
        if(model != null) model.render();
        for (GameObject go : gameObjects) {
            go.render();
        }
    }

    public boolean isInChuk(Vector2f pos){
        int worldX = x*CHUNK_SIZE;
        int worldY = y*CHUNK_SIZE;
        return (pos.x > worldX && pos.y > worldY && pos.x < worldX+CHUNK_SIZE && pos.y < worldY+CHUNK_SIZE);
    }

    public float[] getWallsUVs(int i, int j, Texture texture){

        int index = 0;

        index += Game.map.getTile(x*CHUNK_SIZE + i-1, y*CHUNK_SIZE + j+1,true).wall == null?0:1;
        index += Game.map.getTile(x*CHUNK_SIZE + i, y*CHUNK_SIZE + j+1,true).wall == null?0:2;
        index += Game.map.getTile(x*CHUNK_SIZE + i+1, y*CHUNK_SIZE + j+1,true).wall == null?0:4;

        index += Game.map.getTile(x*CHUNK_SIZE + i-1, y*CHUNK_SIZE + j,true).wall == null?0:8;
        index += Game.map.getTile(x*CHUNK_SIZE + i+1, y*CHUNK_SIZE + j,true).wall == null?0:16;

        index += Game.map.getTile(x*CHUNK_SIZE + i-1, y*CHUNK_SIZE + j-1,true).wall == null?0:32;
        index += Game.map.getTile(x*CHUNK_SIZE + i, y*CHUNK_SIZE + j-1,true).wall == null?0:64;
        index += Game.map.getTile(x*CHUNK_SIZE + i+1, y*CHUNK_SIZE + j-1,true).wall == null?0:128;

        try{
            if(WallMapper.mapper[index] == -1) return texture2.getUVs(35);
            return texture.getUVs(WallMapper.mapper[index]);
        }catch (Exception e){
            e.printStackTrace();
            return texture2.getUVs(35);
        }
    }

    public List<GameObject> getGameObjects(){
        return gameObjects;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Future<WorldMapTile[][]> getFutureTiles() {
        return futureTiles;
    }

    public void setFutureTiles(Future<WorldMapTile[][]> futureTiles) {
        this.futureTiles = futureTiles;
    }

    public WorldMapTile[][] getTiles() {
        return tiles;
    }

    public void setTiles(WorldMapTile[][] tiles) {
        this.tiles = tiles;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}