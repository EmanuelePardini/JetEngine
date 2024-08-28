package gameengine.Engine;


import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene
{
    //View explanation on Assets/Shaders/default.glsl

    private String VertexShaderSrc =  "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String FragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId, fragmentId, shaderProgram;

    private float[] vertexArray =
            {/*position*/ 0.5f, -0.5f, 0.0f,     /*color*/ 1.0f, 0.0f,0.0f,1.0f, //Bottom Right 0
             /*position*/ -0.5f, 0.5f, 0.0f,     /*color*/ 0.0f, 1.0f,0.0f,1.0f, //Top Left 1
             /*position*/ 0.5f, 0.5f, 0.0f,     /*color*/ 0.0f, 0.0f ,1.0f,1.0f, //Top Right 2
             /*position*/ -0.5f, -0.5f, 0.0f,     /*color*/ 1.0f, 1.0f,0.0f,1.0f //Bottom Left 3
    };

    //IMPORT: Must be in counter-clockwise order
    private int[] elementArray = {
        /*
            x1    x2

            x3    x0
         */
        2,1,0, //Top right triangle
        0,1,3 //Bottom left triangle


    };

    private int vaoId, vboId, eboId;

    public LevelEditorScene()
    {
    }

    @Override
    public void Init()
    {
        CompileShaders();
        SendBuffer();
    }

    private void CompileShaders()
    {
        //Compile and Link Shaders
        //Load and compile the Vertex Shaders
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(vertexId, VertexShaderSrc);
        glCompileShader(vertexId);
        //Check for compilation errors
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            assert false : ""; //This will stop the program
        }

        //Load and compile the Fragment Shaders
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(fragmentId, FragmentShaderSrc);
        glCompileShader(fragmentId);
        //Check for compilation errors
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Fragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            assert false : ""; //This will stop the program
        }

        //Link Shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        //Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Linking shader compilation failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : ""; //This will stop the program
        }
    }

    private void SendBuffer()
    {
        //Generate VAO, VBO and EBO buffer object to send to the GPU
        //VAO (Vertex Array Object) Holds the configuration of which VBOs and EBOs are used and how the vertex data is linked to vertex attributes.
        //VBO (Vertex Buffer Object) Stores your vertex data (like positions, colors, etc.).
        //EBO (Element Buffer Object) Stores indices to reuse vertex data (especially useful for complex shapes).
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
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void Update(float dt)
    {
        //Bind Shader Program
        glUseProgram(shaderProgram);
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

        glUseProgram(0);
    }

}
