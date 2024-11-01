package gameengine.Components;

import gameengine.Editor.PropertiesWindow;
import gameengine.Engine.MouseListener;
import javafx.scene.transform.Scale;

public class ScaleGizmo extends Gizmo
{
    public ScaleGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow)
    {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void Update(float DeltaTime)
    {
        super.Update(DeltaTime);

        if (activeGameObject != null)
        {
            if (xAxisActive && !yAxisActive) activeGameObject.transform.scale.x -= MouseListener.GetWorldDx();
            else if (yAxisActive && !xAxisActive) activeGameObject.transform.scale.y -= MouseListener.GetWorldDy();
        }
    }
}
