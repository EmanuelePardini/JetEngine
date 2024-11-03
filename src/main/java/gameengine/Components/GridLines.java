package gameengine.Components;

import gameengine.Renderer.DebugDraw;
import gameengine.Engine.Camera;
import gameengine.Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

//This component will be in charge of Drawing GridLines
public class GridLines extends Component
{
    public static int GRID_WIDTH = 16;
    public static int GRID_HEIGHT = 16;
    
    @Override
    public void EditorUpdate(float dt)
    {
        Camera camera = Window.GetScene().camera();

        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.GetProjectionSize();

        //Calculate where our first X and Y are
        int firstX = ((int)(cameraPos.x / GRID_WIDTH) - 1) * GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / GRID_HEIGHT) -1) * GRID_HEIGHT;

        //Calculate how many vertical and horizontal lines we can have
        int numVtLines = (int)(projectionSize.x * camera.GetZoom() / GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y * camera.GetZoom()/ GRID_HEIGHT) + 2;

        //Set a limit to avoid infinite lines
        int height = (int)(projectionSize.y * camera.GetZoom()) + GRID_HEIGHT * 2;
        int width = (int)(projectionSize.x * camera.GetZoom()) + GRID_WIDTH * 2;

        int maxLines = Math.max(numVtLines,numHzLines);
        Vector3f color = new Vector3f(0.2f,0.2f,0.2f);

        for(int i = 0; i < maxLines; i++)
        {  //Draw the Grid Lines
            int x = firstX + (GRID_WIDTH * i);
            int y = firstY + (GRID_HEIGHT * i);

            if (i< numVtLines)
                DebugDraw.AddLine2D(new Vector2f(x,firstY), new Vector2f(x,firstY + height), color);

            if (i<numHzLines)
                DebugDraw.AddLine2D((new Vector2f(firstX,y)), new Vector2f(firstX + width,y), color);
        }
    }

    @Override
    public void Update(float DeltaTime)
    {

    }
}
