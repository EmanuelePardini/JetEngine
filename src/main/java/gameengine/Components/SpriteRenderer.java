package gameengine.Components;
import gameengine.Renderer.Texture;
import gameengine.Editor.JetImGui;
import gameengine.Engine.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    //transient variables get ignored in serialization
    private transient Transform lastTransform;
    private transient boolean isDirty = true;

    //GSON does not support constructors that take in variables.
//    public SpriteRenderer(Vector4f color)
//    {
//        this.color = color;
//        this.sprite = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite)
//    {
//        this.sprite = sprite;
//        //set to white
//        this.color = new Vector4f(1,1,1,1);
//        this.isDirty = true;
//    }
    @Override
    public Component Copy()
    {
        SpriteRenderer copy = new SpriteRenderer();

        // Copy color by creating a new Vector4f with the same values
        copy.color = new Vector4f(this.color);

        // Copy sprite by creating a new Sprite instance if it exists
        copy.sprite = this.sprite != null ? this.sprite.Copy() : new Sprite();

        // Copy the isDirty status
        copy.isDirty = this.isDirty;

        // Copy lastTransform by creating a new Transform instance if it exists
        copy.lastTransform = this.lastTransform != null ? this.lastTransform.Copy() : null;

        return copy;
    }
    @Override
    public void Start()
    {
        this.lastTransform = gameObject.transform.Copy();
    }

    @Override
    public void Update(float DeltaTime)
    {
        if(!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.Copy(this.lastTransform);
            this.isDirty = true;
        }
    }

    @Override
    public void EditorUpdate(float DeltaTime)
    {
        if(!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.Copy(this.lastTransform);
            this.isDirty = true;
        }
    }

    @Override
    public void ImGui()
    {
        if(JetImGui.ColorPicker("Color Picker", this.color))
            this.isDirty = true;
    }

    public Vector4f GetColor() {return this.color;}

    public Texture GetTexture(){return sprite.GetTexture();}

    public boolean IsDirty() {return isDirty;}

    public void SetDirty(boolean isDirty){this.isDirty = isDirty;}

    public Vector2f[] GetTextCoords()
    {
        return sprite.GetTextCoords();
    }

    public void SetSprite(Sprite sprite)
    {
        //We don't have to check if it's equal because we change sprite
        //in leveleditor and if we change we will not set an equal sprite
        this.sprite = sprite;
        this.isDirty = true; //Always check dirty on any update
    }

    public void SetColor(Vector4f color)
    {
        if(!this.color.equals(color))
        {
            this.color = color;
            this.isDirty = true; //Always check dirty on any update
        }
    }

    public void SetClean(){this.isDirty = false;}

    public void SetTexture(Texture texture)
    {
        this.sprite.SetTexture(texture);
    }

    public Sprite GetSprite() {return sprite;}
}
