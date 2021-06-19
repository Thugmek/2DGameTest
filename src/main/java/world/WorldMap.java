package world;

import window.Renderable;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class WorldMap {

    private Dictionary<Integer,Dictionary<Integer,WorldMapChunk>> map;

    public WorldMap(){
        map = new Hashtable<>();
    }

    public void generate(int x, int y){
        if(map.get(x) == null){
            System.out.println("generating row: " + x);
            map.put(x,new Hashtable<>());
        }

        map.get(x).put(y,new WorldMapChunk(x,y));
    }

    public void render(int x, int y, int zoom) {
        for(int i = x-zoom;i<=x+zoom;i++){
            for(int j = y-zoom;j<=y+zoom;j++){
                map.get(i).get(j).render();
            }
        }
    }
}
