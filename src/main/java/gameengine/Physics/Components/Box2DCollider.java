package gameengine.Physics.Components;

import gameengine.Editor.JetImGui;
import gameengine.Renderer.DebugDraw;
import org.joml.Vector2f;

public class Box2DCollider extends Collider
{
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    @Override
    public void Update(float dt)
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(offset.x, offset.y);
        DebugDraw.AddBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }

    @Override
    public void ImGui()
    {
        JetImGui.DrawVec2Control("Half Size", halfSize);
        JetImGui.DrawVec2Control("Origin", origin);
        JetImGui.DrawVec2Control("Offset", offset);
    }

    @Override
    public void EditorUpdate(float DeltaTime)
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.AddBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }

    public Vector2f getHalfSize() {return halfSize;}

    public Vector2f getOrigin() {return this.origin;}
}
