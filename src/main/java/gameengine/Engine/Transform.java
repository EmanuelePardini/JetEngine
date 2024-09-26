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

    //(TEST KO) I create this functions to test if it get the work done like Copy,
    // but it only reference the same instance, that's not good(USE COPY)
    public Transform GetTransform() {return this;}


    // Creates a copy of the current Transform object and returns it.
    public Transform Copy()
    {
        // Create and return a new Transform instance with the current position and scale.
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    // Copies the position from the current Transform object to another Transform object passed as a parameter.
    public void Copy(Transform to)
    {
        // Set the position of the 'to' Transform to be the same as the current Transform's position.
        to.position.set(this.position); // Copies the position value from the current instance to 'to'.
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o)
    {
        //Pre cast Checks
        if(o == null) return false;
        if(!(o instanceof  Transform)) return false;

        //We can cast now
        Transform t = (Transform)o;
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}
