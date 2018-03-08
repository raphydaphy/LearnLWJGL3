package main.java.com.raphydaphy.learnlwjgl2;

import main.java.com.raphydaphy.learnlwjgl2.entity.Player;
import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.render.Light;
import main.java.com.raphydaphy.learnlwjgl2.render.ModelTransform;
import main.java.com.raphydaphy.learnlwjgl2.render.Transform;
import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.*;
import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.load.Loader;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.load.ModelData;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.load.OBJLoader;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.renderer.RenderManager;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.Material;
import main.java.com.raphydaphy.learnlwjgl2.terrain.Terrain;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        DisplayManager.createDisplay("LearnLWJGL2");

        Loader loader = new Loader();
        Random rand = new Random();

        ModelData stallData = OBJLoader.loadOBJ("stall");
        RawModel stallRaw = loader.loadToVAO(stallData.getVertices(), stallData.getUVS(), stallData.getNormals(), stallData.getIndices());
        Material stallMaterial = new Material(loader.loadTexture("stall"));
        stallMaterial.setShineDamper(10);
        stallMaterial.setReflectivity(1);
        TexturedModel stallModel = new TexturedModel(stallRaw, stallMaterial);
        Transform stallTransform = new Transform(new Vector3f(0, 0, -50), 0, 35, 0, 1);
        ModelTransform stall = new ModelTransform(stallTransform, stallModel);

        ModelData treeData = OBJLoader.loadOBJ("tree");
        RawModel treeRaw = loader.loadToVAO(treeData.getVertices(), treeData.getUVS(), treeData.getNormals(), treeData.getIndices());
        Material treeMaterial = new Material(loader.loadTexture("colors"));
        treeMaterial.setShineDamper(5);
        treeMaterial.setReflectivity(0.1f);
        TexturedModel treeModel = new TexturedModel(treeRaw, treeMaterial);
        List<ModelTransform> trees = new ArrayList<>();
        for (int i = 0; i < 500; i++)
        {
            Transform treeTransform = new Transform(new Vector3f(rand.nextInt(1600) - 800, 0, -rand.nextInt(800)), 0, rand.nextInt(360), 0, rand.nextInt(5) + 5);
            trees.add(new ModelTransform(treeTransform, treeModel));
        }

        ModelData playerData = OBJLoader.loadOBJ("person");
        RawModel playerRaw = loader.loadToVAO(playerData.getVertices(), playerData.getUVS(), playerData.getNormals(), playerData.getIndices());
        Material playerMaterial = new Material(loader.loadTexture("playerTexture"));
        TexturedModel playerModel = new TexturedModel(playerRaw, playerMaterial);
        Player player = new Player(playerModel, new Vector3f(0, 0, 0), 0, 180, 0, 0.5f);

        Material grassMaterial = new Material(loader.loadTexture("grass"));
        Terrain terrain = new Terrain(-1, -1, loader, grassMaterial);
        Terrain terrain1 = new Terrain(0, -1, loader, grassMaterial);
        Terrain terrain2 = new Terrain(1, -1, loader, grassMaterial);



        Light sun = new Light(new Vector3f(0, 15, -20), new Vector3f(1, 1, 1));

        Camera camera = new Camera(player);
        RenderManager renderer = new RenderManager();

        while (!Display.isCloseRequested())
        {
            camera.move();
            player.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain1);
            renderer.processTerrain(terrain2);

            renderer.processObject(stall);

            renderer.processSimilarObjects(trees);

            renderer.processObject(player.data);

            renderer.processObject(stall);
            renderer.render(sun, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }
}
