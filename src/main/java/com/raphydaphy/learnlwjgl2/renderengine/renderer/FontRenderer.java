package main.java.com.raphydaphy.learnlwjgl2.renderengine.renderer;


import main.java.com.raphydaphy.learnlwjgl2.font.FontType;
import main.java.com.raphydaphy.learnlwjgl2.font.GUIText;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.shader.FontShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class FontRenderer
{

	private FontShader shader;

	public FontRenderer()
	{
		shader = new FontShader();
	}

	public void cleanup()
	{
		shader.cleanup();
	}

	public void render(Map<FontType, List<GUIText>> texts)
	{
		prepare();
		for (FontType font : texts.keySet())
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());

			for (GUIText text : texts.get(font))
			{
				renderText(text);
			}
		}
		endRendering();
	}

	private void prepare()
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.bind();
	}

	private void renderText(GUIText text)
	{
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(2);
		shader.loadColor(text.getColour());
		shader.loadTranslation(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void endRendering()
	{
		shader.unbind();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
