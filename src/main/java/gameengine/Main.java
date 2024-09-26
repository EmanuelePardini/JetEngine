package gameengine; // Declare the package name for the game engine

import gameengine.Engine.Window; // Import the Window class from the Engine package

// TODO: Use Hungarian notation - This is a reminder to adopt a naming convention where variable names indicate their type and purpose

public class Main {
    // The main method is the entry point of the Java application
    public static void main(String[] args) {
        // Declare a variable 'window' of type Window
        Window window;

        // Initialize the 'window' variable by calling the static 'Get' method from the Window class.
        // This likely implements the Singleton pattern, ensuring that only one instance of the Window class exists.
        window = Window.Get();

        // Call the 'Run' method on the 'window' object to start the game loop or window operations.
        // This method probably handles events, rendering, and other game-related tasks.
        window.Run();
    }
}
