package main.java.com.raphydaphy.learnlwjgl2.renderengine;

import main.java.com.raphydaphy.learnlwjgl2.models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader
{
    public static RawModel loadOBJ(String file, Loader loader)
    {
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] vertexArray = null;
        float[] textureArray = null;
        float[] normalArray = null;
        int[] indexArray = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/main/resources/models/" + file + ".obj"))))
        {
            String line;

            while (true)
            {
                line = reader.readLine();
                String[] curLine = line.split(" ");

                if (line.startsWith("v "))
                {
                    vertices.add(new Vector3f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3])));
                } else if (line.startsWith("vt "))
                {
                    textures.add(new Vector2f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2])));
                } else if (line.startsWith("vn "))
                {
                    normals.add(new Vector3f(Float.parseFloat(curLine[1]), Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3])));
                } else if (line.startsWith("f "))
                {
                    textureArray = new float[vertices.size() * 2];
                    normalArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null)
            {
                if (!line.startsWith("f "))
                {
                    line = reader.readLine();
                    continue;
                }

                String[] curLine = line.split(" ");

                processVertex(curLine[1].split("/"), indices, textures, normals, textureArray, normalArray);
                processVertex(curLine[2].split("/"), indices, textures, normals, textureArray, normalArray);
                processVertex(curLine[3].split("/"), indices, textures, normals, textureArray, normalArray);

                line = reader.readLine();
            }

        } catch (Exception e)
        {
            System.err.println("Could not read model " + file);
            e.printStackTrace();
        }

        vertexArray = new float[vertices.size() * 3];
        indexArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices)
        {
            vertexArray[vertexPointer++] = vertex.x;
            vertexArray[vertexPointer++] = vertex.y;
            vertexArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++)
        {
            indexArray[i] = indices.get(i);
        }

        return loader.loadToVAO(vertexArray, textureArray, normalArray, indexArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> texCoords, List<Vector3f> normals, float[] texArray, float[] normalArray)
    {
        int vertexPointer = Integer.parseInt(vertexData[0]) - 1;

        indices.add(vertexPointer);

        Vector2f currentTex = texCoords.get(Integer.parseInt(vertexData[1]) - 1);
        texArray[vertexPointer * 2] = currentTex.x;
        texArray[vertexPointer * 2 + 1] = 1 - currentTex.y;

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalArray[vertexPointer * 3] = currentNormal.x;
        normalArray[vertexPointer * 3 + 1] = currentNormal.y;
        normalArray[vertexPointer * 3 + 2] = currentNormal.z

        ;
    }
}
