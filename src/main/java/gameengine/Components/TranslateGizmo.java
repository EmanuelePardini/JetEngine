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
    public void Update(float DeltaTime)
    {
        super.Update(DeltaTime);

        if(activeGameObject != null)
        {
            if(xAxisActive)
            {
                activeGameObject.transform.position.x -= MouseListener.GetWorldDX();
            }
            else if(yAxisActive)
            {
                activeGameObject.transform.position.y -= MouseListener.GetWorldDY();
            }
        }
    }


}
