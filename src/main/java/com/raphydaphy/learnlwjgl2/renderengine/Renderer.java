package main.java.com.raphydaphy.learnlwjgl2.renderengine;

import main.java.com.raphydaphy.learnlwjgl2.render.ModelTransform;
import main.java.com.raphydaphy.learnlwjgl2.render.Transform;
import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shaders.StaticShader;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.textures.ModelTexture;
import main.java.com.raphydaphy.learnlwjgl2.util.MathUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class Renderer
{
    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projection;
    private StaticShader shader;

    public Renderer(StaticShader shader)
    {
        this.shader = shader;

        // This will prevent any triangles with normals that face away from the camera from being rendered
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        initProjection();
        shader.bind();
        shader.loadProjectionMatrix(projection);
        shader.unbind();
    }

    public void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(1, 0, 0, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Map<TexturedModel, List<ModelTransform>> objects)
    {
        for (TexturedModel model : objects.keySet())
        {
            prepareModel(model);
            List<ModelTransform> batch = objects.get(model);

            for (ModelTransform object : batch)
            {
                prepareInstance(object);
                // Draw the vertices bound in GL_ARRAY_BUFFER using indices from GL_ELEMENT_BUFFER
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            unbindModel();
        }
    }

    private void prepareModel(TexturedModel model)
    {
        // Get the raw model in order to get the vertex array ID and vertex count
        RawModel rawModel = model.getRawModel();

        // Bind the model's vertex array, along with all the pointer data stored in it
        GL30.glBindVertexArray(rawModel.getVAOID());

        // Enable the various vertex buffer arrays which we bound in Loader#storeDataInAttributeList
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        // Load the reflection information from the material to the shader
        ModelTexture texture = model.getTexture();
        shader.loadReflectionInfo(texture.getShineDamper(), texture.getReflectivity());

        // Bind the texture to the sampler with id #0
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }

    private void unbindModel()
    {
        // Unbind everything to prevent it being accidently modified
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(ModelTransform object)
    {
        // Generate a transformation matrix based on the transform position, rotation and scale
        Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(object.getTransform().getPosition(),
                object.getTransform().getRotX(), object.getTransform().getRotY(), object.getTransform().getRotZ(),
                object.getTransform().getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

    public void initProjection()
    {
        // The aspect ratio of the camera, based on width and height so that it can scale depending on screen res
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();

        float yScale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f)) * aspectRatio);
        float xScale = yScale / aspectRatio;

        // The total z length which the camera can see objects within
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        // Setup the matrix and use a formula to calculate a simple view frustum
        projection = new Matrix4f();
        projection.m00 = xScale;
        projection.m11 = yScale;
        projection.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projection.m23 = -1;
        projection.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projection.m33 = 0;
    }
}
