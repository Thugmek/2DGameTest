package resources;

import org.lwjgl.stb.STBImage;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture {
    private float[] uvs;
    private int width;
    private int height;
    private float depth;

    public Texture(int width, int height, int index, int n) {
        this.width = width;
        this.height = height;
        this.depth = (float)index/(n)+0.00001f;
    }

    public void generate(int x, int y){
        //TODO - generate uvs

        float xf = (float)width/x;
        float yf = (float)height/y;

        uvs = new float[]{
                0,yf,depth,
                xf,yf,depth,
                0,0,depth,

                xf,0,depth,
                xf,yf,depth,
                0,0,depth,
        };
    }

    public float[] getUVs(){
        return uvs;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getIndex() {
        return depth;
    }
}
