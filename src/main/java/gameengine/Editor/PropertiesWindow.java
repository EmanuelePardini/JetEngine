package gameengine.Editor;

import Renderer.PickingTexture;
import gameengine.Engine.GameObject;
import gameengine.Engine.MouseListener;
import gameengine.Scenes.Scene;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow
{
    protected GameObject activeGameObject = null; //gameobject you are inspecting
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture){this.pickingTexture = pickingTexture;}

    public void Update(float deltaTime, Scene currentScene)
    {
        if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            int x = (int)MouseListener.GetScreenX();
            int y = (int)MouseListener.GetScreenY();
            int gameObjectId = pickingTexture .ReadPixel(x,y);
            activeGameObject = currentScene.GetGameObject(gameObjectId);
        }
    }

    public void ImGui()
    {
        if(activeGameObject != null)
        {
            ImGui.begin("Properties");
            activeGameObject.ImGUI();
            ImGui.end();
        }
    }
}
