package gameobjects;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import resources.Shader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL32.*;

public class Model {

    protected int tris;
    protected int vb_id;
    protected int cb_id;
    protected int uvs_id;
    protected Shader shader;

    protected Vector2f pos;

    public Model(float[] verts,float[] cols,float[] uvs){
        pos = new Vector2f(0,0);

        tris = verts.length/3;

        FloatBuffer buff = BufferUtils.createFloatBuffer(verts.length);
        buff.put(verts);
        buff.flip();

        vb_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vb_id);
        glBufferData(GL_ARRAY_BUFFER,buff,GL_STATIC_DRAW);

        buff = BufferUtils.createFloatBuffer(cols.length);
        buff.put(cols);
        buff.flip();

        cb_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,cb_id);
        glBufferData(GL_ARRAY_BUFFER,buff,GL_STATIC_DRAW);

        buff = BufferUtils.createFloatBuffer(uvs.length);
        buff.put(uvs);
        buff.flip();

        uvs_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,uvs_id);
        glBufferData(GL_ARRAY_BUFFER,buff,GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER,0);

    }

    public void setShader(Shader shader){
        this.shader = shader;
    }

    public void render(){

        //set shader
        shader.setObjectPos(pos);
        shader.setShaderMode(1);
        //render
        glEnable(GL_VERTEX_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER,vb_id);
        glVertexPointer(3,GL_FLOAT,0,0);

        glBindBuffer(GL_ARRAY_BUFFER,cb_id);
        glVertexAttribPointer(1,3,GL_FLOAT,false,0,0);

        glBindBuffer(GL_ARRAY_BUFFER,uvs_id);
        glVertexAttribPointer(2,3,GL_FLOAT,false,0,0);

        glDrawArrays(GL_TRIANGLES,0,tris);

        glBindBuffer(GL_ARRAY_BUFFER,0);

        glDisable(GL_VERTEX_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
    }

    public Vector2f getPos() {
        return pos;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

}
