package gameengine.Engine;

public class LevelScene extends Scene
{
    public LevelScene()
    {
        //check level scene correctly initialized, change screen to green
        System.out.println("Inside level scene");
        Window.get().g = 1;
    }

    @Override
    public void Update(float dt)
    {

    }
}
