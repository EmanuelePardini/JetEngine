package gameengine.Engine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameengine.Components.RigidBody;
import gameengine.Components.Sprite;
import gameengine.Components.SpriteRenderer;
import gameengine.Components.Spritesheet;
import gameengine.Util.AssetPool;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;


public class LevelEditorScene extends Scene
{
    private GameObject obj1;
    private Spritesheet sprites;

    //That's another of my test
    private String[] SpritesPath = {
            "assets/images/spritesheet.png",
            "assets/images/blendImage1.png",
            "assets/images/blendImage2.png"
    };

    public LevelEditorScene()
    {

    }

    @Override
    public void Init()
    {
        LoadResources();
        this.camera = new Camera(new Vector2f(-250.f, 0));

        if(levelLoaded)
        {
            this.activeGameObject = gameObjects.get(0); //It will get the zeroth gameobject
            return;
        }

        sprites = AssetPool.GetSpritesheet(SpritesPath[0]);

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200,100),
                                         new Vector2f(256,256)), -1);

        //temp solution for current setup, will be later edited via editor
        SpriteRenderer obj1Sprite = new SpriteRenderer();
        obj1Sprite.SetColor(new Vector4f(1,0,0,1));

        obj1.AddComponent(obj1Sprite);
        obj1.AddComponent(new RigidBody());
        this.AddGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100),
                new Vector2f(256,256)), 2);

        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();

        obj2Sprite.SetTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        obj2SpriteRenderer.SetSprite(obj2Sprite);

        obj2.AddComponent(obj2SpriteRenderer);
        this.AddGameObjectToScene(obj2);

        this.activeGameObject = obj1;
    }

    private void LoadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.AddSpritesheet(SpritesPath[0],
                new Spritesheet(AssetPool.getTexture(SpritesPath[0]),
                        16,16, 26, 0));

        //TODO: Fix and review asset pool
        AssetPool.getTexture("assets/images/blendImage2.png");
    }


    @Override
    public void Update(float DeltaTime)
    { //Monitor constantly performances
        // System.out.println("FPS: " + (1.0 / DeltaTime));



        MoveCamera(DeltaTime); //Move Camera test

        for(GameObject go : this.gameObjects)
        {
            go.Update(DeltaTime);
        }

        this.renderer.Render();
    }

    @Override
    public void ImGUI()
    {
        ImGui.begin("Test window");
        ImGui.text("Some random text");
        ImGui.end();
    }

    //That's one of my tests Giovanni, if you want to remove you can
    //KEEP CALM
    public void MoveCamera(float DeltaTime)
    {
        if(KeyListener.IsKeyPressed(GLFW_KEY_RIGHT))
            camera.position.x += 100f * DeltaTime;
        else if(KeyListener.IsKeyPressed(GLFW_KEY_LEFT))
            camera.position.x -= 100f * DeltaTime;
        else if(KeyListener.IsKeyPressed(GLFW_KEY_UP))
            camera.position.y += 100f * DeltaTime;
        else if(KeyListener.IsKeyPressed(GLFW_KEY_DOWN))
            camera.position.y -= 100f * DeltaTime;
    }
}
