package gameengine.Engine; // Declare the package name for the engine components

import gameengine.Util.Time; // Import the Time utility class for handling time
import org.lwjgl.Version; // Import the LWJGL version information
import org.lwjgl.glfw.GLFWErrorCallback; // Import the GLFW error callback class
import org.lwjgl.opengl.GL; // Import OpenGL related methods

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks; // Import utility for freeing GLFW callbacks
import static org.lwjgl.glfw.GLFW.*; // Import GLFW constants and functions
import static org.lwjgl.opengl.GL11C.*; // Import OpenGL constants and functions
import static org.lwjgl.system.MemoryUtil.NULL; // Import NULL constant for GLFW window creation

public class Window {

    // Resolution and information about the window
    int width; // Window width
    int height; // Window height
    String title; // Title of the window
    private long glfwWindow; // Handle for the window in memory space (GLFW window reference)

    // RGBA color values for the window background
    public float r; // Red component
    public float g; // Green component
    public float b; // Blue component
    public float a; // Alpha (transparency) component

    // Singleton instance of the Window class
    private static Window window = null;

    // Current active scene in the window
    private static Scene currentScene = null;

    // Private constructor for the Singleton pattern
    private Window() {
        this.width = 1920; // Default width
        this.height = 1080; // Default height
        this.title = "JetEngine"; // Default title
        this.r = 0; // Default red color
        this.g = 0; // Default green color
        this.b = 0; // Default blue color
        this.a = 0; // Default alpha
    }

    // Singleton accessor method to get the instance of the Window
    public static Window Get() {
        if (Window.window == null) Window.window = new Window(); // Create a new instance if it doesn't exist
        return Window.window; // Return the singleton instance
    }

    // Method to get the current scene
    public static Scene GetScene() {
        return Get().currentScene; // Returns the current active scene
    }

    // Changes the current scene dynamically
    public static void ChangeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene(); // Create a new LevelEditorScene
                currentScene.Init(); // Initialize the scene
                currentScene.Start(); // Start the scene
                break;
            case 1:
                currentScene = new LevelScene(); // Create a new LevelScene
                currentScene.Init(); // Initialize the scene
                currentScene.Start(); // Start the scene
                break;
            default:
                assert false : "Unknown scene'" + newScene + "'"; // Assertion for unknown scenes
        }
    }

    // Main method to run the window and the game loop
    public void Run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!"); // Print the LWJGL version

        Init(); // Initialize the window and OpenGL
        Loop(); // Start the game loop

        // Free GLFW callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free(); // May cause a nullptr exception if not set
    }

    // Initialize the window and OpenGL context
    public void Init() {
        // Setup error callback to print errors to the standard error stream
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW"); // Error handling
        }

        // Configure GLFW window hints
        glfwDefaultWindowHints(); // Set default window hints
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Window starts as hidden
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Window is resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // Window starts maximized

        // Create the window with specified width, height, and title
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window"); // Error handling
        }

        // Bind mouse event listeners
        glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePosCallback); // Set mouse position callback
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback); // Set mouse button callback
        glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrollCallback); // Set mouse scroll callback

        // Bind keyboard event listener
        glfwSetKeyCallback(glfwWindow, KeyListener::KeyCallback); // Set key callback

        // Make the OpenGL context current for this window
        glfwMakeContextCurrent(glfwWindow);
        // Enable vertical synchronization (v-sync), which controls the frame rate
        glfwSwapInterval(1);
        // Make the window visible after creation
        glfwShowWindow(glfwWindow);

        /* This line is critical for LWJGL interoperation with GLFW.
        It creates the GLCapabilities instance and makes the OpenGL
        bindings available for use. */
        GL.createCapabilities();

        // Change the initial scene
        Window.ChangeScene(0);
    }

    // Main game loop
    public void Loop() {
        // Initialize frame time variables
        float beginFrameTime = Time.getTime(); // Start time of the frame
        float endFrameTime; // End time of the frame

        float dt = -1.0f; // Delta time (time difference between frames)

        // Main loop: runs until the window is closed
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll for input events
            glfwPollEvents();

            // Set the clear color for the color buffer
            glClearColor(r, g, b, a); // Set the background color
            glClear(GL_COLOR_BUFFER_BIT); // Clear the color buffer

            if (dt >= 0) currentScene.Update(dt); // Update the current scene if dt is valid

            // Swap the front and back buffers (render the current frame)
            glfwSwapBuffers(glfwWindow);

            // Calculate delta time and update time variables
            endFrameTime = Time.getTime(); // Get the end time
            dt = endFrameTime - beginFrameTime; // Calculate delta time
            beginFrameTime = endFrameTime; // Update start time for next frame
        }
    }
}
