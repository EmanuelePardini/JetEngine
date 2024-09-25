package gameengine.Engine;

import Renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene()
    {

    }


    public void Init()
    {

    };

    public void Start()
    {
        for(GameObject go : gameObjects)
        {
            go.Start();
            this.renderer.Add(go);
        }
        isRunning = true;
    }

    public void AddGameObjectToScene(GameObject go)
    {
        if(!isRunning)
        {
            gameObjects.add(go);
        }
        else
        {
            gameObjects.add(go);
            go.Start();
            this.renderer.Add(go);
        }
    }

    public abstract void Update (float DeltaTime);

    public Camera camera()
    {
        return this.camera;
    }

}
