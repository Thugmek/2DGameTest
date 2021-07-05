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
import window.Camera;
import window.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class MenuGameState extends GameState {

    private Window w = Game.getWindow();
    private Camera c = Game.cam;
    private Shader shader = ResourceManager.getShader("shader");

    private TextureGroup menuScreen;
    private Sprite menuScreenSprite;

    public MenuGameState(){
        List<TextureDefinition> texs = new ArrayList<>();
        texs.add(new TextureDefinition("image","graphics\\loadingScreen.png"));
        menuScreen = new TextureGroup(texs);
        menuScreenSprite = new Sprite(menuScreen.getTextureDictionary().get("image"));
        shader.setUniform1i("sampler", 0);
        c.forShader(shader);
        shader.setShaderMode(3);
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
        int width = Game.getWindow().getWidth();
        int height = Game.getWindow().getHeight();
        int menuWidth = 400;
        int menuHeight = 600;

        ImGui.setNextWindowPos(width/2-menuWidth/2,height/2-menuHeight/2);
        ImGui.setNextWindowSize(menuWidth,menuHeight);
        ImGui.begin("Main menu", ImGuiWindowFlags.NoResize|ImGuiWindowFlags.NoCollapse|ImGuiWindowFlags.NoDecoration);
        ImGui.pushFont(Gui.font2);
        if(ImGui.button("Play",380,60)){
            Game.gameState = new LoadingGameState();
        }
        if(ImGui.button("Exit",380,60)){
            glfwSetWindowShouldClose(Game.getWindow().getId(),true);
        }
        ImGui.popFont();
        ImGui.end();
    }
}
