package gameengine;


import gameengine.Window.Window;

public class Main {
    public static void main(String[] args){
        Window window;
        window = Window.get();
        window.run();
    }
}
