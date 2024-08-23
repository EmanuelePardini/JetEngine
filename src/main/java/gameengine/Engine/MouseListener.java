package gameengine.Engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX;
    private double scrollY;

    private double xPos;
    private double yPos;
    private double lastY;
    private double lastX;

    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0;
        this.scrollY =0;

        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    //Singleton
    public static MouseListener get()
    {
        if(instance == null) MouseListener.instance = new MouseListener();

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos)
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

    public static void mouseButtonCallback(long window, int button, int action, int mods)
    {
        //Check if mouse was pressed
        if(action == GLFW_PRESS)
        {
            //Check if only one button was pressed
            if(button < get().mouseButtonPressed.length)
            {
                get().mouseButtonPressed[button] = true;
            }
        }

        else if(action == GLFW_RELEASE)
        {
            if(button < get().mouseButtonPressed.length)
            {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame()
    {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    //GETTERS Region
    // <editor-fold>
    public static float getX()
    {
        return (float)get().xPos;
    }

    public static float getY()
    {
        return (float)get().yPos;
    }

    public static float getDx()
    {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy()
    {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX()
    {
        return (float)get().scrollX;
    }

    public static float getScrollY()
    {
        return (float)get().scrollY;
    }

    public static boolean isDragging()
    {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button)
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
    // </editor-fold>
}
