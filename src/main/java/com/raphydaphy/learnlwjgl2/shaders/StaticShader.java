package main.java.com.raphydaphy.learnlwjgl2.shaders;

import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.util.MathUtils;
import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram
{
    private static final String name = "src/main/resources/shaders/static";

    private int transformLocation;
    private int projectionLocation;
    private int viewLocation;

    public StaticShader()
    {
        super(name);
    }

    @Override
    protected void bindAttributes()
    {
        // The ID's here are the ID's of the vertex buffers within the vertex array
        // Since we bound the position first, and tex_coords second, they are numbered 0 and 1
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "tex_coords");
    }

    @Override
    protected void getAllUniformLocations()
    {
        transformLocation = super.getUniformLocation("transform");
        projectionLocation = super.getUniformLocation("projection");
        viewLocation = super.getUniformLocation("view");
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
}
