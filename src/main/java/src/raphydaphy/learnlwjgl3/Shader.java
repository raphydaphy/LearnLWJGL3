package main.java.src.raphydaphy.learnlwjgl3;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    private int program;
    private int vertexID;
    private int fragID;

    public Shader(String filename)
    {
        program = glCreateProgram();

        vertexID = makeShader(GL_VERTEX_SHADER, filename + ".vert");
        fragID = makeShader(GL_FRAGMENT_SHADER, filename + ".frag");

        glAttachShader(program, vertexID);
        glAttachShader(program, fragID);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) != 1)
        {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);

        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1)
        {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

	public void setUniform(String name, int value)
	{
		int location = glGetUniformLocation(program, name);
		if (location != -1)
		{
			glGetUniformi(location, value);
		}
	}

    public void setUniform(String name, Matrix4f matrix)
    {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		matrix.get(buf);
        if (location != -1)
        {
            glUniformMatrix4fv(location, false, buf);
        }
    }

    public void bind()
    {
        glUseProgram(program);
    }

    private int makeShader(int type, String filename)
    {
        int id = glCreateShader(type);
        glShaderSource(id, readFile(filename));
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) != 1)
        {
            System.err.println("Error compiling shader " + filename + ":\n" + glGetShaderInfoLog(id));
            System.exit(1);
        }

        return id;
    }

    private String readFile(String filename)
    {
        StringBuilder src = new StringBuilder();
        BufferedReader reader;

        try
        {
            reader = new BufferedReader(new FileReader(new File("src//main/resources/shaders/" + filename)));
            String line;

            while ((line = reader.readLine()) != null)
            {
                src.append(line);
                src.append("\n");
            }

            reader.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return src.toString();
    }
}
