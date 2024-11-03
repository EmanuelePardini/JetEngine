package gameengine.Components;

import gameengine.Engine.GameObject;
import gameengine.Engine.MouseListener;
import gameengine.Engine.Window;

import static gameengine.Components.GridLines.GRID_HEIGHT;
import static gameengine.Components.GridLines.GRID_WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component
{
    GameObject holdingObject = null;

    public void PickUpObject(GameObject go)
    {  //Set the value of holding object to the func param
        this.holdingObject = go;
        Window.GetScene().AddGameObjectToScene(go);

    }

    public void Place()
    { //Release the object in the scene by setting the holdingObject ref to null
        this.holdingObject = null;
    }

    @Override
    public void EditorUpdate(float DeltaTime)
    {
        if(holdingObject != null)
        { //Keep it attached to the mouse cursor for the placement period
            holdingObject.transform.position.x = MouseListener.GetOrthoX();
            holdingObject.transform.position.y = MouseListener.GetOrthoY();
            //snap position
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / GRID_WIDTH) * GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / GRID_HEIGHT) * GRID_HEIGHT;

            //System.out.println("ObjX: " + holdingObject.transform.position.x);
            //System.out.println("ObjY: " + holdingObject.transform.position.y);

            if(MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) Place();
        }
    }
}
