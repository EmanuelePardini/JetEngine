package gameengine.Scenes;
import Renderer.DebugDraw;
import gameengine.Components.*;
import gameengine.Engine.*;
import gameengine.Util.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import kotlin.random.URandomKt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;


public class LevelEditorScene extends Scene
{
    private Spritesheet sprites;

    //That's another of my test
    private String[] SpritesPath = {
            "assets/images/spritesheets/spritesheet.png",
            "assets/images/spritesheets/decorationsandblocks.png",
            "assets/images/blendImage1.png",
            "assets/images/blendImage2.png"
    };

    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0); //for testing editor comps

    public LevelEditorScene()
    {

    }

    @Override
    public void Init()
    {
        levelEditorStuff.AddComponent(new MouseControls());
        levelEditorStuff.AddComponent(new GridLines());

        LoadResources();
        this.camera = new Camera(new Vector2f(-250.f, 0));

        sprites = AssetPool.GetSpritesheet(SpritesPath[1]);


        if (levelLoaded)
        {
            this.activeGameObject = gameObjects.get(0); //It will get the zeroth gameobject
        }
        else
        {
            //AddTestObj();
            //AddTestObj();
        }


    }
        /* TODO: Check if still needed
        else
        {
            AddTestObj(); //MY OBJECT CREATION REFORMATTED
            AddTestObj();
        }

         GABE OBJECT CREATION

                //OBJ1
        GameObject obj1 = new GameObject("Object 1",
                new Transform(new Vector2f(200,100),
                        new Vector2f(256,256)), -1);

        //temp solution for current setup, will be later edited via editor
        SpriteRenderer obj1Sprite = new SpriteRenderer();
        obj1Sprite.SetColor(new Vector4f(1,0,0,1));

        obj1.AddComponent(obj1Sprite);
        obj1.AddComponent(new RigidBody());
        this.AddGameObjectToScene(obj1);


        //OBJ 2
        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400,100),
                        new Vector2f(256,256)), 2);

        SpriteRenderer obj2Sprite = new SpriteRenderer();
        obj2Sprite.SetColor(new Vector4f(0,0,1,0.5f));

        obj2.AddComponent(obj2Sprite);
        obj2.AddComponent(new RigidBody());
        this.AddGameObjectToScene(obj2);

        this.activeGameObject = obj1;

    }
*/

    //MY OBJECT CREATION REFORMATTED(Only for Test purpose)
    public void AddTestObj()
    {
        float newX = gameObjects.isEmpty() ? 200 : gameObjects.get(gameObjects.size()-1).transform.position.x + 200;
        float newY = gameObjects.isEmpty() ? 200 : gameObjects.get(gameObjects.size()-1).transform.position.y + 200;

        GameObject obj = new GameObject("Obj",
                new Transform(new Vector2f(newX, newY),
                        new Vector2f(256,256)), -1);

        //temp solution for current setup, will be later edited via editor
        SpriteRenderer obj1Sprite = new SpriteRenderer();
        obj1Sprite.SetColor(new Vector4f(1,0,0,1));

        obj.AddComponent(obj1Sprite);
        obj.AddComponent(new RigidBody());
        this.AddGameObjectToScene(obj);

        this.activeGameObject = obj;
    }

    private void LoadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.AddSpritesheet(SpritesPath[1],
                new Spritesheet(AssetPool.getTexture(SpritesPath[1]),
                        16,16, 81, 0));

        //TODO: Fix and review asset pool
        AssetPool.getTexture("assets/images/blendImage2.png");
    }


    //You can remove, made only for testing
//    float t = 0.0f;
//    private void TestDebugLines()
//    {
//        float x = ((float)Math.sin(t) * 200.f) + 600.f;
//        float y = ((float)Math.cos(t) * 200.f) + 400.f;
//        t += 0.05f;
//        DebugDraw.AddLine2D(new Vector2f(Window.GetWidth() / 2, Window.GetHeight() / 2), new Vector2f(x, y), new Vector3f(0,0,1), 10);
//    }

    @Override
    public void Update(float DeltaTime)
    {
        //Monitor constantly performances
        // System.out.println("FPS: " + (1.0 / DeltaTime));

        //TestDebugLines();

        //mouseControls.Update(DeltaTime);
        levelEditorStuff.Update(DeltaTime);

        for(GameObject go : this.gameObjects)
        {
            go.Update(DeltaTime);
        }

        this.renderer.Render();
    }

    @Override
    public void ImGUI()
    {
        ImGui.begin("BlocksList");

        //Var decl
        ImVec2 windowPos = new ImVec2();
        ImVec2 windowSize = new ImVec2();
        ImVec2 itemSpacing = new ImVec2();
        ImVec2 lastButtonPos = new ImVec2();

        ImGui.getWindowPos(windowPos);
        ImGui.getWindowSize(windowSize);
        ImGui.getStyle().getItemSpacing(itemSpacing);

        //low right angle of the current ImGui window
        float windowX2 = windowPos.x + windowSize.x;

        for(int i = 0; i < sprites.size(); i++)
        { //For any sprite create a button in the BlocksList ImGui
            Sprite sprite = sprites.GetSprite(i);

            float spriteWidth = sprite.GetWidth() * 2;
            float spriteHeight = sprite.GetHeight() * 2;
            int id = sprite.GetTexId();
            Vector2f[] texCoords = sprite.GetTextCoords();

            //That is necessarily because ImGui uses his own IdSystem
            ImGui.pushID(i); //Create ImGui ID to assign to the button just created
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
            {
                //System.out.println("Button " + i + " clicked");
                GameObject object = Prefabs.GenerateSpriteObject(sprite, spriteWidth, spriteHeight);

                //Attach this to the mouse cursor
                levelEditorStuff.GetComponent(MouseControls.class).PickUpObject(object);
            }
            ImGui.popID(); //Drop the ImGui ID after assignment

            ImGui.getItemRectMax(lastButtonPos); //It returns the coords of the right low angle

            float lastButtonX2 = lastButtonPos.x; //get the x of the button right low angle
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if(i + 1 < sprites.size() && nextButtonX2 < windowX2) //Check if it exceeds the window
                ImGui.sameLine(); //If the button doesn't exceed keep it on the same line
        }
        ImGui.end();
    }
}
