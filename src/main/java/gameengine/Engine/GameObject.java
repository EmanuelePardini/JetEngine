package gameengine.Engine;

import gameengine.Components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    //This is not associated with any specific object, that takes the Id Count in general(static)
    private static int ID_COUNTER = 0;
    //This is the unique Id
    private int uid = -1;

    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

//    public GameObject(String name)
//    {
//        this.name = name;
//        components = new ArrayList<>();
//        this.transform = new Transform();
//        this.zIndex = 0;
//    }

    public  GameObject(String name, Transform transform, int zIndex)
    {
        this.name = name;
        components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
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
            c.ImGUI();
        }
    }

    public int ZIndex() {return zIndex;}

    public int GetUid(){return uid;}

    public static void Init(int maxId){ID_COUNTER = maxId;}

    public List<Component> GetAllComponents(){return this.components;}
}
