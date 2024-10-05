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

    //This is a Temporarily GameObject to hold all the Level Editor Rules
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

        sprites = AssetPool.GetSpritesheet("assets/images/spritesheets/decorationsandblocks.png");


        if (levelLoaded && !gameObjects.isEmpty())
        {
            this.activeGameObject = gameObjects.get(0); //It will get the zeroth gameobject
        }
        else
        {
            //AddTestObj();
            //AddTestObj();
        }


    }

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

        AssetPool.AddSpritesheet("assets/images/spritesheets/decorationsandblocks.png",
                new Spritesheet(AssetPool.GetTexture("assets/images/spritesheets/decorationsandblocks.png"),
                        16,16, 81, 0));

        //TODO: Fix and review asset pool
        AssetPool.GetTexture("assets/images/blendImage2.png");

        //Go trought each gameobject and riassign the one texture the obj should have checking in the filepath
        for(GameObject g : gameObjects)
        {
            if(g.GetComponent(SpriteRenderer.class) != null)
            {
                SpriteRenderer spr = g.GetComponent(SpriteRenderer.class);
                if(spr.GetTexture() != null)
                    spr.SetTexture(AssetPool.GetTexture(spr.GetTexture().GetFilepath()));
            }
        }
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

    float x = 0.0f;
    float y = 0.0f;
    float angle = 0.0f;
    @Override
    public void Update(float DeltaTime)
    {
        //Monitor constantly performances
        // System.out.println("FPS: " + (1.0 / DeltaTime));

        //TestDebugLines();
        DebugDraw.AddBox2D(new Vector2f(800,500), new Vector2f(64,32), angle, new Vector3f(0,1,0),1);
        DebugDraw.AddCircle2D(new Vector2f(x,y), 64, new Vector3f(0,1,0),1);
        x += 50f * DeltaTime;
        y += 50f * DeltaTime;
        angle += 40 * DeltaTime;

        //mouseControls.Update(DeltaTime);
        levelEditorStuff.Update(DeltaTime);

        for(GameObject go : this.gameObjects)
        {
            go.Update(DeltaTime);
        }
    }

    @Override
    public void Render()
    {
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
