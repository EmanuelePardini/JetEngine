package gameengine.Components;
import gameengine.Engine.Component;

public class SpriteRenderer extends Component
{
    private boolean firstTime = true;

    @Override
    public void Start()
    {
        System.out.println("Starting Component");
    }
    @Override
    public void Update(float DeltaTime)
    {
        if(firstTime)
        {
            System.out.println("Updating Component");
            firstTime = false;
        }
    }
}
