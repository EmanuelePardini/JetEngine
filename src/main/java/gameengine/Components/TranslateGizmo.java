package gameengine.Components;

import gameengine.Editor.PropertiesWindow;
import gameengine.Engine.GameObject;
import gameengine.Engine.Prefabs;
import gameengine.Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslateGizmo extends Component
{
    //Arrow properties
    private Vector4f xAxisColor = new Vector4f(1,0,0,1);
    private Vector4f xAxisColorHover = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0,1,0,1);
    private Vector4f yAxisColorHover = new Vector4f();

    //The arrows will be objects
    private GameObject xAxisObject;
    private GameObject yAxisObject;
    //Objects with a sprite
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    //We will center the gizmos on the active GameObject
    private GameObject activeGameObject;
    //Referencing to the properties window
    private PropertiesWindow propertiesWindow;
    private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow)
    {
        this.xAxisObject = Prefabs.GenerateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.GenerateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.GetComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.GetComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        Window.GetScene().AddGameObjectToScene(this.xAxisObject);
        Window.GetScene().AddGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void Start()
    {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.SetSerialize(false);
        this.yAxisObject.SetSerialize(false);
    }

    @Override
    public void Update(float DeltaTime)
    { //Always check and keep updated the active go
        this.activeGameObject = this.propertiesWindow.GetActiveGameObject();

        if(this.activeGameObject != null)
        { //If there is an active go set active the gizmos
            this.SetActive();
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(xAxisOffset);
            this.yAxisObject.transform.position.add(yAxisOffset);
        }
        else
        {
            this.SetInactive();
        }
    }

    private void SetActive()
    {
        this.xAxisSprite.SetColor(xAxisColor);
        this.yAxisSprite.SetColor(yAxisColor);

    }

    private void SetInactive()
    {
        this.activeGameObject = null;
        this.xAxisSprite.SetColor(new Vector4f(0,0,0,0));
        this.yAxisSprite.SetColor(new Vector4f(0,0,0,0));
    }

}
