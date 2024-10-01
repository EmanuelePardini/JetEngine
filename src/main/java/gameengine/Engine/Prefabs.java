package gameengine.Engine;

import gameengine.Components.Sprite;
import gameengine.Components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs
{
    public static GameObject GenerateSpriteObject(Sprite sprite, float sizeX, float sizeY)
    {
        GameObject block = new GameObject("Sprite_Object_Gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.SetSprite(sprite);
        block.AddComponent(renderer);

        return block;
    }
}
