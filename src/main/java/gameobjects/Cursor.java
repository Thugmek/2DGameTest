package gameobjects;

import org.joml.Vector2f;
import resources.ResourceManager;

public class Cursor {
    private Model model;

    public Cursor(){
        model = new Model(new float[]{
                0,0,0,
                1,0,0,
                0,1,0,

                1,1,0,
                1,0,0,
                0,1,0,
        }, new float[0],new float[]{
                0,0,
                1,0,
                0,1,

                1,1,
                1,0,
                0,1,
        });

        model.setShader(ResourceManager.getShader("shader"));
        model.setPos(new Vector2f(32,32));
    }

    public void render(){
        model.render();
    }
}
