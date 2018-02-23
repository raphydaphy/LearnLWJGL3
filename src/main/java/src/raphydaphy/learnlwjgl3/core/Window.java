package main.java.src.raphydaphy.learnlwjgl3.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class Window
{
    private long window;
    private int width, height;
    private boolean fullscreen;

    public Window()
    {
        setSize(640, 480);
        setFullscreen(false);
    }

    public void createWindow(String title)
    {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, 1);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, 1);

        window = GLFW.glfwCreateWindow(getWidth(), getHeight(), title, fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
        if (window == 0)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        if (!fullscreen)
        {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                    window,
                    (vidMode.width() - getWidth()) / 2,
                    (vidMode.height() - getHeight()) / 2
            );

            GLFW.glfwShowWindow(window);
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
    }

    public long getID()
    {
        return window;
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public int getWidth()
    {
        return fullscreen ? GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).width() : this.width;
    }

    public int getHeight()
    {
        return fullscreen ? GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).height() : this.height;
    }

    public void setFullscreen(boolean fullscreen)
    {
        this.fullscreen = fullscreen;
    }

    public boolean isFullscreen()
    {
        return fullscreen;
    }
}
