package gameengine.Engine;

public abstract class Component
{
    //what happens is that serializations tries to render gameobject,
    // gameobject has components and components have gameobject, and so we get stack overflow.
    // Fix with transient (transient variables are ignored in serialization)
    public transient GameObject gameObject; //Reference to the Component Owner

    public void Start()
    {

    }
    public void Update(float DeltaTime)
    {

    }

    public void ImGUI()
    {

    }
}
