package window.gameStates;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameobjects.GameObject;
import gameobjects.Sprite;
import gameobjects.entities.Cat;
import gameobjects.entities.Entity;
import gameobjects.entities.entityStates.IdleState;
import gui.Gui;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import input.CursorInput;
import resources.ResourceManager;
import resources.Shader;
import resources.TextureDefinition;
import resources.TextureGroup;
import runners.Game;
import serializers.*;
import serializers.gameobjects.SEntity;
import util.TestWallsBuilder;
import window.Camera;
import window.Window;
import world.Biome;
import world.WorldMap;
import world.WorldMapChunk;
import world.WorldMapTile;

import java.io.*;
import java.util.*;

public class SaveGameState extends GameState {
    private Window w = Game.getWindow();
    private Camera c = Game.cam;
    private WorldMap map = Game.map;
    private Shader shader = ResourceManager.getShader("shader");
    private Random ran = new Random();

    private boolean loaded = false;
    private float progress = 0;
    private String mesage = "";
    private TextureGroup loadingScreen;
    private Sprite loadingScreenSprite;

    private String file = "saves/save.bin";

    public SaveGameState(){
        List<TextureDefinition> texs = new ArrayList<>();
        texs.add(new TextureDefinition("image","graphics\\loadingScreen.png"));
        loadingScreen = new TextureGroup(texs);
        loadingScreenSprite = new Sprite(loadingScreen.getTextureDictionary().get("image"));
        shader.setUniform1i("sampler", 0);
        c.forShader(shader);
        shader.setShaderMode(3);
    }

    @Override
    public void update() {
        mesage = "Saving...";
        renderProgress();

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

                WorldMapTile[][] chunkArray = chunk.getTiles();
                SWorldMapTile[][] sChunkArray = new SWorldMapTile[WorldMapChunk.CHUNK_SIZE][WorldMapChunk.CHUNK_SIZE];

                for(int i = 0;i<WorldMapChunk.CHUNK_SIZE;i++){
                    for(int j = 0;j<WorldMapChunk.CHUNK_SIZE;j++){
                        SWorldMapTile sTile = new SWorldMapTile();
                        sTile.biome = EnumConvertor.serializeBiome(chunkArray[i][j].biome);
                        sTile.wall = EnumConvertor.serializeWall(chunkArray[i][j].wall);
                        sChunkArray[i][j] = sTile;
                    }
                }

                sChunk.setTiles(sChunkArray);
                for(GameObject g:chunk.getGameObjects()) {
                    sChunk.getGameObjects().add(g.serialize());
                }
            }
        }

        progress = 0.1f;
        mesage = "Serializing...";
        renderProgress();

        /*GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(sMap);
        //System.out.println(json);

        progress = 0.6f;
        mesage = "Saving to file";
        renderProgress();

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
        }*/

        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream f = new FileOutputStream("src/main/resources/" + file);
            ObjectOutputStream out = new ObjectOutputStream(f);

            // Method for serialization of object
            out.writeObject(sMap);

            out.close();
            f.close();

            System.out.println("Object has been serialized");

        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        progress = 1f;
        mesage = "Saved";
        renderProgress();
        Game.gameState = new MenuGameState();
    }

    @Override
    public void gui() {
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        ImGui.setNextWindowPos((width/2)-150,(height/2)-60);
        ImGui.setNextWindowSize(300,120);
        ImGui.begin("Saving", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse);
        ImGui.progressBar(progress);
        ImGui.labelText("",mesage);
        ImGui.end();
    }

    private void renderProgress(){

        w.renderStart();
        loadingScreen.bind();
        loadingScreenSprite.render();
        Gui.run(this);

        w.renderEnd();
    }
}
