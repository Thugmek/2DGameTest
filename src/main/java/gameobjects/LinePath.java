package gameobjects;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import resources.ResourceManager;
import resources.Shader;
import util.GarbageCollectionUtils;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class LinePath {

    protected int verts;
    protected int vb_id;
    protected Shader shader;

    protected Vector2f pos;

    public LinePath(List<Vector2i> list){

        pos = new Vector2f(0,0);

        shader = ResourceManager.getShader("lineShader");

        verts = list.size();

        FloatBuffer buff = BufferUtils.createFloatBuffer(verts*3);
        for(int i = 0;i<verts;i++){
            buff.put(list.get(i).x+0.5f);
            buff.put(list.get(i).y+0.5f);
            buff.put(0);
        }

        buff.flip();

        vb_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vb_id);
        glBufferData(GL_ARRAY_BUFFER,buff,GL_STATIC_DRAW);
    }

    public void render(){
        //set shader
        shader.bind();
        shader.setObjectPos(pos);
        //render
        glEnable(GL_VERTEX_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER,vb_id);
        glVertexPointer(3,GL_FLOAT,0,0);

        glDrawArrays(GL_LINE_STRIP,0,verts);

        glBindBuffer(GL_ARRAY_BUFFER,0);

        glDisable(GL_VERTEX_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        shader.unbind();
    }

    public void finalize(){
        System.out.println("finalize: " + vb_id);
        GarbageCollectionUtils.getBuffersList().add(vb_id);
    }
}
