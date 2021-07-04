package window;

import gameobjects.GameObject;
import gameobjects.entities.Cat;
import gameobjects.entities.Entity;
import gameobjects.entities.entityStates.IdleState;
import gui.Gui;
import gui.PauseMenu;
import input.CursorInput;
import resources.ResourceManager;
import resources.Shader;
import runners.Game;
import util.GarbageCollectionUtils;
import world.WorldMap;
import world.WorldMapChunk;

import java.util.List;
import java.util.Random;

public class GameGameState extends GameState {
    private Window w = Game.getWindow();
    private Camera c = Game.cam;
    private WorldMap map = Game.map;
    private Shader shader = ResourceManager.getShader("shader");
    private Random ran = new Random();
    private List<GameObject> objects;

    public GameGameState(){
    }

    @Override
    public void update(){
        float mapX = c.getPos().x;
        float mapY = c.getPos().y;
        mapX /= WorldMapChunk.CHUNK_SIZE;
        mapY /= WorldMapChunk.CHUNK_SIZE;
        mapX += 0.5f;
        mapY += 0.5f;
        mapX = Math.round(mapX);
        mapY = Math.round(mapY);

        //GAME UPDATE-----------------------------------------------------------------------------------------------
        if(!PauseMenu.getPaused()) {
            CursorInput.update();
            c.update(w.getDelta());
            map.update(-(int)mapX,-(int)mapY,(int)(1/(0.001*WorldMapChunk.CHUNK_SIZE))+1,w.getDelta());
        }
        //GAME RENDER-----------------------------------------------------------------------------------------------
        w.renderStart();
        c.forShader(shader);
        map.render(-(int)mapX,-(int)mapY,(int)(1/(c.getScale()*WorldMapChunk.CHUNK_SIZE))+1);

        CursorInput.render();

        //GAME GUI--------------------------------------------------------------------------------------------------
        Gui.run();

        w.renderEnd();
        GarbageCollectionUtils.update();
    }
}
