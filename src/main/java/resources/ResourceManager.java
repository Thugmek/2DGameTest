package resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ResourceManager {

    private static Dictionary<String,Shader> shaders = new Hashtable<>();
    private static Dictionary<String,Texture> textures = new Hashtable<>();
    private static TextureGroup textureGroup;

    public static Shader getShader(String name){
        return shaders.get(name);
    }

    public static void loadShader(String name, String vs,String gs, String fs){
        Shader shader = null;
        try {
            shader = new Shader();
            shader.createVertexShader(loadString(vs));
            shader.createFragmentShader(loadString(fs));
            shader.createGeometryShader(loadString(gs));
            shader.link();
            shaders.put(name,shader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadShader(String name, String vs, String fs){
        Shader shader = null;
        try {
            shader = new Shader();
            shader.createVertexShader(loadString(vs));
            shader.createFragmentShader(loadString(fs));
            shader.link();
            shaders.put(name,shader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void loadTexture(String name, String file){
        //textures.put(name, new TextureGroup(file));
        textures.put("tex", new TextureGroup(new String[0]));
    }*/

    public static Texture getTexture(String name){
        //return textures.get(name);
        return textures.get(name);
    }

    public static void loadTextures(List<TextureDefinition> list){
        //textures.put(name, new TextureGroup(file));
        textureGroup = new TextureGroup(list);
        textures = textureGroup.getTextureDictionary();
    }

    private static String loadString(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + filePath));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
