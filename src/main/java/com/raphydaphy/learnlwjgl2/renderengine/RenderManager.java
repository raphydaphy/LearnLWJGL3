package main.java.com.raphydaphy.learnlwjgl2.renderengine;

import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;
import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.render.Light;
import main.java.com.raphydaphy.learnlwjgl2.render.ModelTransform;
import main.java.com.raphydaphy.learnlwjgl2.render.Transform;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderManager
{
    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<ModelTransform>> objects = new HashMap<>();

    public void render(Light light, Camera camera)
    {
        renderer.prepare();
        shader.bind();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);

        renderer.render(objects);

        shader.unbind();

        objects.clear();
    }

    public void processObject(ModelTransform object)
    {
        TexturedModel model = object.getModel();
        List<ModelTransform> batch = objects.get(model);
        if (batch != null)
        {
            batch.add(object);
        }
        else
        {
            batch = new ArrayList<>();
            batch.add(object);
            objects.put(model, batch);
        }
    }

    public void cleanup()
    {
        shader.cleanup();
    }
}
