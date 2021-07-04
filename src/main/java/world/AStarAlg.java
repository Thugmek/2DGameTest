package world;

import org.joml.Vector2i;

import java.sql.SQLOutput;
import java.util.*;

public class AStarAlg {
    private Vector2i start;
    private Vector2i end;

    private Map<Integer,Map<Integer,WorldMapChunk>> world;
    private Map<Integer,Map<Integer,AlgTile>> map;
    private Map<Integer,Map<Integer,AlgTile>> heads;
    private List<AlgTile> headsList;
    private boolean solved = false;
    private WorldMap worldMap;

    private List<AlgTile> result;
    private List<Vector2i> resultVecs;


    public AStarAlg(Vector2i start, Vector2i end, WorldMap worldMap) {
        this.start = start;
        this.end = end;
        this.worldMap = worldMap;

        map = new HashMap<>();

        AlgTile s = new AlgTile();
        s.pos = start;
        s.a = 0;
        s.h = (float) start.distance(end);

        heads = new HashMap<>();
        heads.put(start.x,new HashMap<>());
        heads.get(start.x).put(start.y,s);

        headsList = new LinkedList<>();
        headsList.add(s);

        long time = System.nanoTime();
        boolean failed = false;
        int iteration = 0;
        while(!solved){

            if(iteration > 10000 || headsList.size() < 1){
                failed = true;
                break;
            }

            iteration++;
            AlgTile bestHead = findBestHead();

            solveTile(bestHead.pos.x-1,bestHead.pos.y-1,bestHead);
            solveTile(bestHead.pos.x-1,bestHead.pos.y,bestHead);
            solveTile(bestHead.pos.x-1,bestHead.pos.y+1,bestHead);

            solveTile(bestHead.pos.x,bestHead.pos.y-1,bestHead);
            solveTile(bestHead.pos.x,bestHead.pos.y+1,bestHead);

            solveTile(bestHead.pos.x+1,bestHead.pos.y-1,bestHead);
            solveTile(bestHead.pos.x+1,bestHead.pos.y,bestHead);
            solveTile(bestHead.pos.x+1,bestHead.pos.y+1,bestHead);

            heads.get(bestHead.pos.x).remove(bestHead.pos.y);
            headsList.remove(bestHead);

            Map<Integer,AlgTile> row = map.get(bestHead.pos.x);
            if(row == null){
                row = new HashMap<>();
                map.put(bestHead.pos.x,row);
            }

            row.put(bestHead.pos.y,bestHead);
            Thread.currentThread().interrupt();
        }

        if(!failed) {

            result = new LinkedList<>();
            result.add(map.get(end.x).get(end.y));

            resultVecs = new LinkedList<>();
            resultVecs.add(result.get(0).pos);

            while (result.get(0).pos.x != start.x || result.get(0).pos.y != start.y) {
                float min = result.get(0).a;
                int minX = result.get(0).pos.x;
                int minY = result.get(0).pos.y;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Map<Integer, AlgTile> row = map.get(result.get(0).pos.x + i);
                        if (row != null) {
                            AlgTile t = row.get(result.get(0).pos.y + j);
                            if (t != null && t.a < min) {
                                min = t.a;
                                minX = t.pos.x;
                                minY = t.pos.y;
                            }
                        }
                    }
                }

                result.add(0, map.get(minX).get(minY));
                resultVecs.add(0, result.get(0).pos);
            }
        }
    }

    private AlgTile findBestHead() {
        float min = Float.MAX_VALUE;
        float minH = Float.MAX_VALUE;
        AlgTile minTile = null;

        for(AlgTile tile:headsList){
            float v = tile.a+tile.h;
            if(v<=min && tile.h<=minH){
                min = v;
                minH = tile.h;
                minTile = tile;
            }
        }

        if(minTile == null){
            System.out.println("null");
        }

        return minTile;
    }

    private void solveTile(int x, int y, AlgTile prev){

        if(worldMap.getTile(x,y,true).wall != null) return;

        AlgTile val = new AlgTile();
        val.pos = new Vector2i(x,y);
        val.h = (float)end.distance(x,y)*2;
        val.a = (float)prev.pos.distance(x,y)+prev.a;

        if(x == end.x && y == end.y){
            solved = true;
            Map<Integer,AlgTile> row = map.get(x);
            if(row == null){
                row = new HashMap<>();
                map.put(x,row);
            }

            row.put(y,val);
        };

        boolean isOnMap = false;
        Map<Integer,AlgTile> mapRow = map.get(x);
        if(mapRow != null){
            AlgTile mapCell = mapRow.get(y);
            if(mapCell != null){
                if(mapCell.a+mapCell.h>val.a+val.h) mapRow.put(y,val);
                isOnMap = true;
            }
        }

        if(!isOnMap){
            Map<Integer,AlgTile> row = heads.get(x);
            if(row == null){
                row = new HashMap<>();
                heads.put(x,row);
            }

            AlgTile cell = row.get(y);
            if(cell == null){
                row.put(y,val);
                headsList.add(val);
            }else{
                if(cell.a+cell.h>val.a+val.h){
                    //row.put(y,val);
                    cell.a = val.a;
                    cell.h = val.h;
                    cell.pos = val.pos;
                }
            }
        }
    }

    private class AlgTile{
        public float a;
        public float h;
        public Vector2i pos;

        public String toString(){
            return String.format("Distance: %f, heuristic: %f, pos[%d|%d]",a,h,pos.x,pos.y);
        }
    }

    public void printResult(){
        System.out.print("Path:");
        for (Vector2i v:resultVecs) {
            System.out.print(String.format(" [%d:%d]",v.x,v.y));
        }
    }

    public List<Vector2i> getResult(){
        return resultVecs;
    }
}
