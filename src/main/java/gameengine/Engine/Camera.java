package gameengine.Engine; // Declare the package for the camera component

import org.joml.Matrix4f; // Import Matrix4f for matrix operations
import org.joml.Vector2f; // Import Vector2f for 2D vector operations
import org.joml.Vector3f; // Import Vector3f for 3D vector operations

// Camera class for managing the view and projection of the scene
public class Camera {
    private Matrix4f projectionMatrix; // Matrix for projection transformations
    private Matrix4f viewMatrix; // Matrix for view transformations
    public Vector2f position; // Position of the camera in 2D space

    // Constructor for Camera, initializes position and projection/view matrices
    public Camera(Vector2f position) {
        this.position = position; // Set the initial position of the camera
        this.projectionMatrix = new Matrix4f(); // Create a new projection matrix
        this.viewMatrix = new Matrix4f(); // Create a new view matrix
        adjustProjection(); // Call method to set up the projection matrix
    }

    // Adjusts the orthographic projection based on the window size
    public void adjustProjection() {
        projectionMatrix.identity(); // Reset the projection matrix to the identity matrix
        /*
        Identity matrix means:
            if you multiply this by another matrix, it remains unchanged.
        */
        projectionMatrix.ortho(0.0f, // Left side of the screen (start)
                32.0f * 40.0f, // Right side of the screen (width)
                0.0f, // Bottom side of the screen (start)
                32.0f * 21.0f, // Top side of the screen (height)
                0.0f, // Near clipping plane (minimum visibility)
                100.0f); // Far clipping plane (maximum visibility)
        // The ortho function defines the parameters for the orthographic view
    }

    // Returns the view matrix, used to transform coordinates from world space to view space
    public Matrix4f GetViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f); // Direction the camera is facing
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f); // Up direction of the camera
        this.viewMatrix.identity(); // Reset the view matrix to the identity matrix
        // Set up the view matrix with camera position and direction
        viewMatrix = viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f), // Camera position in world space
                cameraFront.add(position.x, position.y, 0.0f), // Target point (center of the camera's view)
                cameraUp // Up direction of the camera
        );
        return viewMatrix; // Return the calculated view matrix
    }

    // Returns the projection matrix
    public Matrix4f GetProjectionMatrix() {
        return this.projectionMatrix; // Return the projection matrix
    }
}
