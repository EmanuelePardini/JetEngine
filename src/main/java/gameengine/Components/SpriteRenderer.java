package gameengine.Components;
import gameengine.Engine.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    Vector4f color;

    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
    }

    @Override
    public void Start()
    {

    }
    @Override
    public void Update(float DeltaTime)
    {

    }

    public Vector4f GetColor()
    {
        return this.color;
    }
}
