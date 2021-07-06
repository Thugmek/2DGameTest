package serializers.gameobjects;

import gameobjects.GameObject;
import gameobjects.Sprite;
import gameobjects.entities.Entity;
import org.joml.Vector2f;
import resources.ResourceManager;

public class SEntity implements SGameObject{

    private String texture;
    private Vector2f pos;
    private float speed;

    public SEntity(String texture, Vector2f pos, float spped) {
        this.texture = texture;
        this.pos = pos;
        this.speed = spped;
    }

    @Override
    public GameObject deserialize() {
        Entity e = new Entity(new Sprite(ResourceManager.getTexture(texture)));
        e.setSpeed(speed);
        e.setPos(pos);
        return e;
    }
}
