package gameengine.Physics.Components;

import gameengine.Components.Component;
import org.joml.Vector2f;

public class Collider extends Component
{
    protected Vector2f offset = new Vector2f();

    public Vector2f GetOffset(){return this.offset;}
}
