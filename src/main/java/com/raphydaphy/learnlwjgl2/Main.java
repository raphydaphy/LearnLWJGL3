package main.java.com.raphydaphy.learnlwjgl2;

import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.render.Transform;
import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.DisplayManager;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.Loader;
import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.Renderer;
import main.java.com.raphydaphy.learnlwjgl2.shaders.StaticShader;
import main.java.com.raphydaphy.learnlwjgl2.textures.ModelTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Main
{
    public static void main(String[] args)
    {
        DisplayManager.createDisplay("LearnLWJGL3");

        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        float[] vertices = {
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,

                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,

                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f

        };

        float[] texCoords = {

                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0


        };

        int[] indices = {
                0, 1, 3,
                3, 1, 2,
                4, 5, 7,
                7, 5, 6,
                8, 9, 11,
                11, 9, 10,
                12, 13, 15,
                15, 13, 14,
                16, 17, 19,
                19, 17, 18,
                20, 21, 23,
                23, 21, 22

        };

        RawModel model = loader.loadToVAO(vertices, texCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("hi"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Transform transform = new Transform(new Vector3f(0, 0, -1), 0, 0, 0, 1);

        Camera camera = new Camera();

        while (!Display.isCloseRequested())
        {
            transform.rotate(1, 1, 0);
            camera.move();
            renderer.prepare();
            shader.bind();
            shader.loadViewMatrix(camera);
            renderer.render(texturedModel, transform, shader);
            shader.unbind();
            DisplayManager.updateDisplay();
        }

        shader.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }
}
