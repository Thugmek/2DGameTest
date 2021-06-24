package gameobjects;

import org.joml.Vector2f;
import org.joml.Vector2i;
import resources.ResourceManager;
import resources.Texture;
import resources.TextureGroup;

public class Sprite {
    private Model model;
    private TextureGroup texture;

    public Sprite(Texture tex){
        model = new Model(new float[]{
                0,0,0,
                1,0,0,
                1,1,0,

                1,1,0,
                0,0,0,
                0,1,0,
        },new float[]{},tex.getUVs());

        model.setShader(ResourceManager.getShader("shader"));

        model.setPos(new Vector2f(0,0));

    }

    public void setPos(Vector2f pos){
        model.setPos(pos);
    }

    public Vector2f getPos(){
        return model.getPos();
    }

    public void render(){
        //texture.bind();
        model.render();
    }
}
