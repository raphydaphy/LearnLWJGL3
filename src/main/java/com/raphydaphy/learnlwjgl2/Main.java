package main.java.com.raphydaphy.learnlwjgl2;

import main.java.com.raphydaphy.learnlwjgl2.entity.Player;
import main.java.com.raphydaphy.learnlwjgl2.font.FontType;
import main.java.com.raphydaphy.learnlwjgl2.font.GUIText;
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
import main.java.com.raphydaphy.learnlwjgl2.renderengine.renderer.FontRenderManager;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.renderer.RenderManager;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.Material;
import main.java.com.raphydaphy.learnlwjgl2.terrain.MousePicker;
import main.java.com.raphydaphy.learnlwjgl2.terrain.Terrain;
import main.java.com.raphydaphy.learnlwjgl2.util.NoiseMapGenerator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;
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
		FontRenderManager.init(loader);

		FontType arial = new FontType(loader.loadTextureExact("src/main/resources/fonts/arial.png", "PNG"), new File("src/main/resources/fonts/arial.fnt"));
		GUIText text = new GUIText("hello world",1, arial, new Vector2f(10, 0), 1f, true);
		text.setColour(1, 0, 1);
		int colors = loader.loadTexture("colors");

		Terrain terrain = new Terrain(0, 0,0, loader);

		ModelData treeData = OBJLoader.loadOBJ("tree");
		RawModel treeRaw = loader.loadToModel(treeData.getVertices(), treeData.getUVS(), treeData.getNormals(), treeData.getIndices());
		Material treeMaterial = new Material(colors);
		treeMaterial.setShineDamper(5);
		treeMaterial.setReflectivity(0.1f);
		TexturedModel treeModel = new TexturedModel(treeRaw, treeMaterial);
		List<ModelTransform> trees = new ArrayList<>();
		for (int i = 0; i < Terrain.SIZE / 2; i++)
		{
			Vector3f treePos = new Vector3f(rand.nextInt(Terrain.SIZE), -1f, rand.nextInt(Terrain.SIZE));
			treePos.y = terrain.getHeight(treePos.x, treePos.z) - 1;
			Transform treeTransform = new Transform(treePos, 0, rand.nextInt(360), 0, rand.nextInt(5) + 5);
			trees.add(new ModelTransform(treeTransform, treeModel));
		}

		ModelData playerData = OBJLoader.loadOBJ("person");
		RawModel playerRaw = loader.loadToModel(playerData.getVertices(), playerData.getUVS(), playerData.getNormals(), playerData.getIndices());
		TexturedModel playerModel = new TexturedModel(playerRaw, new Material(colors));
		Player player = new Player(playerModel, new Vector3f(0, 0, 0), 0, 180, 0, 0.75f);

		List<Light> lights = new ArrayList<>();

		float sunBrightness = 0.8f;
		Light sun = new Light(new Vector3f(50000, 100000, 100000), new Vector3f(sunBrightness, sunBrightness, sunBrightness));
		lights.add(sun);
		lights.add(new Light(new Vector3f(0, 35, 0), new Vector3f(1, 1, 1), new Vector3f(1f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(-300, 35, -300), new Vector3f(1, 1, 1), new Vector3f(1f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(-300, 35, -450), new Vector3f(1, 1, 1), new Vector3f(1f, 0.01f, 0.002f)));

		Camera camera = new Camera(player);
		RenderManager renderer = new RenderManager(camera);

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix());

		while (!Display.isCloseRequested())
		{
			player.move(terrain);
			camera.move();

			picker.update();

			if (Mouse.isButtonDown(0))
			{
				int modifyX = (int)player.data.getTransform().getPosition().x;
				int modifyY = Integer.MAX_VALUE;
				int modifyZ = (int)player.data.getTransform().getPosition().z;

				for (int y = Terrain.SIZE - 2; y > 0; y--)
				{
					float density = terrain.getDensity(modifyX, y, modifyZ);
					if (density > 0.5f)
					{
						modifyY = y;
						break;
					}
				}

				if (modifyY < Float.MAX_VALUE)
				{
					System.out.println("dig at " + modifyY);

					int range = 5;

					for (int mx = - range; mx < + range; mx++)
					{
						for (int mz = - range; mz < + range; mz++)
						{
							float density = terrain.getDensity(modifyX + mx, modifyY, modifyZ + mz);

							float factor = Math.abs(mx) + Math.abs(mz);

							float remove = Math.max(0, 0.3f - (0.03f * factor));

							if (!terrain.setDensity(modifyX + mx, modifyY, modifyZ + mz, density - remove))
							{
								System.out.println("sadness");
							}
						}
					}

					terrain.regenerateTerrain(loader);
				}
			}

			renderer.processTerrain(terrain);

			renderer.processSimilarObjects(trees);

			renderer.processObject(player.data);

			renderer.renderShadowMap(sun);
			renderer.render(lights, camera);

			FontRenderManager.render();

			DisplayManager.updateDisplay();
		}

		renderer.cleanup();
		FontRenderManager.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
