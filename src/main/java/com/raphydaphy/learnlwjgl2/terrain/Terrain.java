package main.java.com.raphydaphy.learnlwjgl2.terrain;

import main.java.com.raphydaphy.learnlwjgl2.Main;
import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.load.Loader;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.Material;
import main.java.com.raphydaphy.learnlwjgl2.util.MathUtils;
import main.java.com.raphydaphy.learnlwjgl2.util.NoiseMapGenerator;
import main.java.com.raphydaphy.learnlwjgl2.util.OpenSimplexNoise;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Terrain
{
	private static final int SIZE = 128;
	private static final long SEED = 0;
	private static final int MAX_VERTS_PER_MESH = 30000;
	private static final int MAX_HEIGHT = 30;
	private static final int VERTEX_COUNT = 128;

	private final float x;
	private final float y;
	private final float z;

	private List<RawModel> meshes;

	private float[][] heights;
	private OpenSimplexNoise noise;

	public Terrain(int gridX, int gridY, int gridZ, Loader loader)
	{
		noise = new OpenSimplexNoise(SEED);

		this.x = gridX * SIZE;
		this.y = gridY * SIZE;
		this.z = gridZ * SIZE;

		meshes = generateMesh(loader);
	}

	public float getX()
	{
		return x;
	}

	public float getZ()
	{
		return z;
	}

	public List<RawModel> getMeshes()
	{
		return meshes;
	}

	private float getDensity(int x, int y, int z, int octaves, float scale, float persistance, float lacunarity, Vector3f[] octaveOffsets)
	{
		float density = -y / 2f + 6f;
		float halfSize = SIZE / 2f;
		float amplitude = 1f;
		float frequency = 1f;

		for (int octave = 0; octave < octaves; octave++)
		{
			float sampleX = (x - halfSize + octaveOffsets[octave].x) / scale * frequency;
			float sampleY = (y - halfSize + octaveOffsets[octave].y) / scale * frequency;
			float sampleZ = (z - halfSize + octaveOffsets[octave].z) / scale * frequency;

			float noiseValue = (float)noise.eval(sampleX, sampleY, sampleZ) * 2 - 1;

			density += noiseValue * amplitude;

			amplitude *= persistance;
			frequency *= lacunarity;
		}

		return density * halfSize;
	}

	private Vector3f[] generateOctaveOffsets(int octaves, float persistance, Vector3f offset)
	{
		Random rand = new Random(SEED);
		Vector3f[] octaveOffsets = new Vector3f[octaves];

		float maxHeight = 0;
		float amplitude = 1;

		for (int octave = 0; octave < octaves; octave++)
		{
			float offsetX = rand.nextInt(200000) - 100000 + offset.x;
			float offsetY = rand.nextInt(200000) - 100000 - offset.y;
			float offsetZ = rand.nextInt(200000) - 100000 + offset.z;

			octaveOffsets[octave] = new Vector3f(offsetX, offsetY, offsetZ);

			maxHeight += amplitude;
			amplitude *= persistance;
		}

		return octaveOffsets;
	}

	private List<RawModel> generateMesh(Loader loader)
	{
		Vector3f offset = new Vector3f(x,y,z);
		float[] voxels = new float[SIZE * SIZE * SIZE];
		final int octaves = 8;
		final float persistance = 0.6f;

		Vector3f[] octaveOffsets = generateOctaveOffsets(octaves, persistance, offset);

		for (int x = 0; x < SIZE; x++)
		{
			for (int y = 0; y < SIZE; y++)
			{
				for (int z = 0; z < SIZE; z++)
				{
					float density = getDensity(x, y, z, octaves,50, persistance,2.01f, octaveOffsets);

					voxels[x + y * SIZE + z * SIZE * SIZE] = density;
				}
			}
		}

		List<Vector3f> vertices = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		MarchingCubesGenerator generator = new MarchingCubesGenerator();

		generator.generateMesh(voxels, SIZE, SIZE, SIZE, vertices, normals, indices);

		int numMeshes = vertices.size() / MAX_VERTS_PER_MESH + 1;

		List<RawModel> models = new ArrayList<>();

		for (int mesh = 0; mesh < numMeshes; mesh++)
		{
			List<Vector3f> splitVertices = new ArrayList<>();
			List<Vector3f> splitNormals = new ArrayList<>();
			List<Integer> splitIndices = new ArrayList<>();

			for (int vertex = 0; vertex < MAX_VERTS_PER_MESH; vertex++)
			{
				int index = mesh * MAX_VERTS_PER_MESH + vertex;

				if (index < vertices.size())
				{
					splitVertices.add(vertices.get(index));
					splitNormals.add(normals.get(index));
					splitIndices.add(indices.get(index));
				}
			}

			if (splitVertices.size() == 0)
			{
				continue;
			}

			float[] splitVerticesArray = new float[splitVertices.size() * 3];
			float[] splitNormalsArray = new float[splitNormals.size() * 3];
			int[] splitIndicesArray = new int[splitIndices.size()];

			for (int index = 0; index < splitIndices.size(); index++)
			{
				splitVerticesArray[index * 3] = splitVertices.get(index).x;
				splitVerticesArray[index * 3 + 1] = splitVertices.get(index).y;
				splitVerticesArray[index * 3 + 2] = splitVertices.get(index).z;

				splitNormalsArray[index * 3] = splitNormals.get(index).x;
				splitNormalsArray[index * 3 + 1] = splitNormals.get(index).y;
				splitNormalsArray[index * 3 + 2] = splitNormals.get(index).z;

				splitIndicesArray[index] = indices.get(index);
			}

			models.add(loader.loadToModel(splitVerticesArray, null, splitNormalsArray, splitIndicesArray));
		}

		return models;
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
		if (true)
		{
			return 0;
		}
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