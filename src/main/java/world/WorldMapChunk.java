package world;

import gameobjects.Model;
import org.joml.Vector2f;
import resources.ResourceManager;

public class WorldMapChunk {

    public static final int CHUNK_SIZE = 32;

    private WorldMapTile[][] tiles = new WorldMapTile[CHUNK_SIZE][CHUNK_SIZE];

    private int x;
    private int y;

    private Model model;

    public WorldMapChunk(int x, int y){
        this.x = x;
        this.y = y;
        for(int i = 0;i<CHUNK_SIZE;i++){
            for(int j = 0;j<CHUNK_SIZE;j++){
                tiles[i][j] = WorldGenAlg.getTile(x*CHUNK_SIZE + i, y*CHUNK_SIZE + j);
            }
        }

        generateModel();
    }

    public void generateModel(){

        float[] verts = new float[CHUNK_SIZE*CHUNK_SIZE*6*3];
        float[] cols = new float[CHUNK_SIZE*CHUNK_SIZE*6*3];
        float[] uvs = new float[CHUNK_SIZE*CHUNK_SIZE*6*2];

        for(int i = 0;i<CHUNK_SIZE;i++){
            for(int j = 0;j<CHUNK_SIZE;j++){
                int index = (i*CHUNK_SIZE+j)*6*3;
                int index2 = (i*CHUNK_SIZE+j)*6*2;

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

                uvs[index2] = 0+i;
                uvs[index2+1] = 0+j;

                uvs[index2+2] = 1+i;
                uvs[index2+3] = 0+j;

                uvs[index2+4] = 0+i;
                uvs[index2+5] = 1+j;

                uvs[index2+6] = 1+i;
                uvs[index2+7] = 1+j;

                uvs[index2+8] = 1+i;
                uvs[index2+9] = 0+j;

                uvs[index2+10] = 0+i;
                uvs[index2+11] = 1+j;

                cols[index] = 0;
                cols[index+1] = 0;
                cols[index+2] = 0;

                cols[index+3] = 1;
                cols[index+4] = 0;
                cols[index+5] = 0;

                cols[index+6] = 0;
                cols[index+7] = 1;
                cols[index+8] = 0;

                cols[index+9] = 1;
                cols[index+10] = 1;
                cols[index+11] = 0;

                cols[index+12] = 1;
                cols[index+13] = 0;
                cols[index+14] = 0;

                cols[index+15] = 0;
                cols[index+16] = 1;
                cols[index+17] = 0;
            }
        }

        model = new Model(verts,cols, uvs);
        model.setPos(new Vector2f(x*CHUNK_SIZE,y*CHUNK_SIZE));
        model.setShader(ResourceManager.getShader("shader"));
        model.setPos(new Vector2f(x*CHUNK_SIZE,y*CHUNK_SIZE));
    }

    public void render(){
        model.render();
    }
}