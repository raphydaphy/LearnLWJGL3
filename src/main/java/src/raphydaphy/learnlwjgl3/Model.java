package main.java.src.raphydaphy.learnlwjgl3;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model
{
	private int drawCount;
	private int vao;

	private int vertexID;
	private int texCoordID;
	private int indiceID;

	public Model(float[] vertices, float[] texCoords, int[] indices)
	{
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		drawCount = indices.length;

		vertexID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, getBuffer(vertices), GL15.GL_STATIC_DRAW);

		texCoordID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texCoordID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, getBuffer(texCoords), GL15.GL_STATIC_DRAW);

		IntBuffer buf = BufferUtils.createIntBuffer(indices.length);
		buf.put(indices);
		buf.flip();

		indiceID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indiceID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	public void render()
	{
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexID);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texCoordID);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indiceID);
		GL11.glDrawElements(GL11.GL_TRIANGLES, drawCount, GL11.GL_UNSIGNED_INT, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	private FloatBuffer getBuffer(float[] array)
	{
		FloatBuffer buf = BufferUtils.createFloatBuffer(array.length);
		buf.put(array);
		buf.flip();

		return buf;
	}
}
