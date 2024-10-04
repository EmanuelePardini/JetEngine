package gameengine.Editor;

import gameengine.Engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

//THIS IS THE GABE GAMEVIEWWINDOW
public class Viewport
{
    public static void ImGui()
    {   //Create the ImGui Window
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollbar);

        ImVec2 windowSize = GetLargestSizeForViewport();
        ImVec2 windowPos = GetCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);
        int textureId = Window.GetFrameBuffer().GetTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y,0,1,1,0);

        ImGui.end();
    }

    private static ImVec2 GetLargestSizeForViewport()
    {
        ImVec2 windowSize = new ImVec2();

        //We could do that, but it don't take care of the Scroll dimension
        //ImGui.getContentRegionMax(windowSize);

        //So we take the region of interest and we subtract the scroll dimension
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

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
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.f) - (aspectSize.x / 2.f);
        float viewportY = (windowSize.y / 2.f) - (aspectSize.y / 2.f);

        return new ImVec2(viewportX, //+ ImGui.getCursorPosX(), //ADD IN CASE YOU HAVE A EMPTY WHITE BAR AT THE TOP OF THE VIEWPORT
                viewportY ); //+ ImGui.getCursorPosY()); //ADD IN CASE YOU HAVE A EMPTY WHITE BAR AT THE TOP OF THE VIEWPORT
    }
}
