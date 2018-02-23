package main.java.src.raphydaphy.learnlwjgl3;

import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

public class Main
{
    private static final int TARGET_FPS = 60;
    private static final int TARGET_TPS = 40;

    private Window window;
    private Model model;
    private Timer timer;
    private Shader shader;
    private Camera camera;
    private Texture missing;

    private Matrix4f scale;
    private Matrix4f target;

    public void run()
    {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        Callbacks.glfwFreeCallbacks(window.getID());
        GLFW.glfwDestroyWindow(window.getID());

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    private void init()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = new Window();
        window.createWindow("Learn LWJGL3");

        GLFW.glfwSetKeyCallback(window.getID(), (window, key, scancode, action, mods) ->
        {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });

        timer = new Timer();

        GL.createCapabilities();

        float[] vertices = new float[]{
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0,
                0.5f, -0.5f, 0,
                -0.5f, -0.5f, 0};

        float[] textureCoords = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1};

        // References to positions in vertices and texturecoords to eliminate duplication
        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0};


        model = new Model(vertices, textureCoords, indices);

        GL30.glBindVertexArray(model.getVAO());

        shader = new Shader("default");
        camera = new Camera(640, 480);
        missing = new Texture("src//main/resources/missing.png");

        GL30.glBindVertexArray(0);

        scale = new Matrix4f().translate(100, 0, 0).scale(128);
        target = new Matrix4f();

        camera.setPosition(-100, 0, 0);
    }

    private void loop()
    {
        float delta;
        float accumulator = 0f;
        float interval = 1f / TARGET_TPS;
        float alpha;

        while (!GLFW.glfwWindowShouldClose(window.getID()))
        {
            delta = timer.getDeltaTime();
            accumulator += delta;

            // process input here...

            while (accumulator >= interval)
            {
                update(1f / TARGET_TPS);
                timer.updateTPS();
                accumulator -= interval;
            }

            alpha = accumulator / interval;
            render(alpha);
            timer.updateFPS();

            timer.update();

            sync(TARGET_FPS);

        }
    }

    private void update(float delta)
    {
        camera.move(1, 0, 0);
    }

    private void render(float alpha)
    {
        target = scale;

        GLFW.glfwPollEvents();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", camera.getProjection().mul(target));

        missing.bind(0);
        model.render();

        GLFW.glfwSwapBuffers(window.getID());
    }

    private void sync(int fps)
    {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime)
        {
            Thread.yield();

            try
            {
                Thread.sleep(1);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            now = timer.getTime();
        }
    }

    public static void main(String[] args)
    {
        new Main().run();
    }

}