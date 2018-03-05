package main.java.com.raphydaphy.learnlwjgl2.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{
    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch, yaw, roll;


    public Camera()
    {

    }

    public void move()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            position.x -= 0.05f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            position.x += 0.05f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            position.z += 0.05f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            position.z -= 0.05f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
        {
            yaw -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E))
        {
            yaw += 0.5f;
        }
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getYaw()
    {
        return yaw;
    }

    public float getRoll()
    {
        return roll;
    }
}
