package input.selectionModes;

import gameobjects.Cursor;
import gameobjects.LinePath;
import gameobjects.Sprite;
import gameobjects.Wall;
import org.joml.Vector2f;
import resources.ResourceManager;
import resources.Texture;
import runners.Game;
import world.WorldMapChunk;

import java.util.ArrayList;
import java.util.List;

public class WallBuilderSelectionMode extends SelectionMode {

    private Texture tex;
    public static Wall wall = Wall.GRANITE;

    public WallBuilderSelectionMode() {
        tex = ResourceManager.getTexture("wallShadow");
    }

    public void primaryCursorClick(Vector2f mousePos){
        Vector2f v = Game.cam.screenPosToWorld(mousePos);
        float x = v.x;
        float y = v.y;

        if(x < 0) x--;
        if(y < 0) y--;

        Game.map.getTile((int)x,(int)y,true).wall = wall;
        Game.map.getChunkByPos(x,y).generateModel();
    }

    public void secondaryCursorClick(Vector2f mousePos){
        Vector2f v = Game.cam.screenPosToWorld(mousePos);
        float x = v.x;
        float y = v.y;

        if(x < 0) x--;
        if(y < 0) y--;

        Game.map.getTile((int)x,(int)y,true).wall = null;
        Game.map.getChunkByPos(x,y).generateModel();
    }

    public void primaryCursorDrag(Vector2f mousePos, Vector2f mousePos2){
        List<Sprite> sprites = wallLine(mousePos,mousePos2);

        List<WorldMapChunk> toUpdate = new ArrayList<>();

        for(Sprite sp : sprites){
            Vector2f pos = sp.getPos();
            Game.map.getTile((int)pos.x,(int)pos.y,true).wall = wall;

            WorldMapChunk chunk = Game.map.getChunkByPos((int)pos.x,(int)pos.y);

            if(!toUpdate.contains(chunk)) toUpdate.add(chunk);

        }

        for (WorldMapChunk chunk :toUpdate){
            chunk.generateModel();
        }
    }

    public void draw(Vector2f mousePos, Vector2f mousePos2){
        List<Sprite> sprites = wallLine(mousePos,mousePos2);

        for(Sprite sp : sprites){
            sp.render();
        }
    }

    private List<Sprite> wallLine(Vector2f mousePos, Vector2f mousePos2){
        Vector2f v = Game.cam.screenPosToWorld(mousePos);
        float x1 = v.x;
        float y1 = v.y;
        if(x1 < 0) x1--;
        if(y1 < 0) y1--;

        Vector2f v2 = Game.cam.screenPosToWorld(mousePos2);
        float x2 = v2.x;
        float y2 = v2.y;
        if(x2 < 0) x2--;
        if(y2 < 0) y2--;

        int dX = (int)Math.abs(x1-x2);
        int dY = (int)Math.abs(y1-y2);

        List<Sprite> sprites = new ArrayList<>();

        if(dX > dY){
            int sig = (int)Math.signum(x1-x2);
            for(int i = 0;i<=dX;i++){
                Sprite spr = new Sprite(tex);
                spr.setPos(new Vector2f((int)(x1-i*sig),(int)y1));
                sprites.add(spr);
            }
        }else{
            int sig = (int)Math.signum(y1-y2);
            for(int i = 0;i<=dY;i++){
                Sprite spr = new Sprite(tex);
                spr.setPos(new Vector2f((int)x1,(int)(y1-i*sig)));
                sprites.add(spr);
            }
        }

        return sprites;
    }
}
