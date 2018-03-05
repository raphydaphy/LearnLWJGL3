package main.java.com.raphydaphy.learnlwjgl2.util;

import main.java.com.raphydaphy.learnlwjgl2.render.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MathUtils
{
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rotX, float rotY, float rotZ, float scale)
    {
        // Make a new matrix to store the transformation
        Matrix4f matrix = new Matrix4f();

        // Identity is essentially a empty matrix
        matrix.setIdentity();

        // Apply transformation based on the position we want to use to the matrix
        Matrix4f.translate(translation, matrix, matrix);

        // Rotate around each of the x, y and z axes
        Matrix4f.rotate((float) Math.toRadians(rotX), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotY), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotZ), new Vector3f(0, 0, 1), matrix, matrix);

        // Scale the matrix on all axes by the factor provided
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), matrix, matrix);

        Matrix4f.translate(new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z), matrix, matrix);

        return matrix;
    }
}
