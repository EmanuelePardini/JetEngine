package gameengine.Engine;

import Renderer.DebugDraw;
import Renderer.Framebuffer;
import gameengine.Scenes.LevelEditorScene;
import gameengine.Scenes.LevelScene;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.awt.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{

    //Resolution and info
    int screenWidth;
    int screenHeight;
    int width;
    int height;
    String title;
    private long glfwWindow; //Number where the window is memorized in the memory space
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;

    //Colors
    public float r;
    public float g;
    public float b;
    public float a;


    private static Window window = null;

    private static Scene currentScene = null;

    private Window()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //I changed that to take directly the screen of users to solve some bugs
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;
        this.width = this.screenWidth;
        this.height = this.screenHeight;

        this.title = "JetEngine";

        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    //Singleton
    public static Window Get()
    {
        if (Window.window == null) Window.window = new Window();

        return Window.window;
    }

    public static Scene GetScene()
    {
        return Get().currentScene;
    }

    //Changes scene dynamically
    public static void ChangeScene(int newScene)
    {
        switch (newScene)
        {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene'" + newScene + "'";
        }

        currentScene.Load();
        currentScene.Init();
        currentScene.Start();
    }

    public void Run()
    {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        Init();
        Loop();

        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free(); //May cause a nullptr exception
    }

    public void Init()
    {
        //Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL)
        {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        //Bind mouse delegates for mouse events
        glfwSetCursorPosCallback(glfwWindow, MouseListener::MousePosCallback); //Lambda to call delegates (forwards your position to the function)
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::MouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::MouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::KeyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) ->{Window.SetWidth(newWidth);
            Window.SetHeight(newHeight);});

        //to fix mouse bug
        glfwSetWindowSize(glfwWindow, screenWidth, screenHeight);

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync. Also locks framerate of the entire editor.
        glfwSwapInterval(1);
        //Make the window visible
        glfwShowWindow(glfwWindow);

        /*This line is critical for LWJGL interoperation with GLFW
        OpenGL context, or any context that is managed externally
        LWJGL detects the context that is current in the current thread
        creates the GLCapabilities instance and makes the OpenGL
        bindings available for use*/
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.InitImGui();
        //This override our GameObject textures
        this.framebuffer = new Framebuffer(screenWidth, screenHeight);
        glViewport(0,0, screenWidth, screenHeight); //Specs viewport transformation

        Window.ChangeScene(0);
    }

    public void Loop()
    {
        System.out.println("WinX: " + width);
        System.out.println("WinY: " + height);

        //Initialize frame time
        float beginFrameTime = (float)glfwGetTime();
        float endFrameTime;

        float DeltaTime = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow))
        {
            //Poll input events
            glfwPollEvents();

            DebugDraw.BeginFrame();


            this.framebuffer.Bind();

            //choose color buffer color
            glClearColor(r, g, b, a);
            //clears buffer, meaning it fills it in this case
            glClear(GL_COLOR_BUFFER_BIT);

            //This will be bound when we are drawing all our scene data
            //Calling the framebuffer here override the scene to render
            if (DeltaTime >= 0)
            {
                DebugDraw.Draw();
                currentScene.Update(DeltaTime);
            }
            this.framebuffer.Unbind();

            //Update ImGui
            this.imGuiLayer.Update(DeltaTime, currentScene);

            //swaps back buffer with front buffer
            glfwSwapBuffers(glfwWindow);

            //calculating dt, and updating time vars
            endFrameTime = (float)glfwGetTime();
            DeltaTime = endFrameTime - beginFrameTime;
            beginFrameTime = endFrameTime;
        }

        currentScene.SaveExit();
    }

    public static int GetWidth() {return Get().width;}

    public static int GetHeight() {return Get().height;}

    public static void SetWidth(int newWidth) {Get().width = newWidth;}

    public static void SetHeight(int newHeight) {Get().height = newHeight;}

    public static Framebuffer GetFrameBuffer(){return Get().framebuffer;}
    public static float GetTargetAspectRatio(){return (float)Get().screenWidth / (float)Get().screenHeight;} //Is the same to do 16/9
}
