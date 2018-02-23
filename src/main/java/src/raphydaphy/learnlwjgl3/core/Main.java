package main.java.src.raphydaphy.learnlwjgl3.core;

import main.java.src.raphydaphy.learnlwjgl3.world.Camera;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Model;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Shader;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Texture;
import main.java.src.raphydaphy.learnlwjgl3.world.Tile;
import main.java.src.raphydaphy.learnlwjgl3.world.WorldRenderer;
import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.util.Random;

public class Main
{
    private static final int TARGET_FPS = 60;
    private static final int TARGET_TPS = 40;

    private Window window;
    private Timer timer;
    private WorldRenderer renderer;

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
        window.createWindow("Automania");

        GLFW.glfwSetKeyCallback(window.getID(), (window, key, scancode, action, mods) ->
        {
            if (action == GLFW.GLFW_RELEASE)
            {
                if (key == GLFW.GLFW_KEY_ESCAPE)
                {
                    GLFW.glfwSetWindowShouldClose(window, true);
                }
            }
            if (key == GLFW.GLFW_KEY_P)
            {
                Random rand = new Random();

                renderer.getChunk().setTile(rand.nextInt(32), rand.nextInt(32), Tile.GRASS);
            }
        });

        timer = new Timer();

        GL.createCapabilities();

        renderer = new WorldRenderer(window);

        renderer.init();
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
        //camera.move(1, 0, 0);
    }

    private void render(float alpha)
    {
        GLFW.glfwPollEvents();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        renderer.render();

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