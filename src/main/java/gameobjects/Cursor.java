package gameobjects;

import org.joml.Vector2f;
import resources.ResourceManager;
import resources.Texture;

public class Cursor {
    private Model model;
    private Texture texture;

    private SelectOption selectOption = SelectOption.NONE;

    private Vector2f pos;
    private GameObject object;
    private boolean display;

    public Cursor(){
        texture = ResourceManager.getTexture("cursor");
        model = new Model(new float[]{
                0,0,-1,
                1,0,-1,
                0,1,-1,

                1,1,-1,
                1,0,-1,
                0,1,-1,
        }, new float[0],texture.getUVs());

        model.setShader(ResourceManager.getShader("shader"));
        model.setPos(new Vector2f(0,0));
    }
    public void render(){
        switch (selectOption){
            case NONE:
                display = false;
                break;
            case BY_TILE:
                display = true;
                model.setPos(pos);
                break;
            case BY_OBJECT:
                display = true;
                model.setPos(object.getPos());
                break;
        }

        if(display) model.render();
    }

    public void toPos(Vector2f pos){
        selectOption = SelectOption.BY_TILE;
        this.pos = pos;
    }

    public void toObject(GameObject object){
        selectOption = SelectOption.BY_OBJECT;
        this.object = object;
    }

    public void toNone(){
        selectOption = SelectOption.NONE;
    }
}

enum SelectOption{
    NONE,
    BY_TILE,
    BY_OBJECT
}
