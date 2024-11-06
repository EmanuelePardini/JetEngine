package gameengine.Components;

import gameengine.Engine.KeyListener;

import java.security.Key;

import static org.lwjgl.glfw.GLFW.*;

public class Movement extends Component {
    float velocity = 250.f;

    @Override
    public void Update(float DeltaTime)
    {
        if(KeyListener.IsKeyPressed(GLFW_KEY_A))
        {
            gameObject.transform.position.x -= velocity * DeltaTime;
            if(!gameObject.GetComponent(SpriteRenderer.class).GetSprite().IsFlipped())
                gameObject.GetComponent(SpriteRenderer.class).GetSprite().FlipHorizontally();
        }

        if(KeyListener.IsKeyPressed(GLFW_KEY_D))
        {
            gameObject.transform.position.x += velocity * DeltaTime;
            if(gameObject.GetComponent(SpriteRenderer.class).GetSprite().IsFlipped())
                gameObject.GetComponent(SpriteRenderer.class).GetSprite().FlipHorizontally();
        }
    }
}
