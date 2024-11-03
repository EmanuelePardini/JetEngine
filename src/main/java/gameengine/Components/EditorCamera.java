package gameengine.Components;

import gameengine.Engine.Camera;
import gameengine.Engine.KeyListener;
import gameengine.Engine.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

//Different from a regular game camera
public class EditorCamera extends Component
{
    //todo: nel futuro verrà usato il metodo isDragging da mouselistener
    private float dragDebounce = 0.032f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float lerpTime = 0.0f;
    private float dragSensitivity = 10.0f;
    private float scrollSensitivity = 0.1f;


    public EditorCamera(Camera levelEditorCamera)
    {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }
    @Override
    public void EditorUpdate(float dt)
    {
        if(MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0)
        {
            this.clickOrigin = new Vector2f(MouseListener.GetOrthoX(), MouseListener.GetOrthoY());
            dragDebounce -= dt;
            return;
        }
        else if(MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE))
        {
            Vector2f mousePos = new Vector2f(MouseListener.GetOrthoX(),
                                MouseListener.GetOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);

            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if(dragDebounce <= 0.0f && !MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE))
        {
            dragDebounce = 0.1f;
        }

        if(MouseListener.GetScrollY() != 0.0f)
        {
            float addValue = (float)Math.pow(Math.abs(MouseListener.GetScrollY()) * scrollSensitivity,
                                1 / levelEditorCamera.GetZoom());

            //gets the sign of the value (to add or remove from zoom)
            addValue *= -Math.signum(MouseListener.GetScrollY());

            levelEditorCamera.AddZoom(addValue);
        }

        if(KeyListener.IsKeyPressed(GLFW_KEY_SPACE)) reset = true;

        if(reset)
        {
            //since it gets progressively slower the closer it gets to completing the lerp,
            // we create an increasingly bigger lerp time to smooth it
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);

            //we do a manual lerp for the zoom
            levelEditorCamera.SetZoom(this.levelEditorCamera.GetZoom() +
                                     (1.0f - levelEditorCamera.GetZoom()) * lerpTime);

            this.lerpTime += 0.1f * dt;


            if(Math.abs(levelEditorCamera.position.x) <= 5.0f &&
               Math.abs(levelEditorCamera.position.y) <= 5.0f)
            {
                levelEditorCamera.position.set(0f,0f);
                this.levelEditorCamera.SetZoom(1.0f);
                reset = false;
            }
        }
    }
}
