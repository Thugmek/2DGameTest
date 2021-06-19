package resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

public class Texture {
    BufferedImage bi;
    private int id;
    private int width;
    private int height;

    public Texture(String texture){
        try {
            int x[] = new int[1];
            int y[] = new int[1];
            int ch[] = new int[1];

            System.out.println("generating pixels");

            ByteBuffer pixels = STBImage.stbi_load("src\\main\\resources\\"+texture,x,y,ch,4);
            if(pixels == null) System.out.println("Pixels are null!!!");
            //ByteBuffer pixels = BufferUtils.createByteBuffer(1000);

            width = x[0];
            height = y[0];

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D,id);
            glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,width,height,0,GL_RGBA,GL_UNSIGNED_BYTE,pixels);
            glBindTexture(GL_TEXTURE_2D,0);

            System.out.println(String.format("Texture loaded. Image size: %d:%dpx",width,height));

        }catch(Exception e){

        }
    }

    public int getId(){
        return id;
    }
}
