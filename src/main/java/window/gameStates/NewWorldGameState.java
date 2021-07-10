package window.gameStates;

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
import util.TestWallsBuilder;
import window.Camera;
import window.Window;
import world.Biome;
import world.WorldMap;
import world.WorldMapChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewWorldGameState extends GameState {

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

    public NewWorldGameState(){
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
        if(!loaded) {
            progress = 0;
            mesage = "loading textures";
            renderProgress();

            List<TextureDefinition> textures = new ArrayList<>();

            textures.add(new TextureDefinition("dirt", "sprites\\textures\\Suelo tierra.jpg"));
            textures.add(new TextureDefinition("sand", "sprites\\textures\\Suelo arena.jpg"));
            textures.add(new TextureDefinition("grass", "sprites\\textures\\Suelo hierba.jpg"));
            textures.add(new TextureDefinition("snow", "sprites\\textures\\Suelo nieve.jpg"));
            textures.add(new TextureDefinition("cursor", "sprites\\cursor.png"));
            textures.add(new TextureDefinition("cat", "sprites\\cat.png"));
            textures.add(new TextureDefinition("wallsSandstone", "sprites\\textures\\wallsSandstone.png"));
            textures.add(new TextureDefinition("wallsGranite", "sprites\\textures\\wallsGranite.png"));
            textures.add(new TextureDefinition("wallShadow", "sprites\\textures\\SingleWall.png"));

            ResourceManager.loadTextures(textures);

            Biome.MEADOW.texture = ResourceManager.getTexture("grass");
            Biome.DESERT.texture = ResourceManager.getTexture("sand");
            Biome.SNOW.texture = ResourceManager.getTexture("snow");
            Biome.FORREST.texture = ResourceManager.getTexture("dirt");

            CursorInput.init();

            progress = 0.1f;
            mesage = "building map";
            renderProgress();

            TestWallsBuilder.build(map);

            for (int i = 0; i < 1000; i++) {
                Entity cat = new Cat();
                cat.getPos().add(ran.nextInt(100), ran.nextInt(100));
                cat.setState(new IdleState(cat, map));
                cat.setSpeed(ran.nextFloat() * 3 + 1.5f);
                map.addGameObject(cat);
            }

            progress = 0.2f;
            mesage = "updating chunks";
            renderProgress();

            map.update(0,0,(int)(1/(0.001* WorldMapChunk.CHUNK_SIZE))+1,0);

            progress = 0.5f;
            renderProgress();

            for(int i = -10;i<10;i++){
                for(int j = -10;j<10;j++){
                    map.getChunk(i,j).generateModel();
                    progress += 0.001f;
                    renderProgress();
                }
            }

            progress = 1f;
            mesage = "done";
            renderProgress();

            Game.gameState = new GameGameState();
            loaded = true;
            ResourceManager.getTextureGroup().bind();
        }
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
