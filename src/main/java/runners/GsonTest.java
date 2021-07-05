package runners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;
import org.joml.Vector2i;
import serializers.SWorldMap;
import serializers.SWorldMapChunk;
import serializers.SWorldMapChunksRow;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

public class GsonTest {
    public static void main(String[] args) {

        SWorldMap sWordl = new SWorldMap();
        SWorldMapChunksRow sWorldMapChunksRow = new SWorldMapChunksRow(1);
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(1));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(2));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(4));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(6));
        sWordl.getChunkRows().add(sWorldMapChunksRow);

        sWorldMapChunksRow = new SWorldMapChunksRow(2);
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(1));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(2));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(3));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(4));
        sWordl.getChunkRows().add(sWorldMapChunksRow);

        sWorldMapChunksRow = new SWorldMapChunksRow(3);
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(1));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(2));
        sWordl.getChunkRows().add(sWorldMapChunksRow);

        sWorldMapChunksRow = new SWorldMapChunksRow(4);
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(4));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(5));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(6));
        sWorldMapChunksRow.getChunks().add(new SWorldMapChunk(7));
        sWordl.getChunkRows().add(sWorldMapChunksRow);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(sWordl);
        System.out.println(json);

        sWordl = gson.fromJson(json, SWorldMap.class);
        System.out.println(sWordl.getChunkRows().size());
    }
}
