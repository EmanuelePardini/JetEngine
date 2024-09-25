package gameengine.Engine;
import gameengine.Components.SpriteRenderer;
import gameengine.Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class LevelEditorScene extends Scene
{

    public LevelEditorScene()
    {

    }

    @Override
    public void Init()
    {
        this.camera = new Camera(new Vector2f(-250.f, 0));

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100,100),
                                         new Vector2f(256,256)));
        obj1.AddComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/supermario.png")));
        this.AddGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100),
                new Vector2f(256,256)));
        obj2.AddComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/gumba.png")));
        this.AddGameObjectToScene(obj2);

        LoadResources();
    }

    private void LoadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

    }

    @Override
    public void Update(float DeltaTime)
    {
        //System.out.println("FPS: " + (1.0 / DeltaTime));
        for(GameObject go : this.gameObjects)
        {
            go.Update(DeltaTime);
        }

        this.renderer.Render();
    }

}
