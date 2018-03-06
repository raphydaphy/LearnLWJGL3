package main.java.com.raphydaphy.learnlwjgl2.render;

import org.lwjgl.util.vector.Vector3f;

public class Light
{
    private Vector3f position;

    public Light(Vector3f position, Vector3f color)
    {
        this.position = position;
        this.color = color;
    }

    private Vector3f color;

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Vector3f getColor()
    {
        return color;
    }

    public void setColor(Vector3f color)
    {
        this.color = color;
    }
}
