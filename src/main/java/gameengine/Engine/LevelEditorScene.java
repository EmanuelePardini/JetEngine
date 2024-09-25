package gameengine.Engine;
import gameengine.Components.SpriteRenderer;
import gameengine.Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene
{

    public LevelEditorScene()
    {

    }

    @Override
    public void Init()
    {
        this.camera = new Camera(new Vector2f(-250.f, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.f;
        float sizeY = totalHeight / 100.f;

        for(int x=0; x < 100; x++)
        {
            for(int y=0; y < 100; y++)
            {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);


                GameObject go = new GameObject("Obj" + x + y , new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.AddComponent(new SpriteRenderer(new Vector4f(xPos /totalWidth, yPos / totalHeight,1 ,1)));
                this.AddGameObjectToScene(go);
            }
        }

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
