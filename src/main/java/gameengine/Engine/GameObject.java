package gameengine.Engine;

import java.util.ArrayList; // Import for using ArrayList
import java.util.List; // Import for using List

public class GameObject
{
    private String name; // Name of the GameObject
    private List<Component> components; // List to hold components attached to the GameObject
    public Transform transform; // Transform component for position, rotation, and scale

    // Constructor to initialize a GameObject with a name
    public GameObject(String name)
    {
        this.name = name; // Set the name of the GameObject
        components = new ArrayList<>(); // Initialize the components list
        this.transform = new Transform(); // Create a new Transform instance
    }

    // Overloaded constructor to initialize a GameObject with a name and a specified Transform
    public GameObject(String name, Transform transform)
    {
        this.name = name; // Set the name of the GameObject
        components = new ArrayList<>(); // Initialize the components list
        this.transform = transform; // Assign the specified Transform instance
    }

    // Generic method to retrieve a component of a specified type
    public <T extends Component> T GetComponent(Class<T> componentClass)
    {
        for (Component c : components) // Iterate through all components
        {
            // Check if the component is of the requested type
            if (componentClass.isAssignableFrom(c.getClass()))
            {
                try
                {
                    return componentClass.cast(c); // Cast and return the component
                } catch (ClassCastException e) // Catch any casting errors
                {
                    e.printStackTrace(); // Print stack trace for debugging
                    assert false : "Error: Casting component"; // Assertion for debugging
                }
            }
        }
        return null; // Return null if no matching component is found
    }

    // Generic method to remove a component of a specified type
    public <T extends Component> void RemoveComponent(Class<T> componentClass)
    {
        for(int i = 0; i < components.size(); i++) // Iterate through components
        {
            Component c = components.get(i);
            // Check if the component is of the requested type
            if(componentClass.isAssignableFrom(c.getClass()))
            {
                components.remove(i); // Remove the component from the list
                return; // Exit the method after removing
            }
        }
    }

    // Method to add a component to the GameObject
    public void AddComponent(Component c)
    {
        this.components.add(c); // Add the component to the list

        c.gameObject = this; // Reference the GameObject in the component
    }

    // Method to update all components in the GameObject
    public void Update(float DeltaTime)
    {
        for(int i = 0; i < components.size(); i++) // Iterate through components
        {
            components.get(i).Update(DeltaTime); // Call the Update method on each component
        }
    }

    // Method to initialize all components in the GameObject
    public void Start()
    {
        for(int i=0; i < components.size(); i++) // Iterate through components
        {
            components.get(i).Start(); // Call the Start method on each component
        }
    }
}
