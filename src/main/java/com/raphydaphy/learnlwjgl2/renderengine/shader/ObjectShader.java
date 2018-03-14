package main.java.com.raphydaphy.learnlwjgl2.renderengine.shader;

import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import main.java.com.raphydaphy.learnlwjgl2.render.Light;
import main.java.com.raphydaphy.learnlwjgl2.util.MathUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class ObjectShader extends WorldShader
{
	private static final String name = "src/main/resources/shaders/object";
	private int artificialLightingLocation;

	public ObjectShader()
	{
		super(name);
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttributes();
		super.bindAttribute(2, "tex_coords");
	}

	@Override
	protected void getAllUniformLocations()
	{
		super.getAllUniformLocations();
		artificialLightingLocation = super.getUniformLocation("artificial_lighting");
	}

	public void setArtificialLighting(boolean useArtificialLighting)
	{
		super.uniformInt(artificialLightingLocation, useArtificialLighting == true ? 1 : 0);
	}
}
