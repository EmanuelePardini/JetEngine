package Renderer;

import gameengine.Components.SpriteRenderer;
import gameengine.Engine.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Renderer
{
private final int MAX_BATCH_SIZE = 1000;
private List<RenderBatch> batches;
private static Shader currentShader;

    public Renderer()
    {
        this.batches = new ArrayList<>();
    }

    public void Add(GameObject go)
    {
        SpriteRenderer spr = go.GetComponent(SpriteRenderer.class);
                if(spr != null)
                    Add(spr);
    }

    private void Add(SpriteRenderer sprite)
    {
        boolean added = false;
        for(RenderBatch batch : batches)
        {   //Check if the batch has more space and Add to this batch only if has the same ZIndex
            //That means by now we will divide the Batches per zIndex groups
            if(batch.HasRoom() && batch.ZIndex() == sprite.gameObject.ZIndex())
            {
                Texture tex = sprite.GetTexture();
                //check to avoid missing textures
                if(tex == null || (batch.HasTexture(tex) || batch.HasTextureRoom()))
                {
                    batch.AddSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added)
        {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.ZIndex());
            newBatch.Start();

            batches.add(newBatch);
            newBatch.AddSprite(sprite);
            //That orders the batches by ZIndex using Compare standard Java Library
            Collections.sort(batches);
        }
    }

    public static void BindShader(Shader shader)
    {
        currentShader = shader;
    }
    public static Shader GetBoundShader() {return currentShader;}

    public void Render()
    {
        currentShader.Use();
        for(RenderBatch batch : batches)
        {
            batch.Render();
        }
    }
}
