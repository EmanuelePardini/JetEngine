package gameengine.Editor;

import gameengine.Engine.GameObject;
import gameengine.Engine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow
{
    public void ImGui()
    {
        ImGui.begin("Scene Hierarchy");
        List<GameObject> gameObjects = Window.GetScene().GetGameObjects();
        int index = 0;
        for(GameObject obj : gameObjects)
        {   //If is not serializable we don't want to display it
            if(!obj.ToSerialize()) continue;

            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(
                    obj.name,
                    ImGuiTreeNodeFlags.DefaultOpen |
                            ImGuiTreeNodeFlags.FramePadding |
                            ImGuiTreeNodeFlags.OpenOnArrow |
                            ImGuiTreeNodeFlags.SpanAvailWidth,
                    obj.name
            );
            ImGui.popID();

            if(treeNodeOpen) ImGui.treePop();
        }
        ImGui.end();
    }
}
