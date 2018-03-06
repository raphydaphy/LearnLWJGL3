package main.java.com.raphydaphy.learnlwjgl2.models;

import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.Material;

public class TexturedModel
{
    private RawModel rawModel;
    private Material texture;

    public TexturedModel(RawModel rawModel, Material texture)
    {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel()
    {
        return rawModel;
    }

    public Material getTexture()
    {
        return texture;
    }
}
