package main.java.src.raphydaphy.learnlwjgl3.graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class Shader
{
    private int program;
    private int vertexID;
    private int fragID;

    public Shader(String filename)
    {
        program = GL20.glCreateProgram();

        vertexID = makeShader(GL20.GL_VERTEX_SHADER, filename + ".vert");
        fragID = makeShader(GL20.GL_FRAGMENT_SHADER, filename + ".frag");

        GL20.glAttachShader(program, vertexID);
        GL20.glAttachShader(program, fragID);

        GL20.glBindAttribLocation(program, 0, "vertices");
        GL20.glBindAttribLocation(program, 1, "textures");

        GL20.glLinkProgram(program);

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) != 1)
        {
            System.err.println("Shader program failed to link:\n" + GL20.glGetProgramInfoLog(program));
            System.exit(1);
        }
        GL20.glValidateProgram(program);

        if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) != 1)
        {
            System.err.println("Shader Program failed to validate:\n" + GL20.glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    public void setUniform(String name, int value)
    {
        int location = GL20.glGetUniformLocation(program, name);
        if (location != -1)
        {
            GL20.glUniform1i(location, value);
        }
    }

    public void setUniform(String name, Matrix4f matrix)
    {
        int location = GL20.glGetUniformLocation(program, name);
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        matrix.get(buf);
        if (location != -1)
        {
            GL20.glUniformMatrix4fv(location, false, buf);
        }
    }

    public void bind()
    {
        GL20.glUseProgram(program);
    }

    private int makeShader(int type, String filename)
    {
        int id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, readFile(filename));
        GL20.glCompileShader(id);

        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != 1)
        {
            System.err.println("Error compiling shader " + filename + ":\n" + GL20.glGetShaderInfoLog(id));
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
