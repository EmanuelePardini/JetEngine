package gameengine.Components;

import gameengine.Renderer.Texture;
import org.joml.Vector2f;

public class Sprite
{
    private float width, height;
    private Texture texture = null;


    private Vector2f[] textCoords =
                {
                        new Vector2f(1,1),
                        new Vector2f(1,0),
                        new Vector2f(0,0),
                        new Vector2f(0,1)
                };


    public Texture GetTexture() {return this.texture;}

    public void SetTexture(Texture texture)
    {
        this.texture = texture;
    }

    public float GetHeight() {return height;}

    public void SetHeight(float height) {this.height = height;}

    public float GetWidth() {return width;}

    public void SetWidth(float width) {this.width = width;}

    public Vector2f[] GetTextCoords() {return textCoords;}
    
    public void SetTextCoords(Vector2f[] textCoords) {this.textCoords = textCoords;}

    public int GetTexId(){
        return texture == null ? -1 : texture.GetTexId();
    }

}
