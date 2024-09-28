package gameengine.Engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    public GameObject(String name)
    {
        this.name = name;
        components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public  GameObject(String name, Transform transform, int zIndex)
    {
        this.name = name;
        components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
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
}
