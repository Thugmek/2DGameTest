package serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import world.WorldMap;
import world.WorldMapChunk;

import java.io.*;
import java.util.Dictionary;
import java.util.Enumeration;

public class MapSerializer {
    public static void serialize(WorldMap map, String file){
        Dictionary<Integer, Dictionary<Integer, WorldMapChunk>> rows = map.getDictionary();

        SWorldMap sMap = new SWorldMap();

        Enumeration<Integer> rowKeys = rows.keys();
        while (rowKeys.hasMoreElements()){
            Integer rowIndex = rowKeys.nextElement();
            Dictionary<Integer, WorldMapChunk> cells = rows.get(rowIndex);
            SWorldMapChunksRow sRows = new SWorldMapChunksRow(rowIndex);
            sMap.getChunkRows().add(sRows);

            Enumeration<Integer> cellKeys = cells.keys();
            while (cellKeys.hasMoreElements()){
                Integer cellIndex = cellKeys.nextElement();
                WorldMapChunk chunk = cells.get(cellIndex);
                SWorldMapChunk sChunk = new SWorldMapChunk(cellIndex);
                sRows.getChunks().add(sChunk);

                sChunk.setTiles(chunk.getTiles());
                //sChunk.setGameObjects(chunk.getGameObjects());
            }
        }

        System.out.println("generating...");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(sMap);
        //System.out.println(json);

        System.out.println("saving..." + json.length());

        try
        {
            FileWriter bw = new FileWriter("src/main/resources/" + file);
            bw.write(json);
            bw.flush();
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("loading...");
        String loadedJson = "";
        try
        {
            BufferedReader bw = new BufferedReader(new FileReader("src/main/resources/" + file));
            loadedJson = bw.readLine();
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("loaded..." + loadedJson.length());

        sMap = gson.fromJson(loadedJson,sMap.getClass());
        System.out.println(sMap.getChunkRows().get(5).getChunks().get(4).getPos());
    }
}
