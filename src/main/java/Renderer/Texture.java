package Renderer; // Declare the package for the renderer component

import org.lwjgl.BufferUtils; // Import for buffer utilities
import java.nio.ByteBuffer; // Import for using ByteBuffer
import java.nio.IntBuffer; // Import for using IntBuffer

import static org.lwjgl.opengl.GL11C.*; // Import for OpenGL functions
import static org.lwjgl.stb.STBImage.*; // Import for STB Image functions

// Texture class for loading and managing 2D textures
public class Texture {
    private String filePath; // Path to the texture image file
    private int texID; // OpenGL texture ID

    // Constructor that loads the texture from the specified file path
    public Texture(String filePath) {
        this.filePath = filePath; // Store the file path for future reference

        // Generate texture on the GPU
        texID = glGenTextures(); // Generate a new texture ID
        glBindTexture(GL_TEXTURE_2D, texID); // Bind the texture for configuration

        // Set texture parameters for wrapping behavior along the horizontal axis (U coordinate)
        glTexParameteri(GL_TEXTURE_2D,  // Specify the target texture type (2D texture)
                GL_TEXTURE_WRAP_S,  // Specify the wrapping method for the U coordinate
                GL_REPEAT);  // Repeat the texture if coordinates exceed 0.0 to 1.0

        // Set texture parameters for wrapping along the vertical axis (V coordinate)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // Same as above for V coordinate

        // Set minification filter to pixelate when shrinking the image
        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER, // Specify the filtering method when the image is shrunk
                GL_NEAREST); // Use nearest neighbor filtering for minification

        // Set magnification filter to pixelate when enlarging the image
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // Use nearest neighbor filtering for magnification

        // Buffers to hold image dimensions and number of channels
        IntBuffer width = BufferUtils.createIntBuffer(1); // Buffer for width
        IntBuffer height = BufferUtils.createIntBuffer(1); // Buffer for height
        IntBuffer channels = BufferUtils.createIntBuffer(1); // Buffer for channels

        // Set STB image to flip the image vertically upon loading (OpenGL's origin is at the bottom-left)
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0); // Load the image

        if(image != null) { // Check if the image was loaded successfully
            // Upload the texture data to the GPU for a 2D texture
            if(channels.get(0) == 3) { // If the image has 3 color channels (RGB)
                glTexImage2D(GL_TEXTURE_2D,        // Target texture type we are operating on (2D texture)
                        0,                   // Level of detail (0 for base level)
                        GL_RGB,            // Internal format of the texture (how it is stored in memory)
                        width.get(0),      // Width of the texture image
                        height.get(0),     // Height of the texture image
                        0,                   // Border width (must be 0 for 2D textures)
                        GL_RGB,            // Format of the pixel data (color channels used)
                        GL_UNSIGNED_BYTE,   // Data type of the pixel data (8-bit unsigned bytes)
                        image);            // Pointer to the image data
            } else if (channels.get(0) == 4) { // If the image has 4 color channels (RGBA)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0,
                        GL_RGBA, GL_UNSIGNED_BYTE, image); // Upload the texture data with RGBA format
            } else {
                assert false : "Error: (Texture) Unknown number of channels '" + channels.get(0) + "'"; // Handle unexpected number of channels
            }
        } else {
            assert false : "Error: (Texture) Could not load image '" + filePath + "'"; // Handle error in loading image
        }

        stbi_image_free(image); // Free the image memory allocated by STB
    }

    // Method to bind the texture for rendering
    public void Bind() {
        glBindTexture(GL_TEXTURE_2D, texID); // Bind the texture to the GL_TEXTURE_2D target
    }

    // Method to unbind the texture
    public void Unbind() {
        glBindTexture(GL_TEXTURE_2D, 0); // Unbind the texture by binding to 0
    }
}
