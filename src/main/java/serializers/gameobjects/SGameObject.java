package serializers.gameobjects;

import gameobjects.GameObject;

import java.io.Serializable;

public interface SGameObject extends Serializable {
    GameObject deserialize();
}
