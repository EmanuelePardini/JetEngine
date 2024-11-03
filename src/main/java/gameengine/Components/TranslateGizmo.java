package gameengine.Components;

import gameengine.Editor.PropertiesWindow;
import gameengine.Engine.MouseListener;

public class TranslateGizmo extends Gizmo
{
    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow)
    {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void EditorUpdate(float DeltaTime)
    {
        super.EditorUpdate(DeltaTime);

        if(activeGameObject != null)
        {
            if(xAxisActive && !yAxisActive) activeGameObject.transform.position.x -= MouseListener.GetWorldDx();
            else if(yAxisActive && !xAxisActive) activeGameObject.transform.position.y -= MouseListener.GetWorldDy();

        }
    }
}
