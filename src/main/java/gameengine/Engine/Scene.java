package gameengine.Engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
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

        }
    }

    public abstract void Update (float DeltaTime);

}
