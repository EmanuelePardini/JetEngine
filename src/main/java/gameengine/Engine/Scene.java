package gameengine.Engine;

import Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected  GameObject activeGameObject = null; //gameobject you are inspecting
    protected boolean levelLoaded = false; //checks if level has been loaded

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

    //Serialize gameobject
    //TODO: bugfixing tutorial#21
    public void SaveExit()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                //It reference the custom class to use it
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer()) //needed to keep components on gameobject
                .create();
        try
        {
            //write gameobject as a json
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Load()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer()) //needed to keep components on gameobject
                .create();

        String inFile = "";

        try
        {
            //read file into string
            inFile = new String(Files.readAllBytes(Paths.get("level.txt"))); //files that are too large can get truncated, fix later
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(!inFile.equals(""))
        {
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for(int i = 0; i<objs.length; i++)
            {
                AddGameObjectToScene(objs[i]);
            }
            this.levelLoaded = true;
        }
    }
}
