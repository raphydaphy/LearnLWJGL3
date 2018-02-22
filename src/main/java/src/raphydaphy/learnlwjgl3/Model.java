package main.java.src.raphydaphy.learnlwjgl3;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Model
{
    private int drawCount;


    private int vertexID;
    private int texCoordID;
    private int indiceID;

    public Model(float[] vertices, float[] texCoords, int[] indices)
    {
        drawCount = indices.length;

        vertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glBufferData(GL_ARRAY_BUFFER, getBuffer(vertices), GL_STATIC_DRAW);

        texCoordID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texCoordID);
        glBufferData(GL_ARRAY_BUFFER, getBuffer(texCoords), GL_STATIC_DRAW);

        IntBuffer buf = BufferUtils.createIntBuffer(indices.length);
        buf.put(indices);
        buf.flip();

        indiceID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buf, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render()
    {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vertexID);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, texCoordID);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
    }

    private FloatBuffer getBuffer(float[] array)
    {
        FloatBuffer buf = BufferUtils.createFloatBuffer(array.length);
        buf.put(array);
        buf.flip();

        return buf;
    }
}
