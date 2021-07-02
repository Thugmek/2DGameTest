package util;

import world.WorldMap;

public class TestWallsBuilder {
    public static void build(WorldMap map){
        for(int i = 0;i<256;i++){
            int x = i%16;
            int y = i/16;
            buildPart(x*4,y*4,i,map);
        }

        for(int i = -1;i<5;i++){
            for(int j=-1;j<5;j++){
                map.getChunk(i,j).generateModel();
            }
        }

    }

    private static void buildPart(int x, int y, int val, WorldMap map){
        boolean [] walls = new boolean[8];
        for (int i = 0; i < 8; ++i) {
            walls[i] = (val & (1 << i)) != 0;
        }

        map.getTile(x-1,y+1,true).wall = walls[0];
        map.getTile(x,y+1,true).wall = walls[1];
        map.getTile(x+1,y+1,true).wall = walls[2];

        map.getTile(x-1,y,true).wall = walls[3];
        map.getTile(x,y,true).wall = true;
        map.getTile(x+1,y,true).wall = walls[4];

        map.getTile(x-1,y-1,true).wall = walls[5];
        map.getTile(x,y-1,true).wall = walls[6];
        map.getTile(x+1,y-1,true).wall = walls[7];

    }
}
