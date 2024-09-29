package gameengine.Components;
import Renderer.Texture;
import gameengine.Engine.Component;
import gameengine.Engine.Transform;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    //transient variables get ignored in serialization
    private transient Transform lastTransform;
    private transient boolean isDirty = false;

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
    public void ImGUI()
    {
        float[] imColor = {color.x, color.y, color.z, color.w};

        if(ImGui.colorPicker4("Color Picker: ", imColor))
        {
            this.color.set(imColor[0],imColor[1],imColor[2],imColor[3]);
            this.isDirty = true;
        }

    }

    public Vector4f GetColor() {return this.color;}

    public Texture GetTexture(){return sprite.GetTexture();}

    public boolean IsDirty() {return isDirty;}

    public Vector2f[] GetTextCoords()
    {
        return sprite.GetTextCoords();
    }

    public void SetSprite(Sprite sprite)
    { //I know what you're thinking, but it's a sprite bro
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

}
