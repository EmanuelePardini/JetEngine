package gameengine.Engine; // Declare the package name for engine components

import Renderer.Renderer; // Import the Renderer class for rendering game objects

import java.util.ArrayList; // Import ArrayList for dynamic array management
import java.util.List; // Import List interface

// Abstract class representing a game scene
public abstract class Scene {
    protected Renderer renderer = new Renderer(); // Renderer instance for drawing game objects
    protected Camera camera; // Camera instance for viewing the scene
    private boolean isRunning = false; // Flag to check if the scene is running
    protected List<GameObject> gameObjects = new ArrayList<>(); // List of game objects in the scene

    // Constructor for the Scene class
    public Scene() {
        // Initialization can be done here if needed
    }

    // Method to initialize the scene (to be overridden in subclasses)
    public void Init() {
        // Can be used for any setup required before the scene starts
    }

    // Method to start the scene and initialize game objects
    public void Start() {
        // Loop through all game objects and start them
        for (GameObject go : gameObjects) {
            go.Start(); // Call the Start method on each GameObject
            this.renderer.Add(go); // Add the GameObject to the renderer
        }
        isRunning = true; // Set the scene as running
    }

    // Method to add a game object to the scene
    public void AddGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go); // Add game object if the scene is not running
        } else {
            gameObjects.add(go); // Add game object to the list
            go.Start(); // Start the game object immediately
            this.renderer.Add(go); // Add the game object to the renderer
        }
    }

    // Abstract method for updating the scene (to be implemented in subclasses)
    public abstract void Update(float DeltaTime);

    // Getter method for the camera
    public Camera camera() {
        return this.camera; // Return the camera instance
    }
}
