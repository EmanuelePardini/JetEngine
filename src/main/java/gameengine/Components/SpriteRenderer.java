package gameengine.Components;
import Renderer.Texture;
import gameengine.Engine.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color;
    private Sprite sprite;

    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite)
    {
        this.sprite = sprite;
        //set to white
        this.color = new Vector4f(1,1,1,1);
    }

    @Override
    public void Start()
    {

    }
    @Override
    public void Update(float DeltaTime)
    {

    }

    public Vector4f GetColor() {return this.color;}

    public Texture GetTexture(){return sprite.GetTexture();}

    public Vector2f[] GetTextCoords()
    {
        return sprite.GetTextCoords();
    }
}
