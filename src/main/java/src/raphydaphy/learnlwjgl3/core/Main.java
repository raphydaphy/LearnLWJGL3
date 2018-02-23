package main.java.src.raphydaphy.learnlwjgl3.core;

import main.java.src.raphydaphy.learnlwjgl3.world.*;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Model;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Shader;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Texture;
import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.DoubleBuffer;
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

        GLFW.glfwSetMouseButtonCallback(window.getID(), (window, button, action, mods) ->
        {
            if (action == GLFW.GLFW_RELEASE)
            {
                if (button < Tile.TILES.size())
                {
                    DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
                    DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
                    GLFW.glfwGetCursorPos(window, posX, posY);

                    int mouseX = (int) posX.get();
                    int mouseY = (int) posY.get();

                    int worldX = Math.round((mouseX - (this.window.getWidth() / 2) - renderer.getPlayer().getPosition().x) / renderer.getScale()) + (Chunk.CHUNK_SIZE / 2);
                    int worldY = Math.abs((Math.round((mouseY - (this.window.getHeight() / 2) + renderer.getPlayer().getPosition().y) / renderer.getScale()) + (Chunk.CHUNK_SIZE / 2)) - Chunk.CHUNK_SIZE);

                    System.out.println("Click at [" + mouseX + "," + mouseY + "] at world pos " + worldX + "," + worldY + "]");

                    if (renderer.getChunk().validatePos(worldX, worldY, false))
                    {
                        renderer.getChunk().setTile(worldX, worldY, Tile.TILES.get(button));
                    }
                }
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

        GL11.glClearColor(104 / 256f, 109 / 256f, 224 / 256f, 1);

        while (!GLFW.glfwWindowShouldClose(window.getID()))
        {
            delta = timer.getDeltaTime();
            accumulator += delta;

            while (accumulator >= interval)
            {
                input(1f / TARGET_TPS);
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

    private void input(float delta)
    {
        if (GLFW.glfwGetKey(window.getID(), GLFW.GLFW_KEY_A) == 1)
        {
            renderer.getPlayer().motionX += 5;
        }
        if (GLFW.glfwGetKey(window.getID(), GLFW.GLFW_KEY_D) == 1)
        {
            renderer.getPlayer().motionX -= 5;
        }
        if (GLFW.glfwGetKey(window.getID(), GLFW.GLFW_KEY_SPACE) == 1)
        {
            renderer.getPlayer().motionY -= 15;
        }
    }

    private void update(float delta)
    {
        Player player = renderer.getPlayer();

        player.move(player.motionX, player.motionY, 0);

        player.motionX = 0;
        player.motionY = 0;
    }

    private void render(float alpha)
    {
        GLFW.glfwPollEvents();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

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