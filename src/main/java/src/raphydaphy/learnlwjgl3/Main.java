package main.java.src.raphydaphy.learnlwjgl3;

import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

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

		Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

		GLFW. glfwTerminate();
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

        window = GLFW.glfwCreateWindow(300, 300, "Hello World!", 0, 0);
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

        Matrix4f projection = new Matrix4f().ortho2D(-640f/2, 640f/2, 480f/2, -480f/2);
        Matrix4f scale = new Matrix4f().scale(128);

        Matrix4f target = new Matrix4f();

        projection.mul(scale, target);

        while (!GLFW.glfwWindowShouldClose(window))
        {
            boolean held = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_F) == 1;

			GLFW.glfwPollEvents();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", target);

            missing.bind(0);
            model.render();

			GLFW.glfwSwapBuffers(window);

        }
    }

    public static void main(String[] args)
    {
        new Main().run();
    }

}