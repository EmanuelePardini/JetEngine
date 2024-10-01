package gameengine.Scenes;

import gameengine.Engine.Scene;
import gameengine.Engine.Window;

public class LevelScene extends Scene
{
    public LevelScene()
    {
        //check level scene correctly initialized, change screen to green
        System.out.println("Inside level scene");
        Window.Get().g = 1;
    }

    @Override
    public void Update(float dt)
    {

    }
}
