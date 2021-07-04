package window;

import gameobjects.GameObject;
import gameobjects.entities.Cat;
import gameobjects.entities.Entity;
import gameobjects.entities.entityStates.IdleState;
import input.CursorInput;
import resources.ResourceManager;
import resources.Shader;
import resources.TextureDefinition;
import runners.Game;
import util.TestWallsBuilder;
import world.Biome;
import world.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadingGameState extends GameState {

    private Window w = Game.getWindow();
    private Camera c = Game.cam;
    private WorldMap map = Game.map;
    private Shader shader = ResourceManager.getShader("shader");
    private Random ran = new Random();
    private List<GameObject> objects;

    @Override
    public void update() {

        List<TextureDefinition> textures = new ArrayList<>();

        textures.add(new TextureDefinition("dirt","sprites\\textures\\Suelo tierra.jpg"));
        textures.add(new TextureDefinition("sand","sprites\\textures\\Suelo arena.jpg"));
        textures.add(new TextureDefinition("grass","sprites\\textures\\Suelo hierba.jpg"));
        textures.add(new TextureDefinition("cursor","sprites\\cursor.png"));
        textures.add(new TextureDefinition("cat","sprites\\cat.png"));
        textures.add(new TextureDefinition("wallsSandstone","sprites\\textures\\wallsSandstone.png"));
        textures.add(new TextureDefinition("wallsGranite","sprites\\textures\\wallsGranite.png"));
        textures.add(new TextureDefinition("wallShadow","sprites\\textures\\SingleWall.png"));

        ResourceManager.loadTextures(textures);

        ResourceManager.getShader("shader").setUniform1i("sampler",0 );

        Biome.MEADOW.texture = ResourceManager.getTexture("grass");
        Biome.DESERT.texture = ResourceManager.getTexture("dirt");

        CursorInput.init();

        TestWallsBuilder.build(map);

        for(int i = 0;i<1000;i++){
            Entity cat = new Cat(map);
            cat.getPos().add(ran.nextInt(100),ran.nextInt(100));
            cat.setState(new IdleState(cat,map));
            cat.setSpeed(ran.nextFloat()*3+1.5f);
            map.addGameObject(cat);
        }

        Game.gameState = new GameGameState();
    }
}
