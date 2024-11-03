package gameengine.Components;

import gameengine.Editor.JetImGui;
import gameengine.Engine.GameObject;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component
{
    //This is not associated with any specific object, that takes the ID Count in general(static)
    private static int ID_COUNTER = 0;
    //This is the unique ID
    private int uid = -1;

    //what happens is that serializations tries to render game object,
    // game object has components and components have game object, and so we get stack overflow.
    // Fix with transient (transient variables are ignored in serialization)
    public transient GameObject gameObject; //Reference to the Component Owner

    public void Start()
    {

    }

    public void Update(float DeltaTime) {}

    public void EditorUpdate(float DeltaTime) {}

    public void ImGui()
    {
        try
        {
            Field[] fields = this.getClass().getDeclaredFields(); //Check the actual class that is running this components
            for(Field field : fields)
            {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if(isTransient) continue; //If is transient we don't want to expose the field

                //It temporarily changes the accessibility of the field
                if(isPrivate) field.setAccessible(true);

                CastField(field); //Reformatted the code, that field cast part is a little huge

                if(isPrivate) field.setAccessible(false); //Then reset accessibility
            }
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    private void CastField(Field field) throws IllegalAccessException
    {
        Class type = field.getType(); //Current field type(variable type)
        Object value = field.get(this); //Value contained in the field
        String name = field.getName(); //Field name(Variable name)

        //INTEGERS
        if(type == int.class)
        {
            int val = (int)value;
            field.set(this, JetImGui.DragInt(name, val));
        }
        //FLOATS
        else if(type == float.class)
        {
            float val = (float)value;
            field.set(this, JetImGui.DragFloat(name, val));
        }
        //BOOLEANS
        else if(type == boolean.class)
        {
            boolean val = (boolean)value;
            if(ImGui.checkbox(name + ": ", val))
            {
                field.set(this, !val);
            }
        }
        //VECTOR 2F
        else if(type == Vector2f.class)
        {
            Vector2f val = (Vector2f)value;
            JetImGui.DrawVec2Control(name, val);
        }
        //VECTOR 3F
        else if(type == Vector3f.class)
        {
            Vector3f val = (Vector3f)value;
            float[] imVec = {val.x, val.y, val.z};
            if(ImGui.dragFloat3(name + ": ", imVec))
            {
                val.set(imVec[0], imVec[1], imVec[2]);
            }
        }
        //VECTOR 4F
        else if(type == Vector4f.class)
        {
            Vector4f val = (Vector4f)value;
            float[] imVec = {val.x, val.y, val.z, val.w};
            if(ImGui.dragFloat3(name + ": ", imVec))
            {
                val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
            }
        }
    }

    public void GenerateId()
    {
        if(this.uid == -1)
            this.uid = ID_COUNTER++;
    }

    public void Destroy() {}

    public int GetUid(){return uid;}

    public static void Init(int maxId){ID_COUNTER = maxId;}
}

