package Renderer;

import gameengine.Components.SpriteRenderer;
import gameengine.Engine.Window;
import gameengine.Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;

public class RenderBatch
{
    //Vertex
    //======
    //Pos                           Color                           Tex coords         Tex ID
    //float, float                  float,float,float,float         float, float       float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXT_COORDS_SIZE = 2;
    private final int TEXT_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORD_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORD_OFFSET + TEXT_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int  VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0,1,2,3,4,5,6,7};

    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize)
    {
        //this way you only you reference it (after creating it in LevelEditor)
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        //4 vertices quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void Start()
    {
        //Generate and bind a Vertex Array Object(VAO)
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = GenerateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEXT_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORD_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXT_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void AddSprite(SpriteRenderer spr)
    {
       //Get index and add renderObject
       int index = this.numSprites;
       this.sprites[index] = spr;
       this.numSprites++;

       if(spr.GetTexture() != null)
       {
           if(!textures.contains(spr.GetTexture()))
           {
               textures.add(spr.GetTexture());
           }
       }

       //Add properties to local vertices array
        LoadVertexProperties(index);

        if(numSprites >= this.maxBatchSize)
        {
            this.hasRoom = false;
        }
    }

    public void Render()
    {
        //For now, rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        //Use shader
        shader.Use();
        shader.UploadMat4f("uProjection", Window.GetScene().camera().GetProjectionMatrix());
        shader.UploadMat4f("uView", Window.GetScene().camera().GetViewMatrix());

        for (int i = 0; i< textures.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).Bind();
        }
        shader.UploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i = 0; i< textures.size(); i++)
        {
            textures.get(i).Unbind();
        }

        shader.Detach();
    }

    private void LoadVertexProperties(int index)
    {
        SpriteRenderer sprite = this.sprites[index];

        //Find offset within array(4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.GetColor();
        Vector2f[] texCoords = sprite.GetTextCoords();

        int texID = 0;
        //Loop through texture until we find the one that matches, then assign its position in the array as the ID (skip 0 to use colors)
        if(sprite.GetTexture() != null)
        {
            for(int i = 0; i < textures.size(); i++)
            {
                if(textures.get(i) == sprite.GetTexture())
                {
                    texID = i + 1;
                    break;
                }
            }
        }


        //Add vertice with the appropriate properties
        float xAdd = 1.f;
        float yAdd = 1.f;

        for(int i = 0; i < 4; i++)
        {
            if(i==1)
                yAdd = 0.f;
            else if(i == 2)
                xAdd = 0.f;
            else if(i == 3)
                yAdd = 1.f;

            //Load position
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset +1 ] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            //Load color
            vertices[offset +2] = color.x;
            vertices[offset +3] = color.y;
            vertices[offset +4] = color.z;
            vertices[offset +5] = color.w;

            //Load texture coordinates
            vertices[offset +6] = texCoords[i].x;
            vertices[offset +7] = texCoords[i].y;

            //Load texture ID
            vertices[offset +8] = texID;

            offset += VERTEX_SIZE;
        }
    }

    private int[] GenerateIndices()
    {
        //6 indices per quad(3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++)
        {
            LoadElementIndices(elements, i);
        }
        return elements;
    }

    private void LoadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;
        //Divide the quad in triangles
        //Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        //Triangle 2
        elements[offsetArrayIndex + 3] = offset; //offset + 0
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean HasRoom()
    {
        return hasRoom;
    }
}
