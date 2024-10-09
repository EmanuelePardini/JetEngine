package gameengine.Editor;

import gameengine.Engine.MouseListener;
import gameengine.Engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

//THIS IS THE GABE GAMEVIEWWINDOW
public class Viewport
{
    private static float leftX, rightX, topY, bottomY;

    public void ImGui()
    {   //Create the ImGui Window
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = GetLargestSizeForViewport();
        ImVec2 windowPos = GetCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = GetTopLeft();

        ImGui.getCursorScreenPos(topLeft);

        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        int textureId = Window.GetFrameBuffer().GetTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y,0,1,1,0);

        MouseListener.SetViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.SetViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    public boolean GetWantCaptureMouse()
    {
       System.out.println("Capturing: " + leftX + "|" +rightX+ "|" +bottomY+ "|" +topY);
        System.out.println("Viewport: " +MouseListener.GetX() +"|" + MouseListener.GetY());

        return MouseListener.GetX() >= leftX && MouseListener.GetX() <= rightX &&
                MouseListener.GetY() >= bottomY && MouseListener.GetY() <= topY;
    }

    private static ImVec2 GetLargestSizeForViewport()
    {
        ImVec2 windowSize = GetTopLeft();

        //We could do that, but it don't take care of the Scroll dimension
        //ImGui.getContentRegionMax(windowSize);

        //So we take the region of interest and we subtract the scroll dimension
        ImGui.getContentRegionAvail(windowSize);

        //Save in a new var
        float aspectWidth = windowSize.x;
        //Proportionate height
        float aspectHeight = aspectWidth / Window.GetTargetAspectRatio();

        if(aspectHeight > windowSize.y)
        {
            //Must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.GetTargetAspectRatio();
        }

        //Return the max size
        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 GetCenteredPositionForViewport(ImVec2 aspectSize)
    {
        ImVec2 windowSize = GetTopLeft();
        ImGui.getContentRegionAvail(windowSize);

        float viewportX = (windowSize.x / 2.f) - (aspectSize.x / 2.f);
        float viewportY = (windowSize.y / 2.f) - (aspectSize.y / 2.f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }

    private static ImVec2 GetTopLeft()
    {
        ImVec2 topLeft = new ImVec2();
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();

        return topLeft;
    }
}
