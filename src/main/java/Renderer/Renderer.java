package Renderer;

import gameengine.Components.SpriteRenderer; // Importing SpriteRenderer component
import gameengine.Engine.GameObject; // Importing GameObject class from Engine package

import java.util.ArrayList; // Import for using ArrayList
import java.util.List; // Import for using List

public class Renderer
{
    private final int MAX_BATCH_SIZE = 1000; // Maximum number of sprites in a single batch
    private List<RenderBatch> batches; // List to hold multiple RenderBatches

    // Constructor to initialize the Renderer
    public Renderer()
    {
        this.batches = new ArrayList<>(); // Initialize the batches list
    }

    // Method to add a GameObject to the renderer
    public void Add(GameObject go)
    {
        SpriteRenderer spr = go.GetComponent(SpriteRenderer.class); // Retrieve SpriteRenderer component from GameObject
        if(spr != null) // If the GameObject has a SpriteRenderer component
            Add(spr); // Call the overloaded Add method for SpriteRenderer
    }

    // Private method to add a SpriteRenderer to a RenderBatch
    private void Add(SpriteRenderer sprite)
    {
        boolean added = false; // Flag to check if sprite was added
        for(RenderBatch batch : batches) // Iterate through existing batches
        {
            if(batch.HasRoom()) // Check if the batch has room for more sprites
            {
                batch.AddSprite(sprite); // Add the sprite to the batch
                added = true; // Mark as added
                break; // Exit the loop after adding
            }
        }

        // If no batch was found with room, create a new batch
        if(!added)
        {
            RenderBatch newBatch = new RenderBatch((MAX_BATCH_SIZE)); // Create a new RenderBatch
            newBatch.Start(); // Initialize the new batch

            batches.add(newBatch); // Add the new batch to the list of batches
            newBatch.AddSprite(sprite); // Add the sprite to the new batch
        }
    }

    // Method to render all batches
    public void Render()
    {
        for(RenderBatch batch : batches) // Iterate through each batch
        {
            batch.Render(); // Call the render method for each batch
        }
    }
}
