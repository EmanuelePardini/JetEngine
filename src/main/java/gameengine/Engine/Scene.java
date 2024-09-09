package gameengine.Engine;

public abstract class Scene
{
    protected Camera camera;

    public Scene(){}


    public void Init(){};
    public abstract void Update (float dt);

}
