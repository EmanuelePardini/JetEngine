package gameengine.Engine;

import Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameengine.Components.Component;
import gameengine.Components.ComponentDeserializer;
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
            //When first created will throw a "No File exception", don't worry it's ok for now
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(!inFile.equals(""))
        {
            int maxGoId = -1;
            int maxCompId = -1;

            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for(int i = 0; i<objs.length; i++)
            {
                AddGameObjectToScene(objs[i]);

                for(Component c : objs[i].GetAllComponents())
                {
                    //Align the Components count with Id
                    if(c.GetUid() > maxCompId)
                        maxCompId = c.GetUid();
                }

                //Align  the GameObject count with Id
                if(objs[i].GetUid() > maxGoId)
                    maxGoId = objs[i].GetUid();
            }

            maxGoId++; //Make sure is higher by one than the real max to avoid duplicates
            maxCompId++; //Make sure is higher by one than the real max to avoid duplicates
            System.out.println("objs: " + maxGoId);
            System.out.println("comps: " + maxCompId);
            GameObject.Init(maxGoId);
            Component.Init(maxCompId);
            this.levelLoaded = true;
        }
    }
}
