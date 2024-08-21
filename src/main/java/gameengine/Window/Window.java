package gameengine.Window;


import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    //Resolution and info
    int width;
    int height;
    String title;
    private long glfwWindow; //Number where the window is memorized in the memory space

    //Colors
    private float r;
    private float g;
    private float b;
    private float a;

    private static Window window = null;

    private Window(){
        this.width = 1920;
        this.height = 1080;

        this.title = "JetEngine";

        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    public static Window get()
    {
        if(Window.window == null) Window.window = new Window();

        return Window.window;
    }

    public void run()
    {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        //glfwSetErrorCallback(null).free(); May cause a nullptr exception
    }

    public void init()
    {
        //Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        //Bind mouse delegates for mouse events
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback); //Lamba expression to call delegate
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);
        //Make the window visible
        glfwShowWindow(glfwWindow);
        /*This line is critical for LWGL interoperation with GLFW
        OpenGL context, or any context that is managed externally
        LWJGL detects the context that is current in the current thread
        creates the GLCapabilities instance and makes the OpenGL
        bindings avaible for use*/
        GL.createCapabilities();
    }

    public void loop()
    {
        while(!glfwWindowShouldClose(glfwWindow))
        {
            //Poll events
            glfwPollEvents();

            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            /*TEST
            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE))
            {
                r = 0;
                g = 0;
                b = 0;
            }
            */
            glfwSwapBuffers(glfwWindow);



        }
    }
}
