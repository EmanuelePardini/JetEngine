package gameengine;
import gameengine.Engine.Window;


//TODO: Se abbiamo annotazioni fissiamole nel Main :)
//TODO: Usare convention "Notazione ungara" in parte
//TODO:Se non l'hai fatto installa il plugin GLSL Support su File > Settings > Plugins

public class Main {
    public static void main(String[] args){
        Window window;
        window = Window.get();
        window.run();

    }
}
