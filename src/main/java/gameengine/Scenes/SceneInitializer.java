package gameengine.Scenes;

public abstract class SceneInitializer
{
    public abstract void Init(Scene scene);
    public abstract void LoadResources(Scene scene);
    public abstract void ImGui();
}
