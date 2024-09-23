package gameengine.Components;

import gameengine.Engine.Component;

public class FontRenderer extends Component
{
    @Override
    public void Start()
    {
        if(gameObject.GetComponent(SpriteRenderer.class) != null)
        {
            System.out.println("Found Font Renderer!");
        }
    }

    @Override
    public void Update(float DeltaTime)
    {

    }
}
