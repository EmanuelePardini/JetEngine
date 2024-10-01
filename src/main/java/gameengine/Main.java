package gameengine;
import gameengine.Engine.Window;

public class Main {
    public static void main(String[] args){
        try
        {
            Window window;
            window = Window.Get();
            window.Run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
