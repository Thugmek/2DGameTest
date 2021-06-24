package world;

import gameobjects.Model;
import org.joml.Vector2f;
import resources.ResourceManager;
import resources.Texture;

public class WorldMapChunk {

    public static final int CHUNK_SIZE = 32;

    private WorldMapTile[][] tiles = new WorldMapTile[CHUNK_SIZE][CHUNK_SIZE];

    private int x;
    private int y;

    private Model model;
    private Model model2;
    private Texture texture1;
    private Texture texture2;

    public WorldMapChunk(int x, int y){
        texture1 = ResourceManager.getTexture("dirt");
        texture2 = ResourceManager.getTexture("grass");
        this.x = x;
        this.y = y;
        for(int i = 0;i<CHUNK_SIZE;i++){
            for(int j = 0;j<CHUNK_SIZE;j++){
                tiles[i][j] = WorldGenAlg.getTile(x*CHUNK_SIZE + i, y*CHUNK_SIZE + j);
            }
        }
    }

    public WorldMapTile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void generateModel(){

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
                    float f[] = texture1.getUVs();
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

    public void render(){
        if(model == null) generateModel();
        model.render();
    }
}