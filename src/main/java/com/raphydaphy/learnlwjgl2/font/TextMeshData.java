package main.java.com.raphydaphy.learnlwjgl2.font;

/**
 * Stores the vertex data for all the quads on which a font will be rendered.
 *
 * @author Karl
 */
public class TextMeshData
{

	private float[] vertexPositions;
	private float[] textureCoords;

	protected TextMeshData(float[] vertexPositions, float[] textureCoords)
	{
		this.vertexPositions = vertexPositions;
		this.textureCoords = textureCoords;
	}

	public float[] getVertexPositions()
	{
		return vertexPositions;
	}

	public float[] getTextureCoords()
	{
		return textureCoords;
	}

	public int getVertexCount()
	{
		return vertexPositions.length / 2;
	}

}