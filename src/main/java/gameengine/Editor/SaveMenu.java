package gameengine.Editor;

import gameengine.Observers.EventSystem;
import gameengine.Observers.Events.Event;
import gameengine.Observers.Events.EventType;
import imgui.ImGui;

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
    }
}
