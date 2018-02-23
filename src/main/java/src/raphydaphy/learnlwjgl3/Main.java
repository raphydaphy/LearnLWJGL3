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

    private long window;

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

        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

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

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, 1);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, 1);

        window = GLFW.glfwCreateWindow(640, 480, "Hello World!", 0, 0);
        if (window == 0)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        GLFW.glfwMakeContextCurrent(window);

        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);

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

        while (!GLFW.glfwWindowShouldClose(window))
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

            System.out.println("FPS:" + timer.getFPS() + ", TPS: " + timer.getTPS());

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

        GLFW.glfwSwapBuffers(window);
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