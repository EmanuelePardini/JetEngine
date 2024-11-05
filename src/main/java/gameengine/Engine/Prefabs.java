package gameengine.Engine;

import gameengine.Components.*;
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

    public static GameObject GenerateCharacter(Spritesheet spritesheet, float sizeX, float sizeY)
    {
        GameObject character = GenerateSpriteObject(spritesheet.GetSprite(0), sizeX, sizeY);

        AnimationState run = new AnimationState();
        run.title = "Run";

        float defaultFrameTime = 0.23f;
        run.AddFrame(spritesheet.GetSprite(0), defaultFrameTime);
        run.AddFrame(spritesheet.GetSprite(2), defaultFrameTime);
        run.AddFrame(spritesheet.GetSprite(3), defaultFrameTime);
        run.AddFrame(spritesheet.GetSprite(2), defaultFrameTime);

        run.SetLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.AddState(run);
        stateMachine.SetDefaultState(run.title);
        character.AddComponent(stateMachine);

        return character;
    }
}
