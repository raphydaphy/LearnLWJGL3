package main.java.src.raphydaphy.learnlwjgl3.world;

public class Chunk
{
    public static final int CHUNK_SIZE = 32;

    private int[][] tiles;

    public final int chunkX, chunkY;

    public Chunk(int x, int y)
    {
        this.chunkX = x;
        this.chunkY = y;

        tiles = new int[CHUNK_SIZE][CHUNK_SIZE];

        for (int cx = 0; cx < CHUNK_SIZE; cx++)
        {
            for (int cy = 0; cy < CHUNK_SIZE; cy++)
            {
                tiles[cx][cy] = Tile.AIR.id;
            }
        }
    }

    public Tile getTile(int innerX, int innerY)
    {
        validatePos(innerX, innerY);
        return Tile.TILES.get(tiles[innerX][innerY]);
    }

    public boolean isAir(int innerX, int innerY)
    {
        validatePos(innerX, innerY);
        return tiles[innerX][innerY] == Tile.AIR.id;
    }

    public void setTile(int innerX, int innerY, Tile tile)
    {
        validatePos(innerX, innerY);
        tiles[innerX][innerY] = tile.id;
    }

    public void validatePos(int innerX, int innerY)
    {
        if (innerX < 0 || innerY < 0 || innerX >= CHUNK_SIZE || innerY >= CHUNK_SIZE)
        {
            System.err.println("Tried to access tile at invalid chunk coordinate [" + innerX + ", " + innerY + "]");
            System.exit(1);
        }
    }
}
