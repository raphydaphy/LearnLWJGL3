package main.java.com.raphydaphy.learnlwjgl2.render;

import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;

public class ModelTransform
{
    private Transform transform;
    private TexturedModel model;

    public ModelTransform(Transform transform, TexturedModel model)
    {
        this.transform = transform;
        this.model = model;
    }

    public Transform getTransform()
    {
        return transform;
    }

    public TexturedModel getModel()
    {
        return model;
    }
}
