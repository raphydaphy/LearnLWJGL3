package main.java.src.raphydaphy.learnlwjgl3;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;

public class Texture
{
	public static final String MISSING_TEXTURE = "src//main/resources/missing.png";

	private int id;
	private int width;
	private int height;

	public Texture(String file)
	{
		ByteBuffer image;
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			STBImage.stbi_set_flip_vertically_on_load(true);
			image = STBImage.stbi_load(file, w, h, comp, 4);
			if (image == null)
			{
				image = STBImage.stbi_load(MISSING_TEXTURE, w, h, comp, 4);
			}

			width = w.get();
			height = h.get();
		}

		id = glGenTextures();

		glEnable(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, id);

		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
	}

	public void bind(int sampler)
	{
		if (sampler >= 0 && sampler <= 31)
		{
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}
}
