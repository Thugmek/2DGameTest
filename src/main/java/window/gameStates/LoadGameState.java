package window.gameStates;

import gameobjects.Sprite;
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
import serializers.gameobjects.SGameObject;
import window.Camera;
import window.Window;
import world.Biome;
import world.WorldMap;
import world.WorldMapChunk;
import world.WorldMapTile;

import java.io.*;
import java.util.*;

public class LoadGameState extends GameState {
    private Window w = Game.getWindow();
    private Camera c = Game.cam;
    private WorldMap map = Game.map;
    private Shader shader = ResourceManager.getShader("shader");

    private String file = "saves/save.bin";
    private float progress = 0;
    private String mesage = "";
    private TextureGroup loadingScreen;
    private Sprite loadingScreenSprite;

    public LoadGameState(){
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
        progress = 0;
        mesage = "loading textures";
        renderProgress();

        List<TextureDefinition> textures = new ArrayList<>();

        textures.add(new TextureDefinition("dirt", "sprites\\textures\\Suelo tierra.jpg"));
        textures.add(new TextureDefinition("sand", "sprites\\textures\\Suelo arena.jpg"));
        textures.add(new TextureDefinition("grass", "sprites\\textures\\Suelo hierba.jpg"));
        textures.add(new TextureDefinition("cursor", "sprites\\cursor.png"));
        textures.add(new TextureDefinition("cat", "sprites\\cat.png"));
        textures.add(new TextureDefinition("wallsSandstone", "sprites\\textures\\wallsSandstone.png"));
        textures.add(new TextureDefinition("wallsGranite", "sprites\\textures\\wallsGranite.png"));
        textures.add(new TextureDefinition("wallShadow", "sprites\\textures\\SingleWall.png"));

        ResourceManager.loadTextures(textures);

        Biome.MEADOW.texture = ResourceManager.getTexture("grass");
        Biome.DESERT.texture = ResourceManager.getTexture("dirt");

        CursorInput.init();

        progress = 0.1f;
        mesage = "loading save file";
        renderProgress();

        /*String loadedJson = "";
        try
        {
            BufferedReader bw = new BufferedReader(new FileReader("src/main/resources/" + file));
            loadedJson = bw.readLine();
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        SWorldMap sMap = null;

        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream f = new FileInputStream("src/main/resources/" + file);
            ObjectInputStream in = new ObjectInputStream(f);

            // Method for deserialization of object
            sMap = (SWorldMap) in.readObject();

            in.close();
            f.close();

            System.out.println("Object has been deserialized ");
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        progress = 0.3f;
        mesage = "decoding save";
        renderProgress();

        /*GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        SWorldMap sMap = gson.fromJson(loadedJson,SWorldMap.class);*/

        progress = 0.5f;
        mesage = "building world";
        renderProgress();

        WorldMap map = new WorldMap();
        Game.map = map;

        List<WorldMapChunk> loadedChunks = new ArrayList<>();

        List<SWorldMapChunksRow> rows = sMap.getChunkRows();

        float loadStep = 0.2f/rows.size();

        Dictionary<Integer,Dictionary<Integer,WorldMapChunk>> mapDict = map.getDictionary();
        for(SWorldMapChunksRow row:rows){
            List<SWorldMapChunk> chunks = row.getChunks();
            Dictionary<Integer,WorldMapChunk> rowDict = new Hashtable<>();
            mapDict.put(row.getPos(),rowDict);
            for(SWorldMapChunk chunk:chunks){
                WorldMapChunk mapChunk = new WorldMapChunk(row.getPos(),chunk.getPos());
                //mapChunk.setTiles(chunk.getTiles());
                loadedChunks.add(mapChunk);
                rowDict.put(chunk.getPos(),mapChunk);

                SWorldMapTile[][] sChunkArray = chunk.getTiles();
                WorldMapTile[][] chunkArray = new WorldMapTile[WorldMapChunk.CHUNK_SIZE][WorldMapChunk.CHUNK_SIZE];

                for(int i = 0;i<WorldMapChunk.CHUNK_SIZE;i++){
                    for(int j = 0;j<WorldMapChunk.CHUNK_SIZE;j++){
                        WorldMapTile tile = new WorldMapTile();
                        tile.biome = EnumConvertor.deserializeBiome(sChunkArray[i][j].biome);
                        tile.wall = EnumConvertor.deserializeWall(sChunkArray[i][j].wall);
                        chunkArray[i][j] = tile;
                    }
                }

                mapChunk.setTiles(chunkArray);

                for (SGameObject g:chunk.getGameObjects()){
                    mapChunk.getGameObjects().add(g.deserialize());
                }
            }

            progress += loadStep;
            renderProgress();

        }
        progress = 0.7f;
        mesage = "building world model";
        renderProgress();

        loadStep = 0.3f/loadedChunks.size();

        for(WorldMapChunk chunk:loadedChunks){
            chunk.generateModel();
            progress += loadStep;
            renderProgress();
        }

        progress = 1f;
        mesage = "done";
        renderProgress();

        Game.gameState = new GameGameState();
        ResourceManager.getTextureGroup().bind();
    }

    @Override
    public void gui() {
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        ImGui.setNextWindowPos((width/2)-200,(height/2)-60);
        ImGui.setNextWindowSize(400,120);
        ImGui.begin("Loading", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse);
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
