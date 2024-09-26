package Renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    private String filePath;
    private int texID;
    private int width,height;

    public Texture(String filePath)
    {
        this.filePath = filePath;

        //Generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set the texture parameter for wrapping behavior along the horizontal axis (U coordinate)
        glTexParameteri(GL_TEXTURE_2D,  //It tells OpenGL that the operations being performed are for a two-dimensional texture mapping.
                GL_TEXTURE_WRAP_S,  //It controls what happens when texture coordinates exceed the bounds of the texture.
                GL_REPEAT);  // It specifies that when the texture coordinates exceed the range of 0.0 to 1.0, the texture should repeat itself.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //When stretching the image, then pixelate
        glTexParameteri(GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER, //Specifies the filtering for minification,
                // happens when the size of the rendered object is less than the size of the texture image.
                GL_NEAREST); //This is the value being set for the GL_TEXTURE_MIN_FILTER parameter. It specifies the minification filter to use


        //When shrinking the image, then pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        if(image != null)
        {
            this.width = width.get(0);
            this.height = height.get(0);

            // Upload the texture data to the GPU for a 2D texture
            if(channels.get(0) == 3)
            {
                glTexImage2D(GL_TEXTURE_2D,        // Target texture type we are operating on (2D texture)
                        0,                   // Level of detail (0 for base level)
                        GL_RGB,            // Internal format of the texture (how it is stored in memory)
                        width.get(0),      // Width of the texture image (retrieved from a list or array)
                        height.get(0),     // Height of the texture image (retrieved from a list or array)
                        0,                   // Border width (must be 0 for 2D textures)
                        GL_RGB,            // Format of the pixel data (color channels used)
                        GL_UNSIGNED_BYTE,   // Data type of the pixel data (8-bit unsigned bytes)
                        image);            // Pointer to the image data (pixel data for the texture)
            }
            else if (channels.get(0) == 4)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0,
                                GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else
            {
                assert false : "Error: (Texture) Unknown number of channels '" + channels.get(0) +"'";
            }
        }
        else
        {
            assert false : "Error: (Texture) Could not load image '" + filePath +"'";
        }

        stbi_image_free(image);
    }

    public void Bind()
    {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void Unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int GetWitdh() { return this.width;}
    public int GetHeight() { return this.height;}

}
