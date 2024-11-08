package gameengine.Renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D
{
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private float lifetime;

    public Line2D(Vector2f to, Vector2f from, Vector3f color, float lifetime)
    {
        this.to = to;
        this.from = from;
        this.color = color;
        this.lifetime = lifetime;
    }

    public float BeginFrame()
    {
        this.lifetime--;
        return this.lifetime;
    }

    public Vector3f GetColor()
    {
        return color;
    }

    public Vector2f GetTo()
    {
        return to;
    }

    public Vector2f GetFrom()
    {
        return from;
    }

    /*
    public void Destroy()
    {
        from = new Vector2f(0,0);
        to = new Vector2f(0,0);
        color = new Vector3f(0,0,0);
        lifetime = 0;
    }
    */

}
