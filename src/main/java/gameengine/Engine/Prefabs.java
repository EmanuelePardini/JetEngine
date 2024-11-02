package gameengine.Engine;

import gameengine.Components.Sprite;
import gameengine.Components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs
{
    public static GameObject GenerateSpriteObject(Sprite sprite, float sizeX, float sizeY)
    {
        GameObject block = Window.GetScene().CreateGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.SetSprite(sprite);
        block.AddComponent(renderer);

        return block;
    }
}
