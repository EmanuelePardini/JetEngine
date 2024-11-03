package gameengine.Editor;

import gameengine.Physics.Components.Box2DCollider;
import gameengine.Physics.Components.CircleCollider;
import gameengine.Physics.Components.RigidBody2D;
import gameengine.Renderer.PickingTexture;
import gameengine.Components.NonPickable;
import gameengine.Engine.GameObject;
import gameengine.Engine.MouseListener;
import gameengine.Scenes.Scene;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow
{
    protected GameObject activeGameObject = null; //gameobject you are inspecting
    private PickingTexture pickingTexture;
    private float debounce = 0.2f; // A time delay to prevent multiple rapid mouse clicks from registering

    public PropertiesWindow(PickingTexture pickingTexture){this.pickingTexture = pickingTexture;}

    public void Update(float deltaTime, Scene currentScene) {
        debounce -= deltaTime;  // Decrease the debounce timer by the time elapsed since the last frame

        // Check if the left mouse button is pressed and if the debounce timer has expired
        if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int)MouseListener.GetScreenX();  // Get the current x position of the mouse on the screen
            int y = (int)MouseListener.GetScreenY();  // Get the current y position of the mouse on the screen
            int gameObjectId = pickingTexture.ReadPixel(x, y);  // Use the picking texture to read the object ID at the mouse's position
            GameObject pickedObj = currentScene.GetGameObject(gameObjectId);
            if(pickedObj != null && pickedObj.GetComponent(NonPickable.class) == null)
            {
                activeGameObject = pickedObj;  // Fetch the game object from the scene using the object ID
            }
            else if(pickedObj == null && !MouseListener.IsDragging())
            {
                activeGameObject = null;
            }

            this.debounce = 0.2f;  // Reset the debounce timer to 0.2 seconds to prevent immediate re-triggering
        }
    }

    public void ImGui()
    {
        if(activeGameObject != null)
        {
            ImGui.begin("Properties");

            PopUpAvailableComps();

            activeGameObject.ImGUI();
            ImGui.end();
        }
    }

    private void PopUpAvailableComps()
    {
        if (ImGui.beginPopupContextWindow("ComponentAdder")) {
            if (ImGui.menuItem("Add Rigidbody"))
            {
                if (activeGameObject.GetComponent(RigidBody2D.class) == null)
                    activeGameObject.AddComponent(new RigidBody2D());
            }

            if (ImGui.menuItem("Add Box Collider"))
            {
                if (activeGameObject.GetComponent(Box2DCollider.class) == null && activeGameObject.GetComponent(CircleCollider.class) == null)
                    activeGameObject.AddComponent(new Box2DCollider());
            }

            if (ImGui.menuItem("Add Circle Collider"))
            {
                if (activeGameObject.GetComponent(Box2DCollider.class) == null && activeGameObject.GetComponent(CircleCollider.class) == null)
                    activeGameObject.AddComponent(new CircleCollider());
            }

            ImGui.endPopup();
        }
    }

    public GameObject GetActiveGameObject(){return activeGameObject;}
    public void SetActiveGameObject(GameObject go){this.activeGameObject = go;}
}
