package gameengine.Physics.Components;

import gameengine.Components.Component;
import org.joml.Vector2f;

public class CircleCollider extends Component
{
    private Vector2f halfSize = new Vector2f(1);

    public Vector2f GetHalfSize(){return  halfSize;}
    public void SetHalfSize(Vector2f halfSize){this.halfSize = halfSize;}
}
