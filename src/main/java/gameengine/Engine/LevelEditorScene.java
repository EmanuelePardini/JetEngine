package gameengine.Engine;


import Renderer.Shader;
import Renderer.Texture;
import gameengine.Util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene
{
    //define vertexes and respective colors (for a square)
    private float[] vertexArray =
            {/*position*/ 100.5f, -0.5f, 0.0f,     /*color*/ 1.0f, 0.0f,0.0f,1.0f, /*UV*/ 1, 0, //Bottom Right 0
                    /*position*/ -0.5f, 100.5f, 0.0f,     /*color*/ 0.0f, 1.0f,0.0f,1.0f, /*UV*/0,1, //Top Left 1
                    /*position*/ 100.5f, 100.5f, 0.0f,     /*color*/ 0.0f, 0.0f ,1.0f,1.0f, /*UV*/1,1, //Top Right 2
                    /*position*/ -0.5f, -0.5f, 0.0f,     /*color*/ 1.0f, 1.0f,0.0f,1.0f,  /*UV*/ 0,0 //Bottom Left 3
    };

    //IMPORTANT: Must be in counter-clockwise order, defines two triangles at top right and bottom left of the square
    private int[] elementArray = {
        /*
            x1    x2

            x3    x0
         */
        2,1,0, //Top right triangle
        0,1,3 //Bottom left triangle
    };

    //VAO (Vertex Array Object) Holds the configuration of which VBOs and EBOs are used and how the vertex data is linked to vertex attributes.
    //VBO (Vertex Buffer Object) Stores your vertex data (like positions, colors, etc.).
    //EBO (Element Buffer Object) Stores indices to reuse vertex data (especially useful for complex shapes).
    private int vaoId, vboId, eboId;

    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditorScene()
    {

    }

    @Override
    public void Init()
    {
        this.camera = new Camera(new Vector2f());

        camera.position.y -=  100.0f;
        camera.position.x -=  100.0f;

        defaultShader = new Shader("assets/shaders/default.glsl");

        defaultShader.Compile();

        this.testTexture = new Texture("assets/images/testImage.jpg");
        SendBuffer();
    }

    private void SendBuffer()
    {
        //Generate VAO, VBO and EBO buffer object to send to the GPU
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Create a float buffer of vertices
        //A FloatBuffer is a buffer that can hold floating-point numbers (float) and is used to efficiently pass data to OpenGL.
        // The createFloatBuffer(int capacity) method in BufferUtils creates a buffer that can contain capacity elements of type float.
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        //Flips this buffer. The limit is set to the current position and then the position is set to zero. If the mark is defined then it is discarded.
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboId = glGenBuffers();
        //Bind it to the ID
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        //Send specific buffer data, only drawing statically, not changing what we draw
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        //Generate, bind and make static element buffer
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        //index depends on location in default.glsl variables
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);


        /* Defines the vertex attribute with index 2 (for example, UV coordinates for a texture).
        uvSize specifies the number of components for this attribute.
        GL_FLOAT indicates that each component is a float (floating-point value).
        false means the data should not be normalized (no need to map values to a specific range).
        vertexSizeBytes is the stride, i.e., the total size in bytes of a single vertex.
        (positionSize + colorSize) * Float.BYTES calculates the offset in bytes from the start of the vertex data
        to the beginning of the UV attribute data (skipping the position and color data). */
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void Update(float dt)
    {
        //camera.position.y -= dt * 20.0f;
        //camera.position.x -= dt * 50.0f;

        defaultShader.Use();

        //Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.Bind();

        defaultShader.UploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.UploadMat4f("uView", camera.getViewMatrix());
        defaultShader.UploadFloat("uTime", Time.getTime());

        //Bind the VAO that we're using
        glBindVertexArray(vaoId);

        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        
        defaultShader.Detach();
    }

}
