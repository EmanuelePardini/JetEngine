package gameengine.Engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener
{
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];
    private boolean keyBeginPress[] = new boolean[350];

    private KeyListener()
    {

    }

    //Singleton
    public static KeyListener get()
    {
        if (KeyListener.instance == null) KeyListener.instance = new KeyListener();
        return KeyListener.instance;
    }

    public static void KeyCallback(long window, int key, int scancode, int action, int mods)
    {
        //Check if key was pressed
        if (action == GLFW_PRESS)
        {
            get().keyPressed[key] = true;
            get().keyBeginPress[key] = true;
        } else if (action == GLFW_RELEASE)
        {
            get().keyPressed[key] = false;
            get().keyBeginPress[key] = false;
        }
    }

    public static boolean IsKeyPressed(int keyCode)
    {
        return get().keyPressed[keyCode];
    }
    public static boolean KeyBeginPress(int keyCode)
    {
        boolean res = get().keyBeginPress[keyCode];
        if(res)
            get().keyBeginPress[keyCode] = false;

        return res;
    }
}
