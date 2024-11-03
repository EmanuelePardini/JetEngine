package gameengine.Engine;

import gameengine.Components.Component;
import gameengine.Editor.JetImGui;
import org.joml.Vector2f;

public class Transform extends Component
{
    public Vector2f position;
    public Vector2f scale;
    public float rotation = 0.0f;
    public int zIndex;

    public Transform()
    {
        Init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position){
        Init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale)
    {
        Init(position, scale);
    }

    public void Init(Vector2f position, Vector2f scale)
    {
        this.position = position;
        this.scale = scale;
    }

    // Creates a copy of the current Transform object and returns it.
    public Transform Copy()
    {
        Vector2f newPosition = new Vector2f(this.position).add(this.scale);
        return new Transform(newPosition, new Vector2f(this.scale));
    }

    // Copies the position from the current Transform object to another Transform object passed as a parameter.
    public void Copy(Transform to)
    {
        // Set the position of the 'to' Transform to be the same as the current Transform's position.
        to.position.set(this.position); // Copies the position value from the current instance to 'to'.
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o)
    {
        //Pre cast Checks
        if(o == null) return false;
        if(!(o instanceof  Transform)) return false;

        //We can cast now
        Transform t = (Transform)o;

        return t.position.equals(this.position)
                && t.scale.equals(this.scale)
                && t.rotation == this.rotation
                && t.zIndex == this.zIndex;
    }

    @Override
    public void ImGui()
    {
        gameObject.name = JetImGui.InputText("Name", gameObject.name);
        JetImGui.DrawVec2Control("Position", this.position);
        JetImGui.DrawVec2Control("Scale", this.scale, 32.0f);
        this.rotation = JetImGui.DragFloat("Rotation", this.rotation);
        this.zIndex = JetImGui.DragInt("Z-Index", this.zIndex);
    }
}
