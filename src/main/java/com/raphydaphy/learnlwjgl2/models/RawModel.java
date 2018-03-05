package main.java.com.raphydaphy.learnlwjgl2.models;

public class RawModel
{
    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVAOID()
    {
        return vaoID;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }
}
