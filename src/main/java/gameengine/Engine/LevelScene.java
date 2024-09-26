package gameengine.Engine; // Declare the package name for engine components

// LevelScene class extending Scene to represent a specific game level
public class LevelScene extends Scene {

    // Constructor for LevelScene
    public LevelScene() {
        // Check level scene correctly initialized, change screen to green
        System.out.println("Inside level scene"); // Log message for debugging
        Window.Get().g = 1; // Set the green color component of the window to 1 (fully green)
    }

    // Override the Update method to provide specific behavior for the level scene
    @Override
    public void Update(float dt) {
        // Update logic for the level scene goes here
        // Currently, this method is empty and can be implemented in the future
    }
}
