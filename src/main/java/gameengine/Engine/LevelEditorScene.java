package gameengine.Engine; // Declare the package name for engine components

import gameengine.Components.SpriteRenderer; // Import SpriteRenderer class for rendering sprites
import gameengine.Util.AssetPool; // Import AssetPool for managing game assets
import org.joml.Vector2f; // Import Vector2f for 2D vector operations
import org.joml.Vector4f; // Import Vector4f for 4D vector operations

// LevelEditorScene class extending Scene to represent a level editor interface
public class LevelEditorScene extends Scene {

    // Constructor for LevelEditorScene
    public LevelEditorScene() {
        // Initialization can be done here if needed
    }

    // Override Init method to set up the scene
    @Override
    public void Init() {
        // Initialize the camera position
        this.camera = new Camera(new Vector2f(-250.f, 0)); // Set the camera position

        // Create the first game object
        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100),
                new Vector2f(256, 256))); // Position (100,100), size (256,256)
        obj1.AddComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/supermario.png"))); // Add sprite renderer component with texture
        this.AddGameObjectToScene(obj1); // Add object to the scene

        // Create the second game object
        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100),
                new Vector2f(256, 256))); // Position (400,100), size (256,256)
        obj2.AddComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/gumba.png"))); // Add sprite renderer component with texture
        this.AddGameObjectToScene(obj2); // Add object to the scene

        LoadResources(); // Load additional resources required for the scene
    }

    // Method to load game resources (shaders, textures, etc.)
    private void LoadResources() {
        // Load shader from the asset pool
        AssetPool.getShader("assets/shaders/default.glsl"); // Load the default shader
    }

    // Override Update method to handle scene updates
    @Override
    public void Update(float DeltaTime) {
        // Optionally print FPS for debugging purposes
        // System.out.println("FPS: " + (1.0 / DeltaTime));

        // Update all game objects in the scene
        for (GameObject go : this.gameObjects) {
            go.Update(DeltaTime); // Call Update method on each GameObject
        }

        // Render the scene
        this.renderer.Render(); // Call the render method to draw all objects
    }
}
