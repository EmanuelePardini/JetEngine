package Renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader
{

    private int shaderProgramId;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath)
    {
        this.filepath = filepath;

        try
        {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)"); //RegEx: Regular Expressions

            //Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            //Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equals("vertex"))
            {
                vertexSource = splitString[1];
            }
            else if(firstPattern.equals("fragment"))
            {
                fragmentSource = splitString[1];
            }
            else
            {
                throw new IOException("Unexpected token '" + firstPattern + " ' in ' " + filepath + " '");
            }

            if(secondPattern.equals("vertex"))
            {
                vertexSource = splitString[2];
            }
            else if(secondPattern.equals("fragment"))
            {
                fragmentSource = splitString[2];
            }
            else
            {
                throw new IOException("Unexpected token '" + secondPattern + " ' in ' " + filepath + " '");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: ' " + filepath + " '";
        }

        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }

    public void Compile()
    {
        int vertexId, fragmentId;
        //Compile and Link Shaders
        //Load and compile the Vertex Shaders
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(vertexId, vertexSource);
        glCompileShader(vertexId);
        //Check for compilation errors
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Vertex shader compilation failed at" + filepath);
            System.out.println(glGetShaderInfoLog(vertexId, len));
            assert false : ""; //This will stop the program
        }

        //Load and compile the Fragment Shaders
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(fragmentId, fragmentSource);
        glCompileShader(fragmentId);
        //Check for compilation errors
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Fragment shader compilation failed at " + filepath);
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            assert false : ""; //This will stop the program
        }

        //Link Shaders
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);

        //Check for linking errors
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Linking shader compilation failed at" + filepath);
            System.out.println(glGetProgramInfoLog(shaderProgramId, len));
            assert false : ""; //This will stop the program
        }
    }

    public void Use()
    {
        //Bind Shader Program
        glUseProgram(shaderProgramId);
    }

    public void Detach()
    {
        glUseProgram(0);
    }

    public  void UploadMat4f(String varName, Matrix4f mat4)
    {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}
