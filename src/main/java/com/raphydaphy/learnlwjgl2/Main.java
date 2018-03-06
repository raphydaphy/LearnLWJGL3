package main.java.com.raphydaphy.learnlwjgl2;

import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.render.Light;
import main.java.com.raphydaphy.learnlwjgl2.render.ModelTransform;
import main.java.com.raphydaphy.learnlwjgl2.render.Transform;
import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.*;
import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shaders.StaticShader;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.textures.ModelTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Main
{
    public static void main(String[] args)
    {
        DisplayManager.createDisplay("LearnLWJGL3");

        Loader loader = new Loader();

        RawModel model = OBJLoader.loadOBJ("stall", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("stall"));
        Transform stallTransform = new Transform(new Vector3f(0, 0, -50), 0, 0, 0, 1);
        TexturedModel stallModel = new TexturedModel(model, texture);
        ModelTransform stall = new ModelTransform(stallTransform, stallModel);

        texture.setShineDamper(10);
        texture.setReflectivity(1);


        Light sun = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

        Camera camera = new Camera();
        RenderManager renderer = new RenderManager();

        while (!Display.isCloseRequested())
        {
            stall.getTransform().rotate(0, 0.5f, 0);
            camera.move();

            renderer.processObject(stall);
            renderer.render(sun, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }
}
