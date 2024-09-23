package gameengine.Engine;

public abstract class Component
{
    public GameObject gameObject; //Reference to the Component Owner
    public void Start()
    {

    }
    public abstract void Update(float DeltaTime);
}
