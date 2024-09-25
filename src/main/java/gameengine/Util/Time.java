package gameengine.Util;

public class Time
{
    //Initialized on application start
    public static float timeStarted = System.nanoTime();

    //before getting, converts nanoseconds to seconds multiplying by 1E-9
    public static float getTime(){ return (float) ((System.nanoTime() - timeStarted) * 1E-9);}

    //could be buggy, in case use glfwGetTime();
}
