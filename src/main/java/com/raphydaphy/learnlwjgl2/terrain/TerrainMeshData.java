package main.java.com.raphydaphy.learnlwjgl2.terrain;

import main.java.com.raphydaphy.learnlwjgl2.renderengine.load.Loader;
import org.lwjgl.opengl.GL30;

public class TerrainMeshData
{
	public final float[] positions;
	public final float[] normals;
	public final float[] colors;
	public final int[] indices;

	public TerrainMeshData(float[] positions, float[] normals, float[] colors, int[] indices)
	{
		this.positions = positions;
		this.normals = normals;
		this.colors = colors;
		this.indices = indices;
	}

	public TerrainMesh generateMesh(Loader loader)
	{
		return new TerrainMesh(positions, normals, colors, indices, loader);
	}
}
