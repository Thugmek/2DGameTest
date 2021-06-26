package gameobjects;

import org.joml.Vector2f;

public interface GameObject {
    void update(float delta);
    void render();
    Vector2f getPos();
}
