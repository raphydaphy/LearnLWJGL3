package main.java.src.raphydaphy.learnlwjgl3.world;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera
{
    private Vector3f position;
    private Matrix4f projection;

    public Camera(int width, int height)
    {
        position = new Vector3f(0, 0, 0);
        projection = new Matrix4f().ortho2D(-width / 2, width / 2, -height / 2, height / 2);
    }

    public void setPosition(float x, float y, float z)
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setPosition(Vector3f pos)
    {
        this.position = pos;
    }

    public void move(float x, float y, float z)
    {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void move(Vector3f by)
    {
        this.position.add(by);
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public Matrix4f getProjection()
    {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(position);

        target = projection.mul(pos, target);

        return target;
    }
}
