package gameengine.Editor;

import gameengine.Engine.GameObject;
import gameengine.Engine.KeyListener;
import gameengine.Engine.Window;
import gameengine.Observers.EventSystem;
import gameengine.Observers.Events.Event;
import gameengine.Observers.Events.EventType;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.*;

public class SaveMenu
{
    public void ImGui()
    {
        ImGui.beginMainMenuBar();

        if(ImGui.beginMenu("File"))
        {
            if(ImGui.menuItem("Save", "Ctrl+S"))
                EventSystem.Notify(null, new Event(EventType.SaveLevel));
            if(ImGui.menuItem("Load", "Ctrl+C"))
                EventSystem.Notify(null, new Event(EventType.LoadLevel));

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

        if(KeyListener.IsKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.KeyBeginPress(GLFW_KEY_S))
            EventSystem.Notify(null, new Event(EventType.SaveLevel));

    }
}
