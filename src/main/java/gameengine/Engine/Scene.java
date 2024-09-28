package gameengine.Engine;

import Renderer.Renderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected  GameObject activeGameObject = null; //gameobject you are inspecting

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

    public void SceneImGUI()
    {
        if(activeGameObject != null)
        {
            ImGui.begin("Inspector");
            activeGameObject.ImGUI();
            ImGui.end();
        }

        ImGUI();
    }

    public void ImGUI()
    {

    }
}
