package Renderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

//The main problem of this pattern is that we are not really separating leveleditor and level
public class PickingTexture {
    private int pickingTextureId;
    private int fbo; //Frame buffer id
    private int depthTexture; //This is not really necessary for 2d


    public PickingTexture(int width, int height) {
        if (!Init(width, height)) {
            assert false : "Error initializing picking texture";
        }
    }

    //It draws another layer on the viewport where we can click to take the id of the contained textures by IDs
    public boolean Init(int width, int height) {
        // Generate framebuffer
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // Create the texture to render the data to, and attach it to our framebuffer
        pickingTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, pickingTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0,
                GL_RGB, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                this.pickingTextureId, 0);

        // Create the texture object for the depth buffer
        glEnable(GL_TEXTURE_2D);
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0,
                GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
                GL_TEXTURE_2D, depthTexture, 0);

        // Disable the reading
        glReadBuffer(GL_NONE);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            assert false : "Error: Framebuffer is not complete";
            return false;
        }

        // Unbind the texture and framebuffer
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        return true;
    }

    // Enable rendering to the picking framebuffer (write mode).
    public void EnableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo); // Bind the FBO for drawing.
    }

    // Disable rendering to the picking framebuffer (go back to default framebuffer).
    public void DisableWriting() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0); // Bind the default framebuffer (window).
    }

    // Read the pixel at the specified screen coordinates (x, y) from the picking texture.
    public int ReadPixel(int x, int y) {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo); // Bind the picking FBO for reading.
        glReadBuffer(GL_COLOR_ATTACHMENT0); // Set the read buffer to the color attachment.

        // Allocate an array to hold the RGB values of the pixel.
        float pixels[] = new float[3];
        glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels); // Read the pixel color from the picking texture.

        return (int)(pixels[0]) - 1; //It contains the Object Id
    }
}
