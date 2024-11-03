package gameengine.Scenes;

import gameengine.Physics.Physics2D;
import gameengine.Renderer.DebugDraw;
import gameengine.Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameengine.Components.Component;
import gameengine.Components.ComponentDeserializer;
import gameengine.Engine.Camera;
import gameengine.Engine.GameObject;
import gameengine.Engine.GameObjectDeserializer;
import gameengine.Engine.Transform;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene
{
    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private SceneInitializer sceneInitializer;
    private Physics2D physics2D;

    public Scene(SceneInitializer sceneInitializer)
    {
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    public void Init()
    {
        this.camera = new Camera(new Vector2f(-250f,0));
        this.sceneInitializer.LoadResources(this);
        this.sceneInitializer.Init(this);
    }

    public void Start()
    {
        for(int i = 0; i < gameObjects.size(); i++)
        {
            GameObject go = gameObjects.get(i);
            go.Start();
            this.renderer.Add(go);
            this.physics2D.Add(go);
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
            this.physics2D.Add(go);
        }
    }

    public void Update (float DeltaTime)
    {
        this.camera.adjustProjection();
        this.physics2D.Update(DeltaTime);

        for(int i = 0; i < gameObjects.size(); i++ )
        {
            GameObject go = gameObjects.get(i);
            go.Update(DeltaTime);

            if(go.IsDead())
            {
                gameObjects.remove(i);
                this.renderer.DestroyGameObject(go);
                this.physics2D.DestroyGameObject(go);
                i--; //If we remove from the array we need to realign index
            }
        }
    }

    public void EditorUpdate(float DeltaTime)
    {
        this.camera.adjustProjection();

        for(int i = 0; i < gameObjects.size(); i++ )
        {
            GameObject go = gameObjects.get(i);
            go.EditorUpdate(DeltaTime);

            if(go.IsDead())
            {
                gameObjects.remove(i);
                this.renderer.DestroyGameObject(go);
                this.physics2D.DestroyGameObject(go);
                i--; //If we remove from the array we need to realign index
            }
        }
    }

    public GameObject CreateGameObject(String name)
    {
        GameObject go = new GameObject(name);
        go.AddComponent(new Transform());
        go.transform = go.GetComponent(Transform.class);
        return go;
    }

    //Serialize gameobject
    public void Save()
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

            //Init an array that contains only the GameObjects to serialize
            List<GameObject> objToSerialize = new ArrayList<>();
            //Populate the array
            for(GameObject obj : this.gameObjects)
            {
                if(obj.ToSerialize())
                    objToSerialize.add(obj);
            }

            writer.write(gson.toJson(objToSerialize));
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
                    //Align the Components count with ID
                    if(c.GetUid() > maxCompId)
                        maxCompId = c.GetUid();
                }

                //Align  the GameObject count with ID
                if(objs[i].GetUid() > maxGoId)
                    maxGoId = objs[i].GetUid();
            }

            maxGoId++; //Make sure is higher by one than the real max to avoid duplicates
            maxCompId++; //Make sure is higher by one than the real max to avoid duplicates
            //System.out.println("objs: " + maxGoId);
            //System.out.println("comps: " + maxCompId);
            GameObject.Init(maxGoId);
            Component.Init(maxCompId);
        }
    }

    public void Destroy()
    {
        for(GameObject go : gameObjects)
        {
            go.Destroy();
        }
    }

    public GameObject GetGameObject(int gameObjectId)
    {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.GetUid() == gameObjectId).findFirst();

        return result.orElse(null);
    }

    public List<GameObject> GetGameObjects() {return this.gameObjects;}

    public void Render(){this.renderer.Render();};

    public Camera camera()
    {
        return this.camera;
    }

    public void ImGUI()
    {
        this.sceneInitializer.ImGui();
    }
}
