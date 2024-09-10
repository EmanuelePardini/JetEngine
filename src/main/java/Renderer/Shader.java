package Renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import javax.print.DocFlavor;
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
    private boolean beingUsed = false;

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
        if(!beingUsed)
        {
            //Bind Shader Program
            glUseProgram(shaderProgramId);
            beingUsed = true;
        }

    }

    public void Detach()
    {
        glUseProgram(0);
        beingUsed = false;
    }

    public  void UploadMat4f(String varName, Matrix4f mat4)
    {
        //bind var you are searching with shader you are searching in
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        Use();

        //we need to flatten matrix, in a 16 element array, so we create a buffer and pass it the matrix [1,1,1,1...]
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public  void UploadMat3f(String varName, Matrix3f mat3)
    {

        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        Use();


        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9); //care 3x3 matrix has 9 elements
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void UploadVec4f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        Use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void UploadVec3f(String varName, Vector3f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        Use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void UploadVec2f(String varName, Vector2f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        Use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public  void UploadFloat(String varName, float val)
    {
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        Use();
        glUniform1f(varLocation, val);
    }

    public void UploadInt(String varName, int val)
    {
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        Use();
        glUniform1i(varLocation, val);
    }
}
