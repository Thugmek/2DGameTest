package gameobjects;

import org.joml.Vector2f;
import serializers.gameobjects.SGameObject;

import java.io.Serializable;

public interface GameObject extends Serializable {
    void update(float delta);
    void render();
    Vector2f getPos();
    SGameObject serialize();
}
