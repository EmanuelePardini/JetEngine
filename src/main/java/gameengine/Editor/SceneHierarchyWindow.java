package gameengine.Editor;

import gameengine.Engine.GameObject;
import gameengine.Engine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow
{
    private static String hierarchyName = "SceneHierarchy";
    public void ImGui()
    {
        ImGui.begin(hierarchyName);
        List<GameObject> gameObjects = Window.GetScene().GetGameObjects();
        int index = 0;
        for(GameObject obj : gameObjects)
        {   //If is not serializable we don't want to display it
            if(!obj.ToSerialize()) continue;

            boolean treeNodeOpen = DoTreeNode(obj, index);

            if(treeNodeOpen) ImGui.treePop();
            index++;
        }
        ImGui.end();
    }

    private boolean DoTreeNode(GameObject obj, int index)
    {
        ImGui.pushID(index);
        boolean treenodeOpen = ImGui.treeNodeEx(
                obj.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                obj.name
        );
        ImGui.popID();

        if(ImGui.beginDragDropSource())
        {
            ImGui.setDragDropPayloadObject(hierarchyName, obj);
            ImGui.text(obj.name);
            ImGui.endDragDropSource();
        }

        if(ImGui.beginDragDropTarget())
        {
            Object payloadObj = ImGui.acceptDragDropPayloadObject(hierarchyName);
            if(payloadObj != null)
            {
                if(payloadObj.getClass().isAssignableFrom(GameObject.class))
                {
                    GameObject acceptedObj = (GameObject)payloadObj;
                    System.out.println("Moved Hierarchy " + acceptedObj);
                }
            }
            ImGui.endDragDropTarget();
        }

        return treenodeOpen;
    }
}
