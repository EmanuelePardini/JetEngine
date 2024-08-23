package gameengine;


import gameengine.Engine.Window;

//temp lambda ex
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Window window;
        window = Window.get();
        window.run();


        //temp lambda ex
        List<String> names = Arrays.asList("Anna", "Johnny", "Paulo", "Christopher");

        // Ordinare per lunghezza delle stringhe
        names.sort((s1, s2) -> s1.length() - s2.length());

        System.out.println(names);
    }
}
