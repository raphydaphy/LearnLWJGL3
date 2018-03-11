package main.java.com.raphydaphy.learnlwjgl2.renderengine.renderer;

import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;
import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.render.Light;
import main.java.com.raphydaphy.learnlwjgl2.render.ModelTransform;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.ObjectShader;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.TerrainShader;
import main.java.com.raphydaphy.learnlwjgl2.terrain.Terrain;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

public class RenderManager
{
    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private static final Vector3f SKY = new Vector3f(0.5f, 0.5f, 0.5f);

    private ObjectShader objectShader;
    private ObjectRenderer objectRenderer;

    private TerrainShader terrainShader;
    private TerrainRenderer terrainRenderer;

    private Matrix4f projection;
    private Map<TexturedModel, List<ModelTransform>> objects = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public RenderManager()
    {
        enableCulling();
        initProjection();

        objectShader = new ObjectShader();
        objectRenderer = new ObjectRenderer(objectShader, projection);

        terrainShader = new TerrainShader();
        terrainRenderer = new TerrainRenderer(terrainShader, projection);
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

    private void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(SKY.x, SKY.y, SKY.z, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(List<Light> lights, Camera camera)
    {
        prepare();

        objectShader.bind();

        objectShader.loadSkyColor(SKY);
        objectShader.loadLights(lights);
        objectShader.loadViewMatrix(camera);

        objectRenderer.render(objects);

        objectShader.unbind();

        terrainShader.bind();

        terrainShader.loadSkyColor(SKY);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);

        terrainRenderer.render(terrains);

        terrainShader.unbind();

        objects.clear();
        terrains.clear();
    }

    // Adds all the models to the batch, but things will break if the list contains multiple different models!
    public void processSimilarObjects(List<ModelTransform> list)
    {
        TexturedModel model = list.get(0).getModel();
        List<ModelTransform> batch = objects.get(model);
        if (batch != null)
        {
            batch.addAll(list);
        }
        else
        {
            batch = new ArrayList<>();
            batch.addAll(list);
            objects.put(model, batch);
        }
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

    public void processTerrain(Terrain terrain)
    {
        terrains.add(terrain);
    }

    public void cleanup()
    {
        objectShader.cleanup();
        terrainShader.cleanup();
    }

    public static void enableCulling()
    {
        // This will prevent any triangles with normals that face away from the camera from being rendered
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling()
    {
        // This is useful when rendering transparent models as part of them will not render if culling is enabled
        GL11.glDisable(GL11.GL_CULL_FACE);
    }


}
