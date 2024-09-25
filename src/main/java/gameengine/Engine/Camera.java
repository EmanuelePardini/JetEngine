package gameengine.Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position)
    {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }
    //When the windows change it will adjust ortographic projection
    public void adjustProjection()
    {
        projectionMatrix.identity();
        /*
        That gives us an identity matrix:
            if you multiply this by it you will obtain itself
         */
        projectionMatrix.ortho(0.0f, //left side of the screen(start)
                    32.0f * 40.0f, //right side of the screen
                     0.0f,
                32.0f * 21.0f,
                0.0f, //Max near, i want to see things unless they are this near
                100.0f); //I want to see things unless they are this far
        //OpenGL function to define ortographic view parameters
    }

    public Matrix4f GetViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), //Where is the camera located in world space
                                                    cameraFront.add(position.x, position.y, 0.0f), //Where is the center of the camera front
                                                    cameraUp);
        return  viewMatrix;
    }

    public Matrix4f GetProjectionMatrix() { return this.projectionMatrix; }
}
