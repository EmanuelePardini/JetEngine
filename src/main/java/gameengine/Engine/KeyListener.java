package gameengine.Engine; // Declare the package name for engine components

import static org.lwjgl.glfw.GLFW.GLFW_PRESS; // Import GLFW_PRESS constant for key press
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE; // Import GLFW_RELEASE constant for key release

public class KeyListener {
    // Singleton instance of KeyListener
    private static KeyListener instance;

    // Array to hold the state of each key (up to 350 keys)
    private boolean keyPressed[] = new boolean[350]; // Boolean array to track pressed keys

    // Private constructor for Singleton pattern
    private KeyListener() {
        // Initialization can be done here if needed
    }

    // Singleton accessor method to get the instance of KeyListener
    public static KeyListener get() {
        if (KeyListener.instance == null) KeyListener.instance = new KeyListener(); // Create instance if it doesn't exist
        return KeyListener.instance; // Return the singleton instance
    }

    // Callback method for keyboard events (key press/release)
    public static void KeyCallback(long window, int key, int scancode, int action, int mods) {
        // Check if key was pressed
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true; // Mark the key as pressed
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false; // Mark the key as released
        }
    }

    // Method to check if a specific key is currently pressed
    public static boolean IsKeyPressed(int keyCode) {
        return get().keyPressed[keyCode]; // Return the pressed state of the key
    }
}
