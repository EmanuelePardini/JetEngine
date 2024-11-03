package gameengine.Physics.Components;

import gameengine.Components.Component;
import org.joml.Vector2f;

public class CircleCollider extends Collider
{
    private float radius = 1.0f;

    public float GetRadius(){return  radius;}
    public void SetRadius(float radius){this.radius = radius;}
}
