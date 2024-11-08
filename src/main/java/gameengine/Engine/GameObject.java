package gameengine.Engine;

import gameengine.Components.Component;
import gameengine.Observers.EventSystem;
import gameengine.Observers.Events.Event;
import gameengine.Observers.Events.EventType;
import gameengine.Scenes.Scene;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    //This is not associated with any specific object, that takes the Id Count in general(static)
    private static int ID_COUNTER = 0;
    //This is the unique Id
    private int uid = -1;

    public String name;
    private List<Component> components;
    public transient Transform transform;
    private transient boolean doSerialization = true;
    private boolean isDead = false;

    public  GameObject(String name)
    {
        this.name = name;
        components = new ArrayList<>();
        this.uid = ID_COUNTER++; //TODO: Recheck, it potentially cause problems in the future in the constructor
    }

    public <T extends  Component> T GetComponent(Class<T> componentClass)
    {
        for (Component c : components)
        {
            if (componentClass.isAssignableFrom(c.getClass()))
            {
                try
                {
                    return componentClass.cast(c);
                } catch (ClassCastException e)
                {
                    e.printStackTrace();
                    assert false : "Error: Casting component";
                }
            }
        }
        return null;
    }

    public <T extends Component> void RemoveComponent(Class<T> componentClass)
    {
        for(int i = 0; i < components.size(); i++)
        {
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass()))
            {
                components.remove(i);
                return;
            }
        }
    }

    public void AddComponent(Component c)
    {
        c.GenerateId();
        this.components.add(c);

        c.gameObject = this; //Reference the Component Owner
    }

    public void Update(float DeltaTime)
    {
        for(int i = 0; i < components.size(); i++)
        {
            components.get(i).Update(DeltaTime);
        }
    }

    public void EditorUpdate(float DeltaTime)
    {
        for(int i = 0; i < components.size(); i++)
        {
            components.get(i).EditorUpdate(DeltaTime);
        }
    }

    public void Start()
    {
        for(int i=0; i < components.size(); i++)
        {
            components.get(i).Start();
        }
    }

    public void ImGUI()
    {
        for(Component c : components)
        {
            if(ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.ImGui();
        }
    }

    public static void Init(int maxId){ID_COUNTER = maxId;}

    public void Destroy()
    {
        this.isDead = true;

        for(int i = 0 ; i < components.size(); i++)
        {
            components.get(i).Destroy();
        }
    }

    public GameObject Copy() {
        // Create a new GameObject instance with the same name
        GameObject newGameObject = new GameObject(this.name);

        // Set a new unique ID by incrementing the static ID counter
        newGameObject.uid = ID_COUNTER++;

        // Copy each component from the original to the new instance
        for (Component component : this.components) {
            // Call Copy on each component to get a deep copy
            Component newComponent = component.Copy();
            newGameObject.AddComponent(newComponent);
        }

        // Copy the serialization flag
        newGameObject.doSerialization = this.doSerialization;

        // Handle the transform copy if necessary
        if (this.transform != null)
            newGameObject.transform = this.transform.Copy();  // Assumes Transform has a Copy() method

        return newGameObject;
    }


    public List<Component> GetAllComponents(){return this.components;}
    public int GetUid(){return uid;}
    public boolean IsDead() {return isDead;}
    public void SetSerialize(boolean toSerialize){this.doSerialization = toSerialize;}
    public boolean ToSerialize(){return this.doSerialization;}
}
