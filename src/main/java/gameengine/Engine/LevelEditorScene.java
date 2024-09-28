package gameengine.Engine;
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

        sprites = AssetPool.GetSpritesheet(SpritesPath[0]);

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200,100),
                                         new Vector2f(256,256)), -1);
        obj1.AddComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
        this.AddGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100),
                new Vector2f(256,256)), 2);
        obj2.AddComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesPath[2]))));
        this.AddGameObjectToScene(obj2);

        this.activeGameObject = obj1;

    }

    private void LoadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.AddSpritesheet(SpritesPath[0],
                new Spritesheet(AssetPool.getTexture(SpritesPath[0]),
                        16,16, 26, 0));
    }

    /*ANIMATION TEST(Dirty flag pattern Video)
    Feel free to remove that
    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;
    */

    @Override
    public void Update(float DeltaTime)
    { //Monitor constantly performances
        System.out.println("FPS: " + (1.0 / DeltaTime));

        /*ANIMATION TEST(Dirty flag pattern Video)
        Feel free to remove that

        spriteFlipTimeLeft -= DeltaTime;
        if(spriteFlipTimeLeft <= 0)
        {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if(spriteIndex > 4)
            {
                spriteIndex = 0;
            }
            obj1.GetComponent(SpriteRenderer.class).SetSprite(sprites.GetSprite(spriteIndex));
        }

        obj1.transform.position.x += 10* DeltaTime;

        */

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
