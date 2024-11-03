package gameengine.Scenes;
import gameengine.Components.*;
import gameengine.Engine.*;
import gameengine.Util.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;


public class LevelEditorSceneInitializer  extends SceneInitializer
{
    private Spritesheet sprites;

    private GameObject levelEditorObj;

    public LevelEditorSceneInitializer() {}

    @Override
    public void Init(Scene scene)
    {
        sprites = AssetPool.GetSpritesheet("assets/images/spritesheets/decorationsandblocks.png");
        Spritesheet gizmos = AssetPool.GetSpritesheet("assets/images/gizmos.png");

        levelEditorObj = scene.CreateGameObject("levelEditorObj");
        levelEditorObj.SetSerialize(false);
        levelEditorObj.AddComponent(new MouseControls());
        levelEditorObj.AddComponent(new GridLines());
        levelEditorObj.AddComponent(new EditorCamera(scene.camera()));
        levelEditorObj.AddComponent(new GizmoSystem(gizmos));

        scene.AddGameObjectToScene(levelEditorObj);
    }

    @Override
    public void LoadResources(Scene scene)
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.AddSpritesheet("assets/images/spritesheets/decorationsandblocks.png",
                new Spritesheet(AssetPool.GetTexture("assets/images/spritesheets/decorationsandblocks.png"),
                        16,16, 81, 0));

        AssetPool.AddSpritesheet("assets/images/gizmos.png",
                new Spritesheet(AssetPool.GetTexture("assets/images/gizmos.png"), 24, 48, 3, 0));
        AssetPool.GetTexture("assets/images/blendImage2.png");

        //Go trought each gameobject and riassign the one texture the obj should have checking in the filepath
        for(GameObject g : scene.GetGameObjects())
        {
            if(g.GetComponent(SpriteRenderer.class) != null)
            {
                SpriteRenderer spr = g.GetComponent(SpriteRenderer.class);
                if(spr.GetTexture() != null)
                    spr.SetTexture(AssetPool.GetTexture(spr.GetTexture().GetFilepath()));
            }
        }
    }

    @Override
    public void ImGui()
    {
        ImGui.begin("Level Editor");
        levelEditorObj.ImGUI();
        ImGui.end();

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
                levelEditorObj.GetComponent(MouseControls.class).PickUpObject(object);
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
