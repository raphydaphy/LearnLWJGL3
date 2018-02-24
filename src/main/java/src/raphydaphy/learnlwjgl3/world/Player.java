package main.java.src.raphydaphy.learnlwjgl3.world;

import main.java.src.raphydaphy.learnlwjgl3.graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Player
{
    private final Texture tex;

    public int motionX;
    public int motionY;

    private Vector3f position;
    private Matrix4f projection;

    private Chunk chunk;

    public Player(int width, int height, Chunk chunk)
    {
        this.chunk = chunk;

        position = new Vector3f(0, 0, 0);
        setProjection(width, height);

        motionX = 0;
        motionY = 0;

        this.tex = new Texture("src//main/resources/player.png");
    }

    public Texture getTexture()
    {
        return tex;
    }

    public void setPosition(float x, float y, float z)
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setPosition(Vector2f pos)
    {
        this.position.x = pos.x;
        this.position.y = pos.y;
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

    public void setProjection(int width, int height)
    {
        projection = new Matrix4f().ortho2D(-width / 2, width / 2, -height / 2, height / 2);
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public Matrix4f getProjection()
    {
        return projection.translate(position, new Matrix4f());
    }

    public Matrix4f getOrigin()
    {
        return new Matrix4f(projection);
    }

    public BoundBox getAABB()
    {
        return new BoundBox(new Vector2f(position.x, position.y), new Vector2f(1, 2));
    }
}
