package main.java.src.raphydaphy.learnlwjgl3.world;

import main.java.src.raphydaphy.learnlwjgl3.core.Window;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Model;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Shader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

public class WorldRenderer
{
    public static Model TILE_MODEL;

    private Window window;
    private Shader shader;
    private Camera camera;

    private Chunk chunk;

    public WorldRenderer(Window window)
    {

        this.window = window;
    }

    public void init()
    {
        float[] vertices = new float[]{
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0,
                0.5f, -0.5f, 0,
                -0.5f, -0.5f, 0};

        float[] textureCoords = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1};

        // References to positions in vertices and texturecoords to eliminate duplication
        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0};

        TILE_MODEL = new Model(vertices, textureCoords, indices);

        Tile.init();

        GL30.glBindVertexArray(TILE_MODEL.getVAO());
        chunk = new Chunk(0, 0);
        shader = new Shader("default");
        camera = new Camera(window.getWidth(), window.getHeight());
        GL30.glBindVertexArray(0);
    }

    public void render()
    {
        GL30.glBindVertexArray(TILE_MODEL.getVAO());

        shader.bind();
        shader.setUniform("sampler", 0);

        for (int x = 0; x < Chunk.CHUNK_SIZE; x++)
        {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++)
            {
                if (!chunk.isAir(x, y))
                {
                    Tile tile = chunk.getTile(x, y);

                    Matrix4f target = new Matrix4f().scale(16).translate(x + chunk.chunkX - 16, y + chunk.chunkY - 16, 0);

                    shader.setUniform("projection", camera.getProjection().mul(target));

                    tile.getTex().bind(0);
                    TILE_MODEL.render();
                }
            }
        }
        GL30.glBindVertexArray(0);
    }

    public Chunk getChunk()
    {
        return chunk;
    }
}
