package Renderer;

import gameengine.Components.SpriteRenderer; // Importing SpriteRenderer component
import gameengine.Engine.Window; // Importing the Window class from the Engine package
import gameengine.Util.AssetPool; // Importing AssetPool for shader management
import org.joml.Vector2f; // Import for 2D vectors
import org.joml.Vector4f; // Import for 4D vectors

import java.util.ArrayList; // Import for using ArrayList
import java.util.List; // Import for using List

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray; // Import for binding Vertex Array Objects
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays; // Import for generating Vertex Array Objects
import static org.lwjgl.opengl.GL15C.*; // Import for OpenGL buffer functions
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray; // Import for enabling vertex attribute arrays
import static org.lwjgl.opengl.GL20.glVertexAttribPointer; // Import for specifying vertex attribute pointers
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray; // Import for disabling vertex attribute arrays

public class RenderBatch
{
    // Vertex Structure
    //==================
    // Position                           Color                            Texture Coordinates         Texture ID
    // float, float                       float, float, float, float      float, float               float
    // Define sizes for vertex attributes
    // Each vertex consists of position, color, texture coordinates, and texture ID
    private final int POS_SIZE = 2; // Size of position (x, y)
    private final int COLOR_SIZE = 4; // Size of color (r, g, b, a)
    private final int TEXT_COORDS_SIZE = 2; // Size of texture coordinates (u, v)
    private final int TEXT_ID_SIZE = 1; // Size of texture ID

    // Calculate offsets for each attribute in the vertex structure
    private final int POS_OFFSET = 0; // Offset for position data
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES; // Offset for color data
    private final int TEX_COORD_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES; // Offset for texture coordinates
    private final int TEX_ID_OFFSET = TEX_COORD_OFFSET + TEXT_COORDS_SIZE * Float.BYTES; // Offset for texture ID
    private final int VERTEX_SIZE = 9; // Total number of floats per vertex (2+4+2+1)
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES; // Total size in bytes for each vertex

    private SpriteRenderer[] sprites; // Array to hold sprites to be rendered
    private int numSprites; // Current number of sprites in the batch
    private boolean hasRoom; // Flag to check if there is room for more sprites in the batch
    private float[] vertices; // Array to hold vertex data for all sprites
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7}; // Array for texture slots to hold multiple textures

    private List<Texture> textures; // List to manage textures used in the batch
    private int vaoID, vboID; // IDs for the Vertex Array Object and Vertex Buffer Object
    private int maxBatchSize; // Maximum number of sprites that can be batched
    private Shader shader; // Shader used for rendering


    // Constructor to initialize the RenderBatch
    public RenderBatch(int maxBatchSize)
    {
        shader = AssetPool.getShader("assets/shaders/default.glsl"); // Load shader from AssetPool
        this.sprites = new SpriteRenderer[maxBatchSize]; // Initialize the sprite array
        this.maxBatchSize = maxBatchSize;

        // 4 vertices per quad (sprite)
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE]; // Allocate space for vertices

        this.numSprites = 0; // Initialize number of sprites
        this.hasRoom = true; // Initially, there is room for sprites
        this.textures = new ArrayList<>(); // Initialize the texture list
    }

    // Method to start the RenderBatch, creating necessary OpenGL objects
    public void Start()
    {
        vaoID = glGenVertexArrays(); // Generate a Vertex Array Object
        glBindVertexArray(vaoID); // Bind the Vertex Array Object

        // Allocate space for vertices in a Vertex Buffer Object
        vboID = glGenBuffers(); // Generate a Vertex Buffer Object
        glBindBuffer(GL_ARRAY_BUFFER, vboID); // Bind the Vertex Buffer Object
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW); // Allocate space for vertex data

        // Create and upload indices buffer
        int eboID = glGenBuffers(); // Generate an Element Buffer Object
        int[] indices = GenerateIndices(); // Generate indices for quads
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID); // Bind the Element Buffer Object
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); // Allocate space for indices

        // Enable vertex attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET); // Position
        glEnableVertexAttribArray(0); // Enable position attribute

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET); // Color
        glEnableVertexAttribArray(1); // Enable color attribute

        glVertexAttribPointer(2, TEXT_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORD_OFFSET); // Texture Coordinates
        glEnableVertexAttribArray(2); // Enable texture coordinate attribute

        glVertexAttribPointer(3, TEXT_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET); // Texture ID
        glEnableVertexAttribArray(3); // Enable texture ID attribute
    }

    // Method to add a sprite to the batch
    public void AddSprite(SpriteRenderer spr)
    {
        // Get index and add the sprite to the array
        int index = this.numSprites;
        this.sprites[index] = spr; // Add the sprite to the array
        this.numSprites++; // Increment the sprite count

        // Check if the sprite has a texture
        if(spr.GetTexture() != null)
        {
            // If the texture is not already in the list, add it
            if(!textures.contains(spr.GetTexture()))
            {
                textures.add(spr.GetTexture());
            }
        }

        // Load vertex properties into the local vertices array
        LoadVertexProperties(index); // Load properties for this sprite

        // Check if there's room for more sprites
        if(numSprites >= this.maxBatchSize)
        {
            this.hasRoom = false; // Set flag to false if max batch size is reached
        }
    }

    // Method to render all sprites in the batch
    public void Render()
    {
        // For now, rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID); // Bind the Vertex Buffer Object
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices); // Update vertex data in the buffer

        // Use the shader for rendering
        shader.Use();
        shader.UploadMat4f("uProjection", Window.GetScene().camera().GetProjectionMatrix()); // Upload projection matrix
        shader.UploadMat4f("uView", Window.GetScene().camera().GetViewMatrix()); // Upload view matrix

        // Bind each texture used in the batch
        for (int i = 0; i < textures.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1); // Activate texture slot
            textures.get(i).Bind(); // Bind the texture
        }
        shader.UploadIntArray("uTextures", texSlots); // Upload texture slots to the shader

        // Bind the Vertex Array Object for drawing
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0); // Enable position attribute
        glEnableVertexAttribArray(1); // Enable color attribute

        // Draw the sprites
        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0); // Draw the elements using indices

        // Disable the vertex attribute arrays
        glDisableVertexAttribArray(0); // Disable position attribute
        glDisableVertexAttribArray(1); // Disable color attribute
        glBindVertexArray(0); // Unbind the Vertex Array Object

        // Unbind all textures
        for (int i = 0; i < textures.size(); i++)
        {
            textures.get(i).Unbind(); // Unbind the texture
        }

        shader.Detach(); // Detach the shader after rendering
    }

    // Method to load vertex properties for a given sprite index
    private void LoadVertexProperties(int index)
    {
        SpriteRenderer sprite = this.sprites[index]; // Get the sprite to load properties from

        // Calculate offset in the vertex array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.GetColor(); // Get the color of the sprite
        Vector2f[] texCoords = sprite.GetTextCoords(); // Get the texture coordinates of the sprite

        int texID = 0; // Initialize texture ID
        // Loop through textures to find the correct texture and assign its position in the array
        if(sprite.GetTexture() != null)
        {
            for(int i = 0; i < textures.size(); i++)
            {
                if(textures.get(i) == sprite.GetTexture())
                {
                    texID = i + 1; // Set texture ID to the index in the array (offset by 1)
                    break; // Break the loop once the texture is found
                }
            }
        }

        // Load vertices with the appropriate properties
        float xAdd = 1.f; // Increment for x coordinate
        float yAdd = 1.f; // Increment for y coordinate

        for(int i = 0; i < 4; i++)
        {
            // Determine vertex position based on the iteration
            if(i == 1)
                yAdd = 0.f; // Bottom-left corner
            else if(i == 2)
                xAdd = 0.f; // Bottom-right corner
            else if(i == 3)
                yAdd = 1.f; // Top-right corner

            // Load position
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            // Load color
            vertices[offset + 2] = color.x; // Red
            vertices[offset + 3] = color.y; // Green
            vertices[offset + 4] = color.z; // Blue
            vertices[offset + 5] = color.w; // Alpha

            // Load texture coordinates
            vertices[offset + 6] = texCoords[i].x; // U coordinate
            vertices[offset + 7] = texCoords[i].y; // V coordinate

            // Load texture ID
            vertices[offset + 8] = texID; // Texture ID

            offset += VERTEX_SIZE; // Move to the next vertex offset
        }
    }

    // Method to generate indices for each sprite in the batch
    private int[] GenerateIndices()
    {
        // 6 indices per quad (2 triangles per quad)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++)
        {
            LoadElementIndices(elements, i); // Load indices for each sprite
        }
        return elements; // Return the generated indices
    }

    // Method to load element indices for a specific sprite index
    private void LoadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = 6 * index; // Calculate offset for this index
        int offset = 4 * index; // Calculate offset for vertices

        // Define the triangles for the sprite
        elements[offsetArrayIndex] = offset + 3; // Triangle 1: Top right
        elements[offsetArrayIndex + 1] = offset + 2; // Triangle 1: Bottom right
        elements[offsetArrayIndex + 2] = offset; // Triangle 1: Top left

        elements[offsetArrayIndex + 3] = offset; // Triangle 2: Top left
        elements[offsetArrayIndex + 4] = offset + 2; // Triangle 2: Bottom right
        elements[offsetArrayIndex + 5] = offset + 1; // Triangle 2: Bottom left
    }

    // Method to check if there's room for more sprites
    public boolean HasRoom()
    {
        return hasRoom; // Return the status of room availability
    }
}
