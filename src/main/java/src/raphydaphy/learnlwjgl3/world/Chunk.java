package main.java.src.raphydaphy.learnlwjgl3.world;

public class Chunk
{
    public static final int CHUNK_SIZE = 128;

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
        validatePos(innerX, innerY, true);
        return Tile.TILES.get(tiles[innerX][innerY]);
    }

    public boolean isAir(int innerX, int innerY)
    {
        validatePos(innerX, innerY, true);
        return tiles[innerX][innerY] == Tile.AIR.id;
    }

    public void setTile(int innerX, int innerY, Tile tile)
    {
        validatePos(innerX, innerY, true);
        tiles[innerX][innerY] = tile.id;
    }

    public boolean validatePos(int innerX, int innerY, boolean crash)
    {
        if (innerX < 0 || innerY < 0 || innerX >= CHUNK_SIZE || innerY >= CHUNK_SIZE)
        {
            if (crash)
            {
                System.err.println("Tried to access tile at invalid chunk coordinate [" + innerX + ", " + innerY + "]");
                System.exit(1);
            }
            return false;
        }
        return true;
    }
}
