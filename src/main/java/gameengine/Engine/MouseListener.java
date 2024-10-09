package gameengine.Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener
{
    private static MouseListener instance;
    private double scrollX;
    private double scrollY;

    private double xPos;
    private double yPos;
    private double lastY;
    private double lastX;

    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean isDragging;

    private Vector2f viewportPos = new Vector2f();
    private Vector2f viewportSize = new Vector2f();

    private int screenWidth;
    private int screenHeight;

    private MouseListener()
    {
        this.scrollX = 0;
        this.scrollY = 0;

        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    //Singleton
    public static MouseListener get()
    {
        if (instance == null) MouseListener.instance = new MouseListener();

        return MouseListener.instance;
    }

    public static void MousePosCallback(long window, double xpos, double ypos)
    {
        //save last x and y pos
        get().lastX = get().xPos;
        get().lastY = get().yPos;

        //set new x and y
        get().xPos = xpos;
        get().yPos = ypos;

        //If mouse is moved while any button is pressed, dragging is true (user is dragging)
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void MouseButtonCallback(long window, int button, int action, int mods)
    {
        //Check if mouse was pressed
        if (action == GLFW_PRESS)
        {
            //Check if only one button was pressed
            if (button < get().mouseButtonPressed.length)
            {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE)
        {
            if (button < get().mouseButtonPressed.length)
            {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void MouseScrollCallback(long window, double xOffset, double yOffset)
    {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void EndFrame()
    {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    //GETTERS and SETTERS Region
    // <editor-fold>

    public static float GetScreenX() {
        // Calculate the current X position relative to the viewport position
        float currentX = GetX() - get().GetViewportPos().x;

        // Normalize the X coordinate to the range [-1, 1] for OpenGL
        currentX = (currentX / get().GetViewportSize().x) * get().screenWidth;

        // Return the transformed X coordinate
        return currentX;
    }
    public static float GetScreenY() {
        // Calculate the current Y position relative to the viewport position
        float currentY = GetY() - get().GetViewportPos().y;

        // Normalize the Y coordinate to the range [-1, 1] for OpenGL
        currentY = get().screenHeight -((currentY / get().GetViewportSize().y) * get().screenHeight);

        // Return the transformed Y coordinate
        return currentY;
    }

    public static float GetOrthoX() {
        // Calculate the current X position relative to the viewport position
        float currentX = GetX() - get().GetViewportPos().x;

        // Normalize the X coordinate to the range [-1, 1] for OpenGL
        currentX = (currentX / get().GetViewportSize().x) * 2.0f - 1.0f;

        // Create a Vector4f with the normalized X coordinate and Y = 0
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        // Transform the Vector4f using the view-projection matrix
        tmp.mul(ViewProjection());

        // Update currentX with the transformed X coordinate
        currentX = tmp.x;

        // Return the transformed X coordinate
        return currentX;
    }

    public static float GetOrthoY() {
        // Calculate the current Y position relative to the viewport position
        float currentY = GetY() - get().GetViewportPos().y;

        // Normalize the Y coordinate to the range [-1, 1] for OpenGL
        currentY = -((currentY / get().GetViewportSize().y) * 2.0f - 1.0f);

        // Create a Vector4f with the normalized Y coordinate and X = 0
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        // Transform the Vector4f using the view-projection matrix
        tmp.mul(ViewProjection());

        // Update currentY with the transformed Y coordinate
        currentY = tmp.y;

        // Return the transformed Y coordinate
        return currentY;
    }

    private static Matrix4f ViewProjection()
    {
        // Get the camera from the current scene
        Camera camera = Window.GetScene().camera();

        // Create a new Matrix4f to hold the view-projection matrix
        Matrix4f viewProjection = new Matrix4f();

        // Multiply the inverse view matrix by the inverse projection matrix
        camera.GetInverseView().mul(camera.GetInverseProjection(), viewProjection);

        return viewProjection;
    }



    public static float GetX()
    {
        return (float) get().xPos;
    }

    public static float GetY()
    {
        return (float) get().yPos;
    }

    public static float GetDx()
    {
        return (float) (get().lastX - get().xPos);
    }

    public static float GetDy()
    {
        return (float) (get().lastY - get().yPos);
    }

    public static float GetScrollX()
    {
        return (float) get().scrollX;
    }

    public static float GetScrollY()
    {
        return (float) get().scrollY;
    }

    //currently not working, will be fixed in future tutorial
    public static boolean IsDragging()
    {
        return get().isDragging;
    }

    public static boolean MouseButtonDown(int button)
    {
        if (button < get().mouseButtonPressed.length)
        {
            return get().mouseButtonPressed[button];
        }
        else
        {
            return false;
        }
    }

    public  Vector2f GetViewportSize() {return viewportSize;}

    public static void SetViewportSize(Vector2f viewportSize) {get().viewportSize.set(viewportSize);}

    public static void SetScreenWidth(int screenWidth) {get().screenWidth = screenWidth; }

    public static void SetScreenHeight(int screenHeight) { get().screenHeight = screenHeight;}

    public Vector2f GetViewportPos() {return viewportPos;}

    public static void SetViewportPos(Vector2f viewportPos) {get().viewportPos.set(viewportPos);}

    // </editor-fold>
}
