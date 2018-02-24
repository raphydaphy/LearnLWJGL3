package main.java.src.raphydaphy.learnlwjgl3.world;

import main.java.src.raphydaphy.learnlwjgl3.core.Window;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Model;
import main.java.src.raphydaphy.learnlwjgl3.graphics.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

public class WorldRenderer
{
    public static Model TILE_MODEL;
    public static Model PLAYER_MODEL;

    public int scale;
    private int viewX;
    private int viewY;

    private Window window;
    private Shader shader;

    private Matrix4f world;

    private Chunk chunk;
    private Player player;

    public WorldRenderer(Window window)
    {
        scale = 64;
        this.window = window;
        calculateView(window);
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

        chunk = new Chunk(0, 0);
        player = new Player(window.getWidth(), window.getHeight(), chunk);

        GL30.glBindVertexArray(TILE_MODEL.getVAO());



        for (int x = 0; x < Chunk.CHUNK_SIZE; x++)
        {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++)
            {
                chunk.setTile(x, y, Tile.HAPPY_SQUARE);
            }
        }
        shader = new Shader("default");
        GL30.glBindVertexArray(0);

        this.world = new Matrix4f().setTranslation(new Vector3f(0));
        this.world.scale(scale);
    }

    public void render()
    {
        GL30.glBindVertexArray(TILE_MODEL.getVAO());

        int posX = (int) player.getPosition().x / (scale);
        int posY = (int) player.getPosition().y / (scale);

        for (int i = 0; i < viewX; i++)
        {
            for (int j = 0; j < viewY; j++)
            {
                if (chunk.validatePos(i - posX - (viewX / 2) + 1, j + posY - (viewY / 2), false))
                {
                    Tile t = chunk.getTile(i - posX - (viewX / 2) + 1, j + posY - (viewY / 2));
                    if (t != null) renderTile(t, i - posX - (viewX / 2) + 1, -j - posY + (viewY / 2));
                }
            }
        }


        GL30.glBindVertexArray(PLAYER_MODEL.getVAO());

        shader.setUniform("projection", player.getOrigin().scale(scale));

        player.getTexture().bind(0);
        PLAYER_MODEL.render();

        GL30.glBindVertexArray(0);

    }

    public void renderTile(Tile tile, int x, int y)
    {
        shader.bind();

        if (tile.getTex() != null)
        {
            tile.getTex().bind(0);

            Matrix4f tile_pos = new Matrix4f().translate(x, y, 0);
            Matrix4f target = new Matrix4f();

            player.getProjection().mul(world, target);
            target.mul(tile_pos);

            shader.setUniform("sampler", 0);
            shader.setUniform("projection", target);

            TILE_MODEL.render();
        }
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

    public void cleanup()
    {
        TILE_MODEL.delete();
        PLAYER_MODEL.delete();

        shader.delete();
    }

    public void calculateView(Window window)
    {
        viewX = (window.getWidth() / scale) + 4;
        viewY = (window.getHeight() / scale) + 4;
    }
}
