package gameengine.Components;

import gameengine.Editor.PropertiesWindow;
import gameengine.Engine.GameObject;
import gameengine.Engine.MouseListener;
import gameengine.Engine.Prefabs;
import gameengine.Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Gizmo extends Component
{
    //Arrow properties
    private Vector4f xAxisColor = new Vector4f(1,0.3f,0.3f,1);
    private Vector4f xAxisColorHover = new Vector4f(1,0,0,1);
    private Vector4f yAxisColor = new Vector4f(0.3f,1,0.3f,1);
    private Vector4f yAxisColorHover = new Vector4f(0,1,0,1);

    //The arrows will be objects
    private GameObject xAxisObject;
    private GameObject yAxisObject;
    //Objects with a sprite
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    //We will center the gizmos on the active GameObject
    protected GameObject activeGameObject;
    //Referencing to the properties window
    private PropertiesWindow propertiesWindow;
    private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);

    private int gizmoWidth = 16;
    private int gizmoHeight = 48;

    private boolean using = false;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow)
    {
        this.xAxisObject = Prefabs.GenerateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.GenerateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.GetComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.GetComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        this.xAxisObject.AddComponent(new NonPickable());
        this.yAxisObject.AddComponent(new NonPickable());

        Window.GetScene().AddGameObjectToScene(this.xAxisObject);
        Window.GetScene().AddGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void Start()
    {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.transform.zIndex = 99;
        this.yAxisObject.transform.zIndex = 99;
        this.xAxisObject.SetSerialize(false);
        this.yAxisObject.SetSerialize(false);
    }

    @Override
    public void EditorUpdate(float DeltaTime)
    {
        if(!using) return;

        //Always check and keep updated the active go
        this.activeGameObject = this.propertiesWindow.GetActiveGameObject();
        if(this.activeGameObject == null)
        {
            this.SetInactive();
            return;
        }
        //BELOW ONLY IF THERE IS A ACTIVE OBJ

        this.SetActive();

        boolean xAxisHot = CheckXHoverState();
        boolean yAxisHot = CheckYHoverState();

        if((xAxisHot || xAxisActive) && MouseListener.IsDragging() && MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            xAxisActive = true;
            yAxisActive = false;
        }
        else if((yAxisHot || yAxisActive) && MouseListener.IsDragging() && MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            xAxisActive = false;
            yAxisActive = true;
        }
        else
        {
            xAxisActive = false;
            yAxisActive = false;
        }

        this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
        this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
        this.xAxisObject.transform.position.add(xAxisOffset);
        this.yAxisObject.transform.position.add(yAxisOffset);
    }

    @Override
    public void Update(float DeltaTime)
    {
        SetInactive();
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

    private boolean CheckXHoverState()
    {
        Vector2f mousePos = new Vector2f(MouseListener.GetOrthoX(), MouseListener.GetOrthoY());

        if(mousePos.x <= xAxisObject.transform.position.x &&
                mousePos.x >= xAxisObject.transform.position.x - gizmoHeight &&
                mousePos.y >= xAxisObject.transform.position.y &&
                mousePos.y <= xAxisObject.transform.position.y + gizmoWidth)
        {
            xAxisSprite.SetColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.SetColor(xAxisColor);
        return false;
    }

    private boolean CheckYHoverState()
    {
        Vector2f mousePos = new Vector2f(MouseListener.GetOrthoX(), MouseListener.GetOrthoY());

        if(mousePos.x <= yAxisObject.transform.position.x &&
                mousePos.x >= yAxisObject.transform.position.x - gizmoWidth &&
                mousePos.y <= yAxisObject.transform.position.y &&
                mousePos.y >= yAxisObject.transform.position.y - gizmoHeight)
        {
            yAxisSprite.SetColor(yAxisColorHover);
            return true;
        }

        yAxisSprite.SetColor(yAxisColor);
        return false;
    }

    public void SetUsing(boolean newUsing)
    {
        this.using = newUsing;
        if(!this.using) SetInactive();
    }
}
