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

    public void render(List<Terrain> terrains, Matrix4f toShadowSpace)
    {
        shader.loadShadowMapSpaceMatrix(toShadowSpace);
        for (Terrain terrain : terrains)
        {
            loadModelMatrix(terrain);

            // Draw the vertices bound in GL_ARRAY_BUFFER using indices from GL_ELEMENT_BUFFER
            for (RawModel mesh : terrain.getMeshes())
            {
                prepareTerrainMesh(mesh);
                GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindModel();
        }
    }

    private void prepareTerrainMesh(RawModel terrainMesh)
    {
        // Bind the model's vertex array, along with all the pointer data stored in it
        GL30.glBindVertexArray(terrainMesh.getVAOID());

        // Enable the various vertex buffer arrays which we bound in Loader#storeDataInAttributeList
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
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
