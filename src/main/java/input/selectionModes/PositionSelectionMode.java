package input.selectionModes;

import gameobjects.Cursor;
import gameobjects.entities.Entity;
import gameobjects.entities.entityStates.FindingPathState;
import input.CursorInput;
import org.joml.Vector2f;
import org.joml.Vector2i;
import runners.Game;

public class PositionSelectionMode extends SelectionMode {

    Entity e;

    public PositionSelectionMode(Entity e){
        this.e = e;
    }

    @Override
    public void primaryCursorClick(Vector2f mousePos) {
        Vector2f pos = Game.cam.screenPosToWorld(mousePos);
        Vector2f posF = new Vector2f((int)pos.x,(int)pos.y);
        Vector2i posI = new Vector2i((int)pos.x,(int)pos.y);
        Cursor cursor = new Cursor();
        cursor.toPos(posF);
        CursorInput.getCursors().add(cursor);

        e.setState(
                new FindingPathState(e,posI,Game.map)
        );
    }
}
