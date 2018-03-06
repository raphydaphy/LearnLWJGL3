package main.java.com.raphydaphy.learnlwjgl2.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.security.Key;

public class Camera
{
    private static final float speed = 0.5f;

    private Vector3f position = new Vector3f(0, 5, 0);
    private float pitch, yaw, roll;

    public void move()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            position.x -= speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            position.x += speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            position.z -= speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            position.z += speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            position.y -= speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            position.y += speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
        {
            yaw -= speed * 2;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E))
        {
            yaw += speed * 2;
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
