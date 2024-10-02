package gameengine.Components;

import Renderer.DebugDraw;
import gameengine.Engine.Window;
import gameengine.Util.Settings;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component
{
    @Override
    public void Update(float dt)
    {
        Vector2f cameraPos = Window.GetScene().camera().position;
        Vector2f projectionSize = Window.GetScene().camera().GetProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) -1) * Settings.GRID_HEIGHT;

        int numVtLines = (int)(projectionSize.x / Settings.GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y / Settings.GRID_HEIGHT) + 2;

        int height = (int)projectionSize.y + Settings.GRID_HEIGHT * 2;
        int width = (int)projectionSize.x + Settings.GRID_WIDTH * 2;

        int maxLines = Math.max(numVtLines,numHzLines);
        Vector3f color = new Vector3f(0.2f,0.2f,0.2f);

        for(int i = 0; i < maxLines; i++)
        {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i< numVtLines)
            {
                DebugDraw.AddLine2D(new Vector2f(x,firstY), new Vector2f(x,firstY + height), color);
            }

            if (i<numHzLines)
            {
                DebugDraw.AddLine2D((new Vector2f(firstX,y)), new Vector2f(firstX + width,y), color);
            }
        }
    }
}
