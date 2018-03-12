package main.java.com.raphydaphy.learnlwjgl2.renderengine.renderer;

import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.Material;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.TerrainShader;
import main.java.com.raphydaphy.learnlwjgl2.terrain.Terrain;
import main.java.com.raphydaphy.learnlwjgl2.util.MathUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class TerrainRenderer
{
    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projection)
    {
        this.shader = shader;

        shader.bind();
        shader.loadProjectionMatrix(projection);
        shader.unbind();
    }

    public void render(List<Terrain> terrains)
    {
        for (Terrain terrain : terrains)
        {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);

            // Draw the vertices bound in GL_ARRAY_BUFFER using indices from GL_ELEMENT_BUFFER
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            unbindModel();
        }
    }

    private void prepareTerrain(Terrain terrain)
    {
        // Get the raw model in order to get the vertex array ID and vertex count
        RawModel rawModel = terrain.getMesh();

        // Bind the model's vertex array, along with all the pointer data stored in it
        GL30.glBindVertexArray(rawModel.getVAOID());

        // Enable the various vertex buffer arrays which we bound in Loader#storeDataInAttributeList
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        // Load the reflection information from the material to the shader
        Material texture = terrain.getTexture();
        shader.loadReflectionInfo(texture.getShineDamper(), texture.getReflectivity());

        // Bind the texture to the sampler with id #0
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
    }

    private void unbindModel()
    {
        // Unbind everything to prevent it being accidently modified
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain)
    {
        // Generate a transformation matrix based on the transform position, rotation and scale
        Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
