package gameengine.Engine;


import gameengine.util.Time;
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

    private static Scene currentScene = null;

    private Window(){
        this.width = 1920;
        this.height = 1080;

        this.title = "JetEngine";

        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    //Singleton
    public static Window get()
    {
        if(Window.window == null) Window.window = new Window();

        return Window.window;
    }

    //Changes scene dynamically
    public static void changeScene(int newScene)
    {
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                //currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene'" + newScene + "'";
        }
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
        glfwSetErrorCallback(null).free(); //May cause a nullptr exception
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



        /*In Java :: è un operatore di riferimento al metodo, e può essere considerato una scorciatoia
          per una Lambda expression. In c++ è un operatore di risoluzione dell'ambito, e può essere usato tra le altre cose
          per implementare metodi fuori dalla dichiarazione di una classe
          eg. void MyClass::myMethod() { implementazione fuori dalla classe }

          Una Lambda expression è una funzione anonima, cioè senza nome, usata per scrivere codice in modo
          più conciso o funzionale. Ad esempio (x, y) -> x + y, che somma i valori x ed y. Ti lascio un esempio in main.
          Se vuoi rivederle insieme son felice che non le ho capite bene...
         */

        //Bind mouse delegates for mouse events
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback); //Lambda to call delegates (forwards your position to the function)
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        //Bind key delegate
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);


        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);
        //Make the window visible
        glfwShowWindow(glfwWindow);
        /*This line is critical for LWJGL interoperation with GLFW
        OpenGL context, or any context that is managed externally
        LWJGL detects the context that is current in the current thread
        creates the GLCapabilities instance and makes the OpenGL
        bindings available for use*/
        GL.createCapabilities();
    }


    public void loop()
    {
        //Initialize frame time
        float beginFrameTime = Time.getTime();
        float endFrameTime = Time.getTime();

        while(!glfwWindowShouldClose(glfwWindow))
        {
            //Poll input events
            glfwPollEvents();

            //choose color buffer color
            glClearColor(r,g,b,a);

            //clears buffer, meaning it fills it in this case
            glClear(GL_COLOR_BUFFER_BIT);

            //swaps back buffer with front buffer
            glfwSwapBuffers(glfwWindow);

             /*TESTS*/
            // <editor-fold>
            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE))
            {
                r = 0;
                g = 0;
                b = 0;
            }

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                r = 1;
                g = 0;
                b = 0;
            }

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_2)){
                r = 1;
                g = 1;
                b = 0;
            }
            // </editor-fold>

            //calculating dt, and updating time vars
            endFrameTime = Time.getTime();
            float dt = endFrameTime - beginFrameTime;
            beginFrameTime = endFrameTime;
        }
    }
}
