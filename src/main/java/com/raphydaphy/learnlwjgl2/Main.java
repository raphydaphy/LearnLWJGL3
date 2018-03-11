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
import main.java.com.raphydaphy.learnlwjgl2.util.NoiseMapGenerator;
import main.java.com.raphydaphy.learnlwjgl2.util.OpenSimplexNoise;
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
        Random rand = new Random(NoiseMapGenerator.SEED);

       int colors = loader.loadTexture("colors");

	    Material grassMaterial = new Material(colors);
	    Terrain terrain = new Terrain(-1, -1, loader, grassMaterial);

        ModelData treeData = OBJLoader.loadOBJ("tree");
        RawModel treeRaw = loader.loadToVAO(treeData.getVertices(), treeData.getUVS(), treeData.getNormals(), treeData.getIndices());
        Material treeMaterial = new Material(colors);
        treeMaterial.setShineDamper(5);
        treeMaterial.setReflectivity(0.1f);
        TexturedModel treeModel = new TexturedModel(treeRaw, treeMaterial);
        List<ModelTransform> trees = new ArrayList<>();
        for (int i = 0; i < 500; i++)
        {
        	Vector3f treePos = new Vector3f(-rand.nextInt(800), -1f, -rand.nextInt(800));
        	treePos.y = terrain.getHeight(treePos.x, treePos.z) - 1;
            Transform treeTransform = new Transform(treePos, 0, rand.nextInt(360), 0, rand.nextInt(5) + 5);
            trees.add(new ModelTransform(treeTransform, treeModel));
        }

        ModelData playerData = OBJLoader.loadOBJ("person");
        RawModel playerRaw = loader.loadToVAO(playerData.getVertices(), playerData.getUVS(), playerData.getNormals(), playerData.getIndices());
        TexturedModel playerModel = new TexturedModel(playerRaw, new Material(colors));
        Player player = new Player(playerModel, new Vector3f(-400, 0, -400), 0, 180, 0, 0.75f);

        Light sun = new Light(new Vector3f(0, 15, -20), new Vector3f(1, 1, 1));

        Camera camera = new Camera(player);
        RenderManager renderer = new RenderManager();

        while (!Display.isCloseRequested())
        {
            camera.move();
            player.move(terrain);

            renderer.processTerrain(terrain);

            renderer.processSimilarObjects(trees);

            renderer.processObject(player.data);
            renderer.render(sun, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }
}
