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
import serializers.MapSerializer;
import util.TestWallsBuilder;
import window.Camera;
import window.Window;
import world.Biome;
import world.WorldMap;
import world.WorldMapChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public SaveGameState(){
        List<TextureDefinition> texs = new ArrayList<>();
        texs.add(new TextureDefinition("image","graphics\\loadingScreen.png"));
        loadingScreen = new TextureGroup(texs);
        loadingScreenSprite = new Sprite(loadingScreen.getTextureDictionary().get("image"));
        shader.setUniform1i("sampler", 0);
        c.forShader(shader);
        shader.setShaderMode(3);

        MapSerializer.serialize(Game.map,"saves/save.txt");
    }

    @Override
    public void update() {
        progress += 0.01f;
        mesage = "Saving...";
        renderProgress();

        if(progress > 1) Game.gameState = new MenuGameState();
    }

    @Override
    public void gui() {
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        ImGui.setNextWindowPos((width/2)-150,(height/2)-50);
        ImGui.setNextWindowSize(300,100);
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
