package gameengine.Editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class JetImGui
{
    private static float defaultColumnWidth = 100;

    public static void DrawVec2Control(String label, Vector2f values)
    {
        DrawVec2Control(label, values, 0,defaultColumnWidth);
    }

    public static void DrawVec2Control(String label, Vector2f values, float resetValue)
    {
        DrawVec2Control(label, values, resetValue, defaultColumnWidth);
    }

    public static void DrawVec2Control(String label, Vector2f values, float resetValue, float columnWidth)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing,0,0);

        //Calculate spacing
        float lineHeight = ImGui.getFontSize() * 1.2f;
        Vector2f buttonSize = new Vector2f(lineHeight, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.f;

        //X VALUE
        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.f);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.f);
        if(ImGui.button("X", buttonSize.x, buttonSize.y))
            values.x = resetValue;

        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        //Y VALUE
        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y))
            values.y = resetValue;

        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();
        values.x = vecValuesX[0];
        values.y = vecValuesY[0];
        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static float DragFloat(String label, float value)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.dragFloat("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static int DragInt(String label, int value)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {value};
        ImGui.dragInt("##dragInt", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static boolean ColorPicker(String label, Vector4f color)
    {
        boolean res = false;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] imColor = {color.x, color.y, color.z, color.w};
        if(ImGui.colorEdit4("##colorPicker", imColor))
        {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static String InputText(String label, String text)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImString outString = new ImString(text,  256);

        if(ImGui.inputText("##" + label, outString))
            text = outString.get(); //Update Text with the value of the Input field

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }
}
