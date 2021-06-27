package input.selectionModes;

import gameobjects.Cursor;
import gameobjects.GameObject;
import gameobjects.entities.Entity;
import gui.DevStatsWindow;
import gui.EntityDetailWindow;
import gui.RightClickMenu;
import org.joml.Vector2f;
import runners.Game;
import world.WorldMapChunk;

import java.util.ArrayList;
import java.util.List;

public class EntitySelectionMode extends SelectionMode{
    public EntitySelectionMode(List<Cursor> cursors) {
        super(cursors);
    }

    public void primaryCursorClick(Vector2f mousePos){
        Vector2f v = Game.cam.screenPosToWorld(mousePos);

        GameObject nearest = getObject(v);

        if(nearest instanceof Entity)
            EntityDetailWindow.go = nearest;

        if(nearest != null) {
            Cursor cursor = new Cursor();
            cursor.toObject(nearest);
            cursors.add(cursor);
        }

        DevStatsWindow.mousePos = v;
        RightClickMenu.setActive(false);
    }
    public void secondaryCursorClick(Vector2f mousePos){
        //cursor.toNone();
        RightClickMenu.open((int)mousePos.x,(int)mousePos.y);
    }
    public void primaryCursorDrag(Vector2f mousePos, Vector2f mousePos2){
        Vector2f v = Game.cam.screenPosToWorld(mousePos);
        Vector2f v2 = Game.cam.screenPosToWorld(mousePos2);

        float x1in = v.x;
        float y1in = v.y;
        float x2in = v2.x;
        float y2in = v2.y;

        float x1 = Math.min(x1in,x2in);
        float y1 = Math.min(y1in,y2in);
        float x2 = Math.max(x1in,x2in);
        float y2 = Math.max(y1in,y2in);

        WorldMapChunk startChunk = Game.map.getChunkByPos(x1,y1);
        WorldMapChunk endChunk = Game.map.getChunkByPos(x2,y2);

        List<Entity> selected = new ArrayList<>();
        List<WorldMapChunk> chunks = new ArrayList<>();

        if(startChunk == endChunk){
            chunks.add(startChunk);
        }else{
            int chunkX1 = startChunk.getX();
            int chunkY1 = startChunk.getY();
            int chunkX2 = endChunk.getX();
            int chunkY2 = endChunk.getY();

            for(int i = chunkX1;i<=chunkX2;i++){
                for(int j = chunkY1;j<=chunkY2;j++){
                    chunks.add(Game.map.getChunk(i,j));
                }
            }

        }

        for(WorldMapChunk chunk:chunks){
            List<GameObject> gameObjects = chunk.getGameObjects();
            for(GameObject go : gameObjects){
                Vector2f pos = go.getPos();
                if(pos.x > x1 && pos.x < x2 && pos.y > y1 && pos.y < y2){
                    if(go instanceof Entity) {
                        selected.add((Entity) go);
                        Cursor cursor = new Cursor();
                        cursor.toObject(go);
                        cursors.add(cursor);
                    }

                }
            }
        }

    }
    public void secondaryCursorDrag(Vector2f mousePos, Vector2f mousePos2){
    }
}
