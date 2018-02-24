package main.java.src.raphydaphy.learnlwjgl3.world;

import org.joml.Vector2f;

public class BoundBox
{
    private Vector2f center, half_extent;

    public BoundBox(Vector2f center, Vector2f half_extent)
    {
        this.center = center;
        this.half_extent = half_extent;
    }

    public Collision getCollision(BoundBox box2)
    {
        Vector2f distance = box2.center.sub(center, new Vector2f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);

        distance.sub(half_extent.add(box2.half_extent, new Vector2f()));

        return new Collision(distance, distance.x < 0 && distance.y < 0);
    }

    public Collision getCollision(Vector2f point)
    {
        Vector2f distance = point.sub(center);
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);

        distance.sub(half_extent);

        return new Collision(distance, distance.x < 0 && distance.y < 0);
    }

    public void correctPosition(BoundBox box2, Collision data)
    {
        Vector2f correctionDistance = box2.center.sub(center, new Vector2f());
        if (data.distance.x > data.distance.y)
        {
            if (correctionDistance.x > 0)
            {
                center.add(data.distance.x, 0);
            } else
            {
                center.add(-data.distance.x, 0);
            }
        } else
        {
            if (correctionDistance.y > 0)
            {
                center.add(0, data.distance.y);
            } else
            {
                center.add(0, -data.distance.y);
            }
        }
    }

    public Vector2f getCenter()
    {
        return center;
    }

    public Vector2f getHalfExtent()
    {
        return half_extent;
    }


    public class Collision
    {
        public Vector2f distance;
        public boolean isIntersecting;

        public Collision(Vector2f distance, boolean intersects)
        {
            this.distance = distance;
            this.isIntersecting = intersects;
        }
    }
}