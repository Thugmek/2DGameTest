package resources;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {
    private final int programId;

    private int vertexShaderId;
    private int fragmentShaderId;
    private int geometryShaderId;

    private int cameraPos;
    private int cameraZoom;
    private int objectPos;

    public Shader() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        System.out.println("Create vertex shader");
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        System.out.println("Create fragment shader");
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public void createGeometryShader(String shaderCode) throws Exception {
        System.out.println("Create geometry shader");
        geometryShaderId = createShader(shaderCode, GL_GEOMETRY_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }
        if (geometryShaderId != 0) {
            glDetachShader(programId, geometryShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        cameraPos = glGetUniformLocation(programId,"cameraPos");
        cameraZoom = glGetUniformLocation(programId,"cameraZoom");
        objectPos = glGetUniformLocation(programId,"objectPos");

    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public void setUniform(String name, float value){
        int location = glGetUniformLocation(programId,name);
        if(location != -1){
            glUniform1f(location,value);
        }else{
            System.err.println("Can't get uniform location");
        }
    }
    public void setUniform1i(String name, int value){
        int location = glGetUniformLocation(programId,name);
        if(location != -1){
            glUniform1i(location,value);
        }else{
            System.err.println("Can't get uniform location");
        }
    }
    public void setCamePos(Vector2f pos){
        glUniform2f(cameraPos,pos.x,pos.y);
    }
    public void setCameraZoom(float zoom){
        glUniform1f(cameraZoom,zoom);
    }
    public void setObjectPos(Vector2f pos){
        glUniform2f(objectPos,pos.x,pos.y);
    }
}
