package world;

import gameobjects.GameObject;
import gameobjects.Model;
import org.joml.Vector2f;
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Texture;
import runners.Game;

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
    private Model model2;
    private Texture texture1;
    private Texture texture2;

    public WorldMapChunk(int x, int y){
        //texture1 = ResourceManager.getTexture("dirt");
        texture2 = ResourceManager.getTexture("sand");
        this.x = x;
        this.y = y;
        futureTiles = Game.executor.submit(() -> {
            return generate();
        });
    }

    private WorldMapTile[][] generate(){
        WorldMapTile[][] tiles = new WorldMapTile[CHUNK_SIZE][CHUNK_SIZE];
        for(int i = 0;i<CHUNK_SIZE;i++){
            for(int j = 0;j<CHUNK_SIZE;j++){
                tiles[i][j] = WorldGenAlg.getTile(x*CHUNK_SIZE + i, y*CHUNK_SIZE + j);
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

        try {
            tiles = futureTiles.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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

                if(!tiles[i][j].wall){
                    float f[] = tiles[i][j].biome.texture.getUVs();
                    for(int u = 0;u<18;u++){
                        uvs[index + u] = f[u];
                    }
                }else{
                    float f[] = texture2.getUVs();
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
        for (GameObject go : gameObjects) {
            go.update(delta);
            if(isInChuk(go.getPos()));
        }
    }

    public void render(){
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
}