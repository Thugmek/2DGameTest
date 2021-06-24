package gameobjects;

import org.joml.Vector2f;
import resources.ResourceManager;
import resources.Texture;

public class Cursor {
    private Model model;

    private Texture texture;

    public Cursor(){
        texture = ResourceManager.getTexture("cursor");
        model = new Model(new float[]{
                0,0,0,
                1,0,0,
                0,1,0,

                1,1,0,
                1,0,0,
                0,1,0,
        }, new float[0],texture.getUVs());

        model.setShader(ResourceManager.getShader("shader"));
        model.setPos(new Vector2f(0,0));
    }

    public void render(){
        model.render();
    }

    public void setPos(Vector2f pos){
        model.setPos(pos);
    }
}
