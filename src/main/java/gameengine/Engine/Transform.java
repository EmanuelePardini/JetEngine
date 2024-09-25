package gameengine.Engine;

import org.joml.Vector2f;

public class Transform
{
    public Vector2f position;
    public Vector2f scale;

    public Transform()
    {
        Init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position){
        Init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale)
    {
        Init(position, scale);
    }

    public void Init(Vector2f position, Vector2f scale)
    {
        this.position = position;
        this.scale = scale;
    }
}
