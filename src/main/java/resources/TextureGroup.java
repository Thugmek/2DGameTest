package resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

public class TextureGroup {
    private int id;
    private int maxWidth = 0;
    private int maxHeight = 0;

    private Dictionary<String,Texture> textureDictionary = new Hashtable<>();

    public TextureGroup(List<TextureDefinition> list){
        try {

            int length = list.size();
            ByteBuffer[] buffers = new ByteBuffer[length];
            Texture[] textures = new Texture[length];

            int x[] = new int[1];
            int y[] = new int[1];
            int ch[] = new int[1];

            for(int i = 0;i<length;i++) {

                buffers[i] = STBImage.stbi_load("src\\main\\resources\\" + list.get(i).getPath(), x, y, ch, 4);
                if (buffers[i] == null) System.out.println("Pixels are null!!!");

                int width = y[0];
                int height = x[0];

                textures[i] = new Texture(width,height,i,length,list.get(i).getName());
                textureDictionary.put(list.get(i).getName(),textures[i]);

                maxWidth = Math.max(maxWidth,width);
                maxHeight = Math.max(maxHeight,height);

                System.out.println(String.format("Texture loaded: n:%d, width: %d, height:%d",i,width,height));
            }


            System.out.println(String.format("Max width: %d, height: %d",maxWidth,maxHeight));

            ByteBuffer data = BufferUtils.createByteBuffer(maxWidth*maxHeight*length*4);

            for(int i = 0;i<length;i++) {
                putTexture(buffers[i],textures[i].getHeight(),textures[i].getWidth(),data);
                textures[i].generate(maxHeight,maxWidth);
            }

            data.flip();

            id = glGenTextures();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_3D,id);
            glTexParameterf(GL_TEXTURE_3D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
            glTexParameterf(GL_TEXTURE_3D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
            glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexImage3D(GL_TEXTURE_3D,0,GL_RGBA,maxWidth,maxHeight,length,0,GL_RGBA,GL_UNSIGNED_BYTE,data);

            System.out.println(String.format("Textures loaded."));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void bind(){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_3D,id);
    }

    public int getId(){
        return id;
    }

    public Dictionary<String, Texture> getTextureDictionary() {
        return textureDictionary;
    }

    private void putTexture(ByteBuffer image, int x, int y, ByteBuffer result){
        for(int i = 0;i<maxHeight;i++){
            for(int j = 0;j<maxWidth;j++){
                if(i<y && j<x){
                    int index = (j+i*x)*4;
                    result.put(image.get(index));
                    result.put(image.get(index+1));
                    result.put(image.get(index+2));
                    result.put(image.get(index+3));
                }else{
                    result.put(new byte[]{(byte)255,(byte)0,(byte)100,(byte)255});
                }
            }
        }
    }
}
