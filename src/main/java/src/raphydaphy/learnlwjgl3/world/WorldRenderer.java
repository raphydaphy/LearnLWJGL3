package main.java.src.raphydaphy.learnlwjgl3.world;

import main.java.src.raphydaphy.learnlwjgl3.core.Window;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Model;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Shader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import javax.swing.text.html.parser.Entity;

public class WorldRenderer
{
    public static Model TILE_MODEL;
    public static Model PLAYER_MODEL;

    private int scale;

    private Window window;
    private Shader shader;

    private Chunk chunk;
    private Player player;

    public WorldRenderer(Window window)
    {
        scale = 64;
        this.window = window;
    }

    public void init()
    {
        TILE_MODEL = new Model(new float[]{-0.5f, 0.5f, 0, 0.5f, 0.5f, 0, 0.5f, -0.5f, 0, -0.5f, -0.5f, 0},
                new float[]{0, 0, 1, 0, 1, 1, 0, 1}, new int[]{0, 1, 2, 2, 3, 0});

        float[] playerModelVertices = new float[]{
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0,
                0.5f, -0.5f, 0,
                -0.5f, -0.5f, 0,
                -0.5f, 1.5f, 0,
                0.5f, 1.5f, 0};

        float[] playerModelTextureCoords = new float[]{
                0, 0.5f,
                1, 0.5f,
                1, 1,
                0, 1,
                0, 0,
                1, 0};

        int[] playerModelIndices = new int[]{
                0, 1, 2,
                2, 3, 0,
                0, 1, 4,
                4, 1, 5};

        PLAYER_MODEL = new Model(playerModelVertices, playerModelTextureCoords, playerModelIndices);

        Tile.init();
        player = new Player(window.getWidth(), window.getHeight());

        GL30.glBindVertexArray(TILE_MODEL.getVAO());
        chunk = new Chunk(0, 0);
        shader = new Shader("default");
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

                    Matrix4f target = new Matrix4f().scale(scale).translate(x + chunk.chunkX - (Chunk.CHUNK_SIZE / 2), y + chunk.chunkY - (Chunk.CHUNK_SIZE / 2), 0);

                    shader.setUniform("projection", player.getProjection().mul(target));

                    tile.getTex().bind(0);
                    TILE_MODEL.render();
                }
            }
        }

        GL30.glBindVertexArray(PLAYER_MODEL.getVAO());

        shader.setUniform("projection", player.getOrigin().scale(scale));

        player.getTexture().bind(0);
        PLAYER_MODEL.render();

        GL30.glBindVertexArray(0);

    }

    public Chunk getChunk()
    {
        return chunk;
    }

    public int getScale()
    {
        return scale;
    }

    public Player getPlayer()
    {
        return player;
    }
}
