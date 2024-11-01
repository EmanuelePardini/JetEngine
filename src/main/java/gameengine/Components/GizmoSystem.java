package gameengine.Components;

import gameengine.Engine.KeyListener;
import gameengine.Engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class GizmoSystem extends Component
{
    private Spritesheet gizmos;
    private int usingGizmo = 0;

    public  GizmoSystem(Spritesheet gizmoSprites)
    {
        gizmos = gizmoSprites;
    }

    @Override
    public void Start()
    {
        gameObject.AddComponent(new TranslateGizmo(gizmos.GetSprite(1),
                Window.GetImGuiLayer().GetPropertiesWindow())); //It's a bad design, but we will change it with an event system
        gameObject.AddComponent(new ScaleGizmo(gizmos.GetSprite(2),
                Window.GetImGuiLayer().GetPropertiesWindow()));
    }

    @Override
    public void Update(float DeltaTime)
    {
        if(usingGizmo == 0)
        {
            gameObject.GetComponent(TranslateGizmo.class).SetUsing(true);
            gameObject.GetComponent(ScaleGizmo.class).SetUsing(false);
        }
        else if(usingGizmo == 1)
        {
            gameObject.GetComponent(TranslateGizmo.class).SetUsing(false);
            gameObject.GetComponent(ScaleGizmo.class).SetUsing(true);
        }

        if(KeyListener.IsKeyPressed(GLFW_KEY_E))
        {
            usingGizmo = 0;
        }
        else if(KeyListener.IsKeyPressed(GLFW_KEY_R))
        {
            usingGizmo = 1;
        }
    }
}
