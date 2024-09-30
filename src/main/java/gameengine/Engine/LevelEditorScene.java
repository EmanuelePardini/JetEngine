package gameengine.Engine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameengine.Components.RigidBody;
import gameengine.Components.Sprite;
import gameengine.Components.SpriteRenderer;
import gameengine.Components.Spritesheet;
import gameengine.Util.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;


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

    public LevelEditorScene()
    {

    }

    @Override
    public void Init()
    {
        LoadResources();
        this.camera = new Camera(new Vector2f(-250.f, 0));

        sprites = AssetPool.GetSpritesheet(SpritesPath[1]);

        if(levelLoaded)
        {
            this.activeGameObject = gameObjects.get(0); //It will get the zeroth gameobject
        }
        else
        {
            AddTestObj(); //MY OBJECT CREATION REFORMATTED
            AddTestObj();
        }

        /* GABE OBJECT CREATION

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
         */
    }


    public void AddTestObj()
    {
        float newX = gameObjects.isEmpty() ? 200 : gameObjects.get(gameObjects.size()-1).transform.position.x + 200;
        float newY = gameObjects.isEmpty() ? 200 : gameObjects.get(gameObjects.size()-1).transform.position.y + 200;

        GameObject obj = new GameObject("Obj" + gameObjects.size(),
                new Transform(new Vector2f(newX, newY),
                new Vector2f(256,256)), gameObjects.size());

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


    @Override
    public void Update(float DeltaTime)
    { //Monitor constantly performances
        // System.out.println("FPS: " + (1.0 / DeltaTime));

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

            float spriteWidth = sprite.GetWidth() * 4;
            float spriteHeight = sprite.GetHeight() * 4;
            int id = sprite.GetTexId();
            Vector2f[] texCoords = sprite.GetTextCoords();

            //That is necessarily because ImGui uses his own IdSystem
            ImGui.pushID(i); //Create ImGui ID to assign to the button just created
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y))
            {
                System.out.println("Button " + i + " clicked");
            }
            ImGui.popID(); //Drop the ImGui Id after assignment

            ImGui.getItemRectMax(lastButtonPos); //It returns the coords of the right low angle

            float lastButtonX2 = lastButtonPos.x; //get the x of the button right low angle
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if(i + 1 < sprites.size() && nextButtonX2 < windowX2) //Check if it exceed the window
                ImGui.sameLine(); //If the button doesn't exceed keep it on the same line
        }
        ImGui.end();
    }
}
