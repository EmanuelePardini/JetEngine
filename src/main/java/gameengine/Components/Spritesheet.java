package gameengine.Components;

import Renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet
{
    private Texture texture;
    private List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWitdth, int spriteHeight, int numSprites, int spacing)
    {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        //bottom left corner of top left sprite
        int currentX = 0;
        int currentY = texture.GetHeight() - spriteHeight;

        for(int i = 0; i < numSprites; i++)
        {
            //getting normalized values
            float topY = (currentY + spriteHeight) / (float) texture.GetHeight();
            float rightX = (currentX + spriteWitdth) / (float) texture.GetWitdh();
            float leftX = currentX / (float) texture.GetWitdh();
            float bottomY = currentY / (float) texture.GetHeight();

            Vector2f[] textCoords =
                    {
                            new Vector2f(rightX,topY),
                            new Vector2f(rightX,bottomY),
                            new Vector2f(leftX,bottomY),
                            new Vector2f(leftX,topY)
                    };

            //Create dinamically our sprite by spritesheet scraping
            Sprite sprite = new Sprite(this.texture, textCoords);
            this.sprites.add(sprite);

            currentX += spriteWitdth + spacing;
            if(currentX >= texture.GetWitdh())
            {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite GetSprite(int index) {return this.sprites.get(index); }

}
