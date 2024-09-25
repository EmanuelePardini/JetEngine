package Renderer;

import gameengine.Components.SpriteRenderer;
import gameengine.Engine.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer
{
private final int MAX_BATCH_SIZE = 1000;
private List<RenderBatch> batches;

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
        {
            if(batch.HasRoom())
            {
                batch.AddSprite(sprite);
                added = true;
                break;
            }
        }

        if(!added)
        {
            RenderBatch newBatch = new RenderBatch((MAX_BATCH_SIZE));
            newBatch.Start();

            batches.add(newBatch);
            newBatch.AddSprite(sprite);
        }
    }

    public void Render()
    {
        for(RenderBatch batch : batches)
        {
            batch.Render();
        }
    }
}
