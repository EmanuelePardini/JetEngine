package Renderer; // Declare the package for the renderer component

import org.joml.*; // Import JOML for matrix and vector types
import org.lwjgl.BufferUtils; // Import for buffer utilities
import java.io.IOException; // Import for handling IO exceptions
import java.nio.FloatBuffer; // Import for using FloatBuffer
import java.nio.file.Files; // Import for file operations
import java.nio.file.Paths; // Import for file path handling
import static org.lwjgl.opengl.GL11.GL_FALSE; // Import for OpenGL false constant
import static org.lwjgl.opengl.GL20.*; // Import for OpenGL 2.0 functions
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog; // Import for getting program info logs

// Shader class to handle the compilation, linking, and usage of shaders
public class Shader {
    private int shaderProgramId; // ID for the shader program created by linking vertex and fragment shaders
    private boolean beingUsed = false; // Flag to check if the shader is currently in use

    private String vertexSource; // Source code for the vertex shader
    private String fragmentSource; // Source code for the fragment shader
    private String filepath; // Path to the shader source file

    // Constructor to initialize the shader by reading the source file
    public Shader(String filepath) {
        this.filepath = filepath; // Set the file path for the shader source

        try {
            // Read the entire shader source file into a string
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            // Split the source by the #type directive to separate vertex and fragment shaders
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6; // Move index past '#type ' keyword
            int eol = source.indexOf("\r\n", index); // Find the end of the line
            String firstPattern = source.substring(index, eol).trim(); // Get the shader type

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6; // Move index past the second '#type ' keyword
            eol = source.indexOf("\r\n", index); // Find the end of the line
            String secondPattern = source.substring(index, eol).trim(); // Get the shader type

            // Assign the appropriate shader source based on the identified patterns
            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + " ' in ' " + filepath + " '");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + " ' in ' " + filepath + " '");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging
            assert false : "Error: Could not open file for shader: ' " + filepath + " '"; // Ensure shader source was loaded correctly
        }
    }

    // Compiles and links the shaders
    public void Compile() {
        int vertexId, fragmentId; // IDs for the vertex and fragment shaders

        // Load and compile the Vertex Shader
        vertexId = glCreateShader(GL_VERTEX_SHADER); // Create a vertex shader object
        glShaderSource(vertexId, vertexSource); // Pass the vertex shader source code
        glCompileShader(vertexId); // Compile the vertex shader
        // Check for compilation errors
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Vertex shader compilation failed at " + filepath);
            System.out.println(glGetShaderInfoLog(vertexId, len)); // Print shader compilation errors
            assert false : ""; // Stop execution if compilation fails
        }

        // Load and compile the Fragment Shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER); // Create a fragment shader object
        glShaderSource(fragmentId, fragmentSource); // Pass the fragment shader source code
        glCompileShader(fragmentId); // Compile the fragment shader
        // Check for compilation errors
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Fragment shader compilation failed at " + filepath);
            System.out.println(glGetShaderInfoLog(fragmentId, len)); // Print shader compilation errors
            assert false : ""; // Stop execution if compilation fails
        }

        // Link Shaders into a Shader Program
        shaderProgramId = glCreateProgram(); // Create a shader program
        glAttachShader(shaderProgramId, vertexId); // Attach the vertex shader
        glAttachShader(shaderProgramId, fragmentId); // Attach the fragment shader
        glLinkProgram(shaderProgramId); // Link the shaders into a single program

        // Check for linking errors
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Linking shader compilation failed at " + filepath);
            System.out.println(glGetProgramInfoLog(shaderProgramId, len)); // Print program linking errors
            assert false : ""; // Stop execution if linking fails
        }
    }

    // Activate the shader program for use
    public void Use() {
        if (!beingUsed) { // Only use if not already in use
            glUseProgram(shaderProgramId); // Bind the shader program
            beingUsed = true; // Set the flag indicating the shader is in use
        }
    }

    // Detach the currently used shader program
    public void Detach() {
        glUseProgram(0); // Unbind the shader program
        beingUsed = false; // Reset the flag
    }

    // Upload a 4x4 matrix to the shader
    public void UploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        // Create a buffer and pass the matrix data
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); // Create a FloatBuffer of size 16
        mat4.get(matBuffer); // Load matrix data into the buffer
        glUniformMatrix4fv(varLocation, false, matBuffer); // Upload the matrix to the shader
    }

    // Upload a 3x3 matrix to the shader
    public void UploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9); // Create a FloatBuffer of size 9
        mat3.get(matBuffer); // Load matrix data into the buffer
        glUniformMatrix3fv(varLocation, false, matBuffer); // Upload the matrix to the shader
    }

    // Upload a 4D vector to the shader
    public void UploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w); // Upload the vector to the shader
    }

    // Upload a 3D vector to the shader
    public void UploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        glUniform3f(varLocation, vec.x, vec.y, vec.z); // Upload the vector to the shader
    }

    // Upload a 2D vector to the shader
    public void UploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        glUniform2f(varLocation, vec.x, vec.y); // Upload the vector to the shader
    }

    // Upload a float to the shader
    public void UploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        glUniform1f(varLocation, val); // Upload the float to the shader
    }

    // Upload an integer to the shader
    public void UploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        glUniform1i(varLocation, val); // Upload the integer to the shader
    }

    // Upload a texture to the shader
    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the uniform variable in the shader
        Use(); // Ensure the shader is being used

        glUniform1i(varLocation, slot); // Set the texture unit
    }

    // Upload an integer array to the shader
    public void UploadIntArray(String varName, int[] array) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName); // Get the location of the variable in the shader
        Use(); // Ensure the shader is being used

        glUniform1iv(varLocation, array); // Upload the integer array to the shader
    }
}
