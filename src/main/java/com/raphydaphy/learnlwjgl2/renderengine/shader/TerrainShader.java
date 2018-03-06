package main.java.com.raphydaphy.learnlwjgl2.renderengine.shader;

import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.render.Light;
import main.java.com.raphydaphy.learnlwjgl2.util.MathUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TerrainShader extends ShaderProgram
{
    private static final String name = "src/main/resources/shaders/terrain";

    private int transformLocation;
    private int projectionLocation;
    private int viewLocation;
    private int lightPositionLocation;
    private int lightColorLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int skyColorLocation;

    public TerrainShader()
    {
        super(name);
    }

    @Override
    protected void bindAttributes()
    {
        // The ID's here are the ID's of the vertex buffers within the vertex array
        // The number is whatever we tell it to be in Loader#loadToVAO
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "tex_coords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations()
    {
        transformLocation = super.getUniformLocation("transform");
        projectionLocation = super.getUniformLocation("projection");
        viewLocation = super.getUniformLocation("view");
        lightPositionLocation = super.getUniformLocation("light_position");
        lightColorLocation = super.getUniformLocation("light_color");
        shineDamperLocation = super.getUniformLocation("shine_damper");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        skyColorLocation = super.getUniformLocation("sky_color");
    }

    public void loadSkyColor(Vector3f skyColor)
    {
        super.uniformVector3(skyColorLocation, skyColor);
    }

    public void loadReflectionInfo(float damper, float reflectivity)
    {
        super.uniformFloat(shineDamperLocation, damper);
        super.uniformFloat(reflectivityLocation, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f transform)
    {
        super.uniformMatrix4(transformLocation, transform);
    }

    public void loadProjectionMatrix(Matrix4f projection)
    {
        super.uniformMatrix4(projectionLocation, projection);
    }

    public void loadViewMatrix(Camera camera)
    {
        super.uniformMatrix4(viewLocation, MathUtils.createViewMatrix(camera));
    }

    public void loadLight(Light light)
    {
        super.uniformVector3(lightPositionLocation, light.getPosition());
        super.uniformVector3(lightColorLocation, light.getColor());
    }
}
