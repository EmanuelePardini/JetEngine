package gameengine.Components;

import Renderer.Texture;
import org.joml.Vector2f;

public class Sprite
{
    private Texture texture = null;
    private Vector2f[] textCoords =
                {
                        new Vector2f(1,1),
                        new Vector2f(1,0),
                        new Vector2f(0,0),
                        new Vector2f(0,1)
                };

//    public Sprite(Texture texture)
//    {
//        this.texture = texture;
//        Vector2f[] textCoords =
//                {
//                        new Vector2f(1,1),
//                        new Vector2f(1,0),
//                        new Vector2f(0,0),
//                        new Vector2f(0,1)
//                };
//        this.textCoords = textCoords;
//    }
//
//    public Sprite(Texture texture, Vector2f[] textCoords)
//    {
//        this.texture = texture;
//        this.textCoords = textCoords;
//    }

    public Texture GetTexture() {return this.texture;}

    public Vector2f[] GetTextCoords() {return this.textCoords;}

    public void SetTexture(Texture texture)
    {
        this.texture = texture;
    }

    public void SetTexCoords(Vector2f[] textCoords)
    {
        this.textCoords = textCoords;
    }
}
