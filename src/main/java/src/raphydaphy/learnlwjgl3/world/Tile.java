package main.java.src.raphydaphy.learnlwjgl3.world;

import main.java.src.raphydaphy.learnlwjgl3.graphics.Texture;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.Map;

public class Tile
{
    public static final Map<Integer, Tile> TILES = new HashMap<>();
    public static int nextID = 0;

    public static Tile AIR;
    public static Tile GRASS;
    public static Tile DIRT;
    public static Tile HAPPY_SQUARE;

    private boolean fullTile;

    public final int id;
    private final String name;
    private final Texture tex;

    public Tile(String name)
    {
        this(name, true);
    }

    public Tile(String name, boolean hasTexture)
    {
        this.name = name;
        this.id = nextID++;

        if (hasTexture)
        {
            this.tex = new Texture("src//main/resources/" + name + ".png");
        } else
        {
            this.tex = null;
        }

        fullTile = true;

        this.register();
    }

    public void register()
    {
        TILES.put(id, this);
    }

    public Texture getTex()
    {
        return tex;
    }

    public String getName()
    {
        return name;
    }

    public boolean isFullTile()
    {
        return this.fullTile;
    }

    public Tile setFullTile(boolean isFull)
    {
        this.fullTile = isFull;
        return this;
    }

    public static void init()
    {
        GL30.glBindVertexArray(WorldRenderer.TILE_MODEL.getVAO());

        AIR = new Tile("air", false).setFullTile(false);
        GRASS = new Tile("grass");
        DIRT = new Tile("dirt");
        HAPPY_SQUARE = new Tile("happy_square");

        GL30.glBindVertexArray(0);
    }

    public static void cleanup()
    {
        for (Tile tile : TILES.values())
        {
            if (tile.tex != null)
            {
                tile.tex.delete();
            }
        }
    }


}
