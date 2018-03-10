package main.java.com.raphydaphy.learnlwjgl2.util;

import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

public class NoiseMapGenerator
{
	public static final int SEED = 0;
	public static final OpenSimplexNoise NOISE = new OpenSimplexNoise(SEED);

	public static float[][] generateNoiseMap(int size, float scale, int octaves, float persistance, float lacunarity, Vector2f offset)
	{
		if (scale <= 0)
		{
			scale = 0.0001f;
		}

		Random rand = new Random(SEED);
		Vector2f[] octaveOffsets = new Vector2f[octaves];

		float maxHeight = 0f;
		float amplitude = 1f;
		float frequency = 1f;

		for (int octave = 0; octave < octaves; octave++)
		{
			float offX = rand.nextInt(200000) - 100000 + offset.x;
			float offY = rand.nextInt(200000) - 100000 - offset.y;

			octaveOffsets[octave] = new Vector2f(offX, offY);

			maxHeight += amplitude;
			amplitude *= persistance;
		}

		float[][] noiseMap = new float[size][size];

		float halfSize = size / 2f;

		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				amplitude = 1f;
				frequency = 1f;
				float noiseHeight = 0;

				for (int octave = 0; octave < octaves; octave++)
				{
					float sampleX = (x - halfSize + octaveOffsets[octave].x) / scale * frequency;
					float sampleY = (y - halfSize + octaveOffsets[octave].y) / scale * frequency;
					float noiseValue = (float)NOISE.eval(sampleX, sampleY);

					noiseHeight += noiseValue * amplitude;

					amplitude *= persistance;
					frequency *= lacunarity;
				}

				noiseMap[x][y] = noiseHeight;
			}
		}

		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				float normalizedHeight = (noiseMap[x][y] + 1) / maxHeight;
				noiseMap[x][y] = (float)MathUtils.clamp(normalizedHeight, 0, Integer.MAX_VALUE);
			}
		}

		return noiseMap;
	}
}
