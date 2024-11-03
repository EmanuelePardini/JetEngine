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

    public Sprite Copy() {
        Sprite copy = new Sprite();

        // Copy width and height values
        copy.width = this.width;
        copy.height = this.height;

        // Reference the same texture, assuming we want to share it
        copy.texture = this.texture;

        // Create a new array for textCoords and copy each Vector2f element
        copy.textCoords = new Vector2f[this.textCoords.length];
        for (int i = 0; i < this.textCoords.length; i++) {
            copy.textCoords[i] = new Vector2f(this.textCoords[i]); // Creates a new Vector2f with the same values
        }

        return copy;
    }


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
