package gameengine.Engine;


import Renderer.Shader;
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
            {/*position*/ 100.5f, -0.5f, 0.0f,     /*color*/ 1.0f, 0.0f,0.0f,1.0f, //Bottom Right 0
             /*position*/ -0.5f, 100.5f, 0.0f,     /*color*/ 0.0f, 1.0f,0.0f,1.0f, //Top Left 1
             /*position*/ 100.5f, 100.5f, 0.0f,     /*color*/ 0.0f, 0.0f ,1.0f,1.0f, //Top Right 2
             /*position*/ -0.5f, -0.5f, 0.0f,     /*color*/ 1.0f, 1.0f,0.0f,1.0f //Bottom Left 3
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

    public LevelEditorScene()
    {

    }

    @Override
    public void Init()
    {
        this.camera = new Camera(new Vector2f());

        defaultShader = new Shader("assets/shaders/default.glsl");

        defaultShader.Compile();
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
        int floatSizeBytes = 4;

        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        //index depends on location in default.glsl variables
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void Update(float dt)
    {
        camera.position.x -= dt * 50.0f;

        defaultShader.Use();
        defaultShader.UploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.UploadMat4f("uView", camera.getViewMatrix());
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
