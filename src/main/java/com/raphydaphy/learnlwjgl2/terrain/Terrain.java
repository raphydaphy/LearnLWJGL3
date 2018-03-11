package main.java.com.raphydaphy.learnlwjgl2.terrain;

import main.java.com.raphydaphy.learnlwjgl2.Main;
import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.load.Loader;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.Material;
import main.java.com.raphydaphy.learnlwjgl2.util.MathUtils;
import main.java.com.raphydaphy.learnlwjgl2.util.NoiseMapGenerator;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Terrain
{
	private static final float SIZE = 800;
	private static final int MAX_HEIGHT = 30;
	private static final int VERTEX_COUNT = 128;

	private final float x;
	private final float z;

	private RawModel mesh;
	private Material texture;

	private float[][] heights;

	public Terrain(int gridX, int gridZ, Loader loader, Material texture)
	{
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texture = texture;

		mesh = generateMesh(loader);
	}

	public float getX()
	{
		return x;
	}

	public float getZ()
	{
		return z;
	}

	public RawModel getMesh()
	{
		return mesh;
	}

	public Material getTexture()
	{
		return texture;
	}

	private RawModel generateMesh(Loader loader)
	{
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] uvs = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;

		heights = new float[VERTEX_COUNT][VERTEX_COUNT];

		float[][] noiseMap = NoiseMapGenerator.generateNoiseMap(VERTEX_COUNT, 10, 8, 0.5f, 2, new Vector2f(0, 0));
		for (int i = 0; i < VERTEX_COUNT; i++)
		{
			for (int j = 0; j < VERTEX_COUNT; j++)
			{

				float height = noiseMap[i][j] * MAX_HEIGHT;
				heights[j][i] = height;
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;

				Vector3f normal = calculateNormal(i, j, noiseMap);

				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				uvs[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				uvs[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int indexPointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++)
		{
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++)
			{
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[indexPointer++] = topLeft;
				indices[indexPointer++] = bottomLeft;
				indices[indexPointer++] = topRight;
				indices[indexPointer++] = topRight;
				indices[indexPointer++] = bottomLeft;
				indices[indexPointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, uvs, normals, indices);
	}

	private Vector3f calculateNormal(int x, int z, float[][] noiseMap)
	{
		if (x > 0 && z < 0 && x < VERTEX_COUNT - 1 && z < VERTEX_COUNT - 1)
		{
			float heightL = noiseMap[x - 1][z];
			float heightR = noiseMap[x + 1][z];
			float heightD = noiseMap[x][z - 1];
			float heightU = noiseMap[x][z + 1];

			Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
			normal.normalise();

			return normal;
		}

		return new Vector3f(0, 1, 0);
	}

	public float getHeight(float worldX, float worldZ)
	{
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);

		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX < 0 || gridZ < 0 || gridX >= heights.length - 1 || gridZ >= heights.length - 1)
		{
			return 0;
		}

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

		float height;

		if (xCoord <= (1 - zCoord))
		{
			height = MathUtils.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else

		{
			height = MathUtils.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}

		return height;
	}
}
