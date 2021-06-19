package resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class ResourceManager {

    private static Dictionary<String,Shader> shaders = new Hashtable<>();

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
