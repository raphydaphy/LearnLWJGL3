package main.java.com.raphydaphy.learnlwjgl2.renderengine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager
{
    private static int WIDTH = 1080 * 4;
    private static int HEIGHT = 720 * 4;

    private static int FPS_CAP = 60;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay(String title)
    {
        ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);

        try
        {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle(title);
        } catch (LWJGLException e)
        {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void updateDisplay()
    {
        Display.sync(FPS_CAP);
        Display.update();
        long currentTime = getCurrentTime();
        delta = (currentTime - lastFrameTime) / 1000f;
        lastFrameTime = currentTime;
    }

    public static void closeDisplay()
    {
        Display.destroy();
    }

    public static float getFrameTimeSeconds()
    {
        return delta;
    }

    private static long getCurrentTime()
    {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
