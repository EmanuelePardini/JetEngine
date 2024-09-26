package gameengine.Engine; // Declare the package name for the engine components

import static org.lwjgl.glfw.GLFW.GLFW_PRESS; // Import GLFW_PRESS constant for mouse button press
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE; // Import GLFW_RELEASE constant for mouse button release

public class MouseListener {
    // Singleton instance of MouseListener
    private static MouseListener instance;

    // Variables to hold scroll values
    private double scrollX; // Horizontal scroll value
    private double scrollY; // Vertical scroll value

    // Variables to track mouse position
    private double xPos; // Current X position of the mouse
    private double yPos; // Current Y position of the mouse
    private double lastY; // Last Y position of the mouse
    private double lastX; // Last X position of the mouse

    // Array to track which mouse buttons are pressed (up to 3 buttons)
    private boolean mouseButtonPressed[] = new boolean[3]; // Boolean array for button states
    private boolean isDragging; // Flag to indicate if the mouse is being dragged

    // Private constructor for Singleton pattern
    private MouseListener() {
        // Initialize scroll and position values
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    // Singleton accessor method to get the instance of MouseListener
    public static MouseListener get() {
        if (instance == null) MouseListener.instance = new MouseListener(); // Create instance if it doesn't exist
        return MouseListener.instance; // Return the singleton instance
    }

    // Callback method for mouse position changes
    public static void MousePosCallback(long window, double xpos, double ypos) {
        // Save last X and Y position
        get().lastX = get().xPos; // Update last X position
        get().lastY = get().yPos; // Update last Y position

        // Set new X and Y positions
        get().xPos = xpos; // Update current X position
        get().yPos = ypos; // Update current Y position

        // Determine if dragging is happening (if any button is pressed)
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    // Callback method for mouse button actions (press/release)
    public static void MouseButtonCallback(long window, int button, int action, int mods) {
        // Check if mouse button was pressed
        if (action == GLFW_PRESS) {
            // Check if the button index is valid
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true; // Mark button as pressed
            }
        } else if (action == GLFW_RELEASE) {
            // Check if the button index is valid
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false; // Mark button as released
                get().isDragging = false; // Reset dragging flag
            }
        }
    }

    // Callback method for mouse scroll events
    public static void MouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset; // Set horizontal scroll offset
        get().scrollY = yOffset; // Set vertical scroll offset
    }

    // Method to reset mouse state at the end of the frame
    public static void EndFrame() {
        get().scrollX = 0; // Reset horizontal scroll
        get().scrollY = 0; // Reset vertical scroll
        get().lastX = get().xPos; // Update last X to current
        get().lastY = get().yPos; // Update last Y to current
    }

    // GETTERS Region
    // <editor-fold>
    // Method to get current X position of the mouse
    public static float GetX() {
        return (float) get().xPos; // Return current X position as float
    }

    // Method to get current Y position of the mouse
    public static float GetY() {
        return (float) get().yPos; // Return current Y position as float
    }

    // Method to get the change in X position since the last frame
    public static float GetDx() {
        return (float) (get().lastX - get().xPos); // Return change in X position
    }

    // Method to get the change in Y position since the last frame
    public static float GetDy() {
        return (float) (get().lastY - get().yPos); // Return change in Y position
    }

    // Method to get the horizontal scroll amount
    public static float GetScrollX() {
        return (float) get().scrollX; // Return horizontal scroll amount
    }

    // Method to get the vertical scroll amount
    public static float GetScrollY() {
        return (float) get().scrollY; // Return vertical scroll amount
    }

    // Method to check if the mouse is currently being dragged
    public static boolean IsDragging() {
        return get().isDragging; // Return dragging state
    }

    // Method to check if a specific mouse button is down
    public static boolean MouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button]; // Return button pressed state
        } else {
            return false; // Return false if button index is invalid
        }
    }
    // </editor-fold>
}
