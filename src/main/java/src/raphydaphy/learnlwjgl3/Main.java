package main.java.src.raphydaphy.learnlwjgl3;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main
{

    private long window;
    private Texture missing;
    private Shader shader;

    public void run()
    {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        //glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    private void loop()
    {
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


        Model model = new Model(vertices, textureCoords, indices);

        shader = new Shader("default");
        missing = new Texture("src//main/resources/missing.png");

        Matrix4f projection = new Matrix4f().ortho2D(-640f/2, 640f/2, -480f/2, 480f/2);
        Matrix4f scale = new Matrix4f().scale(128);

        Matrix4f target = new Matrix4f();

        projection.mul(scale, target);

        target.translate(10, 10, 10);

        while (!glfwWindowShouldClose(window))
        {
            boolean held = glfwGetKey(window, GLFW_KEY_F) == 1;

            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.bind();
            shader.setUniform("sampler", 0);
           // shader.setUniform("projection", target);

            missing.bind(0);
            model.render();


            glfwSwapBuffers(window);


        }
    }

    public static void main(String[] args)
    {
        new Main().run();
    }

}