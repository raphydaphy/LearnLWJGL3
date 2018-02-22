package main.java.src.raphydaphy.learnlwjgl3;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    public static final String MISSING_TEXTURE = "src//main/resources/missing.png";

    private int id;
    private int width;
    private int height;

    public Texture(String file)
    {
        BufferedImage bi = null;

        try
        {
            bi = ImageIO.read(new File(file));

        } catch (IOException e1)
        {
            try
            {
                bi = ImageIO.read(new File(MISSING_TEXTURE));
                System.err.println("Could not load texture with resource location: " + file);
            }
            catch (IOException e2)
            {
                System.err.println("Missing texture could not be resolved at location: " + MISSING_TEXTURE);
                System.exit(1);
            }
        }

        width = bi.getWidth();
        height = bi.getHeight();

        int[] pixels = new int[width * height];
        pixels = bi.getRGB(0, 0, width, height, null, 0, width);

        ByteBuffer tex = BufferUtils.createByteBuffer(width * height * 4);

        System.out.println(width + " : " + height);
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                int pixel = pixels[i * width + j];

                tex.put((byte) ((pixel >> 16) & 0xFF)); //RED
                tex.put((byte) ((pixel >> 8) & 0xFF));  //GREEN
                tex.put((byte) ((pixel) & 0xFF));       //BLUE
                tex.put((byte) ((pixel >> 24) & 0xFF)); //ALPHA

            }
        }

        tex.flip();

        id = glGenTextures();

        glEnable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, tex);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, id);
    }
}
