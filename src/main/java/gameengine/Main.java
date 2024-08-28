package gameengine;


import gameengine.Engine.Window;

//TODO: Se abbiamo annotazioni fissiamole nel Main :)
//TODO: Fissiamo convention per le variabili la prima lettera minuscola tipo "int shaderProgram"
//TODO:Se non l'hai fatto installa il plugin GLSL Support su File > Settings > Plugins

public class Main {
    public static void main(String[] args){
        Window window;
        window = Window.get();
        window.run();

    }
}
