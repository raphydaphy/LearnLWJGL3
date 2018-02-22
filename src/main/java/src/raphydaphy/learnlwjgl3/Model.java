package main.java.src.raphydaphy.learnlwjgl3;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Model
{
    private int drawCount;
    private int vertexID;

    private int texCoordID;

    public Model(float[] vertices, float[] texCoords)
    {
        drawCount = vertices.length / 1;

        vertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glBufferData(GL_ARRAY_BUFFER, getBuffer(vertices), GL_STATIC_DRAW);

        texCoordID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texCoordID);
        glBufferData(GL_ARRAY_BUFFER, getBuffer(texCoords), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render()
    {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glBindBuffer(GL_ARRAY_BUFFER, vertexID);

        // size = the amount of dimensions
        glVertexPointer(2, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, texCoordID);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, drawCount);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    private FloatBuffer getBuffer(float[] array)
    {
        FloatBuffer buf = BufferUtils.createFloatBuffer(array.length);
        buf.put(array);
        buf.flip();

        return buf;
    }
}
