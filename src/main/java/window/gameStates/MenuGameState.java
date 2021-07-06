package window.gameStates;

import gameobjects.Sprite;
import gui.Gui;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import resources.ResourceManager;
import resources.Shader;
import resources.TextureDefinition;
import resources.TextureGroup;
import runners.Game;
import serializers.SaveDefinition;
import window.Camera;
import window.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class MenuGameState extends GameState {

    private Window w = Game.getWindow();
    private Camera c = Game.cam;
    private Shader shader = ResourceManager.getShader("shader");

    private TextureGroup menuScreen;
    private Sprite menuScreenSprite;

    CurrentMenu currentMenu = CurrentMenu.MENU;

    private List<SaveDefinition> saves;

    public MenuGameState(){
        List<TextureDefinition> texs = new ArrayList<>();
        texs.add(new TextureDefinition("image","graphics\\loadingScreen.png"));
        menuScreen = new TextureGroup(texs);
        menuScreenSprite = new Sprite(menuScreen.getTextureDictionary().get("image"));
        shader.setUniform1i("sampler", 0);
        c.forShader(shader);
        shader.setShaderMode(3);
        loadSaves();
    }

    @Override
    public void update() {
        w.renderStart();
        menuScreen.bind();
        menuScreenSprite.render();
        Gui.run(this);

        w.renderEnd();
    }

    @Override
    public void gui() {
        ImGui.showDemoWindow();
        switch (currentMenu){
            case MENU:
                menuGui();
                break;
            case LOAD:
                loadGui();
                break;
            case SETTINGS:
                settingsGui();
                break;
        }
    }

    private void menuGui(){
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        int menuWidth = 400;
        int menuHeight = 600;

        ImGui.setNextWindowPos(width/2-menuWidth/2,height/2-menuHeight/2);
        ImGui.setNextWindowSize(menuWidth,menuHeight);
        ImGui.begin("Main menu", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse|ImGuiWindowFlags.NoDecoration);
        ImGui.pushFont(Gui.font2);
        if(ImGui.button("New World",380,60)){
            Game.gameState = new NewWorldGameState();
        }
        if(ImGui.button("Load World",380,60)){
            currentMenu = CurrentMenu.LOAD;
        }
        if(ImGui.button("Settings",380,60)){
            currentMenu = CurrentMenu.SETTINGS;
        }
        if(ImGui.button("Exit",380,60)){
            glfwSetWindowShouldClose(Game.getWindow().getId(),true);
        }
        ImGui.popFont();
        ImGui.end();
    }

    private void loadGui(){
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        int menuWidth = 400;
        int menuHeight = 600;

        ImGui.setNextWindowPos(width/2-menuWidth/2,height/2-menuHeight/2);
        ImGui.setNextWindowSize(menuWidth,menuHeight);
        ImGui.begin("Load", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse|ImGuiWindowFlags.NoDecoration);
        ImGui.beginTable("saves",2);
        ImGui.tableSetupColumn("Name");
        ImGui.tableSetupColumn("Action");
        int i = 0;
        for(SaveDefinition save:saves) {
            ImGui.pushID(i);
            ImGui.tableNextRow();
            ImGui.tableNextColumn();
            ImGui.text(save.getName());
            ImGui.tableNextColumn();
            if (ImGui.button("Load")) {
                Game.gameState = new LoadGameState();
            }
            ImGui.sameLine();
            if (ImGui.button("Delete")) {
                Game.gameState = new LoadGameState();
            }
            ImGui.popID();
            i++;
        }
        ImGui.endTable();
        ImGui.pushFont(Gui.font2);
        if(ImGui.button("Back",380,60)){
            currentMenu = CurrentMenu.MENU;
        }
        ImGui.popFont();
        ImGui.end();
    }

    private void settingsGui(){
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        int menuWidth = (int)(width*0.8f);
        int menuHeight = (int)(height*0.8f);

        ImGui.setNextWindowPos(width/2-menuWidth/2,height/2-menuHeight/2);
        ImGui.setNextWindowSize(menuWidth,menuHeight);
        ImGui.begin("Settings", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse|ImGuiWindowFlags.NoDecoration);
        ImGui.pushFont(Gui.font2);
        ImGui.sliderInt("slider 1",new int[]{27},0,100);
        ImGui.sliderInt("slider 2",new int[]{10},0,25);
        ImGui.sliderInt("slider 3",new int[]{2},0,3);
        ImGui.sliderInt("slider 4",new int[]{8},0,10);
        if(ImGui.button("Back",380,60)){
            currentMenu = CurrentMenu.MENU;
        }
        ImGui.popFont();
        ImGui.end();
    }

    private void loadSaves(){
        saves = new ArrayList<>();
        try {
            Files.list(new File("src/main/resources/saves/").toPath()).forEach(a -> {
                System.out.println(a);
                saves.add(new SaveDefinition(a));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum CurrentMenu{
        MENU,
        LOAD,
        SETTINGS
    }
}
